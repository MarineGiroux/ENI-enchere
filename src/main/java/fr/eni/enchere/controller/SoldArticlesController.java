package fr.eni.enchere.controller;

import java.security.Principal;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.enchere.bll.SoldArticlesService;
import fr.eni.enchere.bll.CategoryService;
import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bll.PickUpService;
import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.Category;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vente")
public class SoldArticlesController {

	@Autowired
	private SoldArticlesService soldArticlesService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PickUpService pickUpService;
	@Autowired
	private AuctionsService auctionsService;


	public SoldArticlesController(SoldArticlesService soldArticlesService, UserService userService,
								  CategoryService categoryService, PickUpService pickUpService, AuctionsService auctionsService) {
		this.soldArticlesService = soldArticlesService;
		this.userService = userService;
		this.categoryService = categoryService;
		this.pickUpService = pickUpService;
		this.auctionsService = auctionsService;
	}

	@GetMapping
	public String createSoldArticle(Model model) {
		System.out.println("début creer article");
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		model.addAttribute("article", new SoldArticles());
		// model.addAttribute("user", new Utilisateur());
		return "sell";
	}

	@PostMapping
	public String createArticle(@Valid @ModelAttribute("soldArticle") SoldArticles soldArticles,
								BindingResult bindingResult, Principal principal, Model model) {
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			return "sell";
		} else {
			System.out.println("Article vendu = " + soldArticles);
			try {
				controlAddress(soldArticles, principal.getName(), model);
				this.soldArticlesService.add(soldArticles, userService.findByEmail(principal.getName()));
				soldArticles.getPickUpLocation().setIdArticle(soldArticles.getIdArticle());
				this.pickUpService.createAdress(soldArticles.getPickUpLocation());
				return "redirect:/";
			} catch (BusinessException e) {
				e.getlistErrors().forEach(erreur -> {
					ObjectError error = new ObjectError("globalError", erreur);
					bindingResult.addError(error);
				});
				return "sell";
			}
		}
	}

	private void controlAddress(SoldArticles soldArticles, String emailUser, Model model) {
		User user = userService.findByEmail(emailUser);
		if (soldArticles.getPickUpLocation().getRoad() == null || soldArticles.getPickUpLocation().getZipPass() == null
				|| soldArticles.getPickUpLocation().getCity() == null) {
			model.addAttribute("user", user);
		} else {
			System.out.println("je sais pas");
		}
	}
	
	@GetMapping("/detail")
	public String showArticle(@RequestParam(name = "id", required = true)int id, Model model) {
		System.out.println("Affichage article vendu " + id);
		SoldArticles soldArticles = soldArticlesService.FindById(id);
		soldArticles.setPickUpLocation(pickUpService.findByNum(id));
		
		model.addAttribute("soldArticle", soldArticles);
		
		return "detail-sale";
	}
	
	@GetMapping("/auctions")
	public String bid(Model model) {
		System.out.println("help");
		model.addAttribute("auctions", auctionsService.recoverAuctions());
		return"redirect:/detail";
	}

	@PostMapping("/auctions")
	public String enregistrerEnchere(@RequestParam("idArticle") int idArticle,
									 @RequestParam("amountAuction") int amountAuction,
									 Principal principal,
									 Model model) {
		Auctions auctions = new Auctions();
		auctions.setUser(userService.findByEmail(principal.getName()));
		auctions.setSoldArticle(soldArticlesService.FindById(idArticle));
		auctions.setDateAuctions(java.time.LocalDate.now());
		auctions.setAmountAuctions(amountAuction);
		this.auctionsService.bid(auctions);
		return "redirect:/vente/detail?id=" + idArticle;
	}
	
	

}
