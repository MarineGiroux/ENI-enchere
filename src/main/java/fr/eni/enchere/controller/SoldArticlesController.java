package fr.eni.enchere.controller;

import fr.eni.enchere.bll.*;
import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.PickUp;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/vente")
public class SoldArticlesController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SoldArticlesController.class);

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
		LOGGER.info("Init article model");
		model.addAttribute("soldArticleViewModel", new SoldArticleViewModel());
		model.addAttribute("listCategory", categoryService.findAll());
		return "sell";
	}

	@PostMapping
	public String createArticle(@Valid @ModelAttribute("soldArticleViewModel") SoldArticleViewModel soldArticleViewModel,
								BindingResult bindingResult, Principal principal, Model model) {
		if (!bindingResult.hasErrors()) {
			LOGGER.debug("Sold article {}", soldArticleViewModel);
			try {
				controlAddress(soldArticleViewModel.getPickUpLocation(), principal.getName(), model);

				SoldArticles soldArticles = soldArticleViewModel.getSoldArticles();
				soldArticles.setIdUser(userService.findByEmail(principal.getName()).getIdUser());

				this.soldArticlesService.add(soldArticleViewModel);

				return "redirect:/";
			} catch (BusinessException e) {
				LOGGER.error("Error while creation soldArticleViewModel", e);
				e.getlistErrors().forEach(erreur -> {
					ObjectError error = new ObjectError("globalError", erreur);
					bindingResult.addError(error);
				});
			}
		}

		LOGGER.debug("soldArticleViewModel {} , errors {}", soldArticleViewModel, bindingResult.getAllErrors());
		model.addAttribute("listCategory", categoryService.findAll());
		model.addAttribute("soldArticleViewModel", soldArticleViewModel);

		return "sell";
	}

	private void controlAddress(PickUp pickUpLocation, String emailUser, Model model) {
		User user = userService.findByEmail(emailUser);
		if (pickUpLocation.getRoad() == null || pickUpLocation.getZipPass() == null || pickUpLocation.getCity() == null) {
			model.addAttribute("user", user);
		} else {
			// TODO créer une erreur avec BindingResult
			System.out.println("je sais pas");
		}
	}
	
	@GetMapping("/detail")
	public String showArticle(@RequestParam(name = "id", required = true)int id, Model model) {
		System.out.println("Affichage article vendu " + id);
		SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
		SoldArticles soldArticles = soldArticlesService.findById(id);
		soldArticleViewModel.setSoldArticles(soldArticles);
		soldArticleViewModel.setPickUpLocation(pickUpService.findByNum(id));
		
		model.addAttribute("soldArticleViewModel", soldArticleViewModel);
		
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
		auctions.setSoldArticle(soldArticlesService.findById(idArticle));
		auctions.setDateAuctions(java.time.LocalDate.now());
		auctions.setAmountAuctions(amountAuction);
		this.auctionsService.bid(auctions);
		return "redirect:/vente/detail?id=" + idArticle;
	}
	
	

}
