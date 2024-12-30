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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/sales")
public class SalesController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SalesController.class);

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private SoldArticlesService soldArticlesService;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PickUpService pickUpService;
	@Autowired @Lazy
	private AuctionsService auctionsService;
    @Autowired
    private FileService fileService;


	public SalesController(SoldArticlesService soldArticlesService, UserService userService,
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
	public String createArticle(
			@Valid @ModelAttribute("soldArticleViewModel") SoldArticleViewModel soldArticleViewModel,
			BindingResult bindingResult,
			@RequestParam(value = "picture", required = false) MultipartFile picture,
			Principal principal,
			Model model) {

		if (soldArticleViewModel.getSoldArticles().getEndDateAuctions() != null &&
				soldArticleViewModel.getSoldArticles().getStartDateAuctions() != null &&
				!soldArticleViewModel.getSoldArticles().getEndDateAuctions()
						.isAfter(soldArticleViewModel.getSoldArticles().getStartDateAuctions())) {
			bindingResult.rejectValue("soldArticles.endDateAuctions",
					"DateRange.soldArticles",
					"La date de fin doit être postérieure à la date de début");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("listCategory", categoryService.findAll());
			return "sell";
		}

		try {
			String picturePath = fileService.saveFile(picture);
			if (picturePath != null) {
				soldArticleViewModel.getSoldArticles().setPicture(picturePath);
			}
			SoldArticles soldArticles = soldArticleViewModel.getSoldArticles();
			soldArticles.setIdUser(userService.findByEmail(principal.getName()).getIdUser());
			soldArticlesService.add(soldArticleViewModel);

			return "redirect:/";
		} catch (Exception e) {
			LOGGER.error("Error during article creation", e);
			bindingResult.rejectValue("soldArticles.picture", "error.picture", "Erreur lors de l'upload.");
			model.addAttribute("listCategory", categoryService.findAll());
			return "sell";
		}
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
	public String showArticle(@RequestParam(name = "id", required = true) int id, Model model) {
		model.addAttribute("soldArticleViewModel", soldArticlesService.findById(id));
		
		return "detail-sale";
	}
	
	@GetMapping("/auctions")
	public String bid(Model model) {
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
		auctions.setSoldArticle(soldArticlesService.findById(idArticle).getSoldArticles());
		auctions.setDateAuctions(java.time.LocalDate.now());
		auctions.setAmountAuctions(amountAuction);
		this.auctionsService.bid(auctions);
		return "redirect:/sales/detail?id=" + idArticle;
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") int id, Model model, Principal principal) {
		SoldArticleViewModel viewModel = soldArticlesService.findById(id);

		if (viewModel.getSoldArticles().getIdUser() !=
				userService.findByEmail(principal.getName()).getIdUser()) {
			return "redirect:/error";
		}

		LOGGER.debug("Date de début : {}", viewModel.getSoldArticles().getStartDateAuctions());

		model.addAttribute("article", viewModel.getSoldArticles());
		model.addAttribute("categories", categoryService.findAll());
		return "updateArticle";
	}

	@PostMapping("/edit")
	public String updateArticle(
			@Valid @ModelAttribute("article") SoldArticles article,
			BindingResult bindingResult,
			@RequestParam(value = "pictureFile", required = false) MultipartFile pictureFile,
			Principal principal,
			Model model) {

		if (article.getEndDateAuctions() != null &&
				article.getStartDateAuctions() != null &&
				!article.getEndDateAuctions().isAfter(article.getStartDateAuctions())) {
			bindingResult.rejectValue("endDateAuctions",
					"DateRange",
					"La date de fin doit être postérieure à la date de début");
		}

		if (bindingResult.hasErrors()) {
			LOGGER.debug("Date reçue : {}", article.getStartDateAuctions());

			model.addAttribute("categories", categoryService.findAll());
			return "updateArticle";
		}

		try {
			if (pictureFile != null && !pictureFile.isEmpty()) {
				String picturePath = fileService.saveFile(pictureFile);
				if (picturePath != null) {
					article.setPicture(picturePath);
				}
			}

			soldArticlesService.update(article);
			return "redirect:/sales/detail?id=" + article.getIdArticle();
		} catch (BusinessException e) {
			e.getlistErrors().forEach(error ->
					bindingResult.addError(new ObjectError("globalError", error)));
			model.addAttribute("categories", categoryService.findAll());
			return "updateArticle";
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


	@GetMapping("/deleteArticle/{id}")
	public String deleteArticle(@PathVariable("id") int id, Principal principal) {
		SoldArticles article = soldArticlesService.findById(id).getSoldArticles();
		if (article.getIdUser() != userService.findByEmail(principal.getName()).getIdUser()) {
			return "redirect:/error";
		}
		soldArticlesService.deleteArticleById(String.valueOf(id));
		return "redirect:/";
	}

}
