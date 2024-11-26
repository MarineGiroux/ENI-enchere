package fr.eni.enchere.controller;

import java.security.Principal;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.ItemSold;
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

import fr.eni.enchere.bll.ItemSoldService;
import fr.eni.enchere.bll.CategoryService;
import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bll.PickUpService;
import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vente")
public class ItemSoldController {

	@Autowired
	private ItemSoldService itemSoldService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PickUpService pickUpService;
	@Autowired
	private AuctionsService auctionsService;


	public ItemSoldController(ItemSoldService itemSoldService, UserService userService,
							  CategoryService categoryService, PickUpService pickUpService, AuctionsService auctionsService) {
		this.itemSoldService = itemSoldService;
		this.userService = userService;
		this.categoryService = categoryService;
		this.pickUpService = pickUpService;
		this.auctionsService = auctionsService;
	}

	@GetMapping
	public String createItemSold(Model model) {
		System.out.println("début creer article");
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		model.addAttribute("article", new ItemSold());
		// model.addAttribute("user", new Utilisateur());
		return "sell";
	}

	@PostMapping
	public String createArticle(@Valid @ModelAttribute("itemSold") ItemSold itemSold,
								BindingResult bindingResult, Principal principal, Model model) {
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			return "sell";
		} else {
			System.out.println("Article vendu = " + itemSold);
			try {
				controlAddress(itemSold, principal.getName(), model);
				this.itemSoldService.add(itemSold, userService.findByEmail(principal.getName()));
				itemSold.getPickUpLocation().setIdArticle(itemSold.getIdArticle());
				this.pickUpService.createAdress(itemSold.getPickUpLocation());
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

	private void controlAddress(ItemSold itemSold, String emailUser, Model model) {
		User user = userService.findByEmail(emailUser);
		if (itemSold.getPickUpLocation().getRoad() == null || itemSold.getPickUpLocation().getZipPass() == null
				|| itemSold.getPickUpLocation().getCity() == null) {
			model.addAttribute("user", user);
		} else {
			System.out.println("je sais pas");
		}
	}
	
	@GetMapping("/detail")
	public String showArticle(@RequestParam(name = "id", required = true)int id, Model model) {
		System.out.println("Affichage article vendu " + id);
		ItemSold itemSold = itemSoldService.FindById(id);
		itemSold.setPickUpLocation(pickUpService.findByNum(id));
		
		model.addAttribute("itemSold", itemSold);
		
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
		auctions.setItemSold(itemSoldService.FindById(idArticle));
		auctions.setDateAuctions(java.time.LocalDate.now());
		auctions.setAmountAuctions(amountAuction);
		this.auctionsService.bid(auctions);
		return "redirect:/vente/detail?id=" + idArticle;
	}
	
	

}
