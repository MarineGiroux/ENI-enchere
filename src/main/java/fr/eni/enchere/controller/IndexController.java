package fr.eni.enchere.controller;


import java.util.List;
import java.util.stream.Collectors;

import fr.eni.enchere.bo.SoldArticles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.enchere.bll.SoldArticlesService;
import fr.eni.enchere.bll.CategoryService;
import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.Category;


@Controller
public class IndexController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private AuctionsService auctionsService;
	@Autowired
	private SoldArticlesService soldArticlesService;
	@Autowired
	private CategoryService categoryService;
	
	public IndexController(UserService userService, AuctionsService auctionsService,
						   SoldArticlesService soldArticlesService, CategoryService categoryService) {
		this.userService = userService;
		this.auctionsService = auctionsService;
		this.soldArticlesService = soldArticlesService;
		this.categoryService = categoryService;
	}

	@GetMapping
	public String index(Model model) {
		List<SoldArticles> soldArticles = soldArticlesService.findAll();
		model.addAttribute("soldArticle", soldArticles);
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		return "index";
	}
	
	@GetMapping("/category")
	public String filterArticle(@RequestParam(name = "id", required = true) long id, Model model) {
		List<SoldArticles> soldArticles = soldArticlesService.findAll().stream()
							.filter(f -> f.getCartegoryArticle().getIdCategory() == id)
							.collect(Collectors.toList());
		model.addAttribute("soldArticle", soldArticles);
		
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		return "index";
	}
	
	@GetMapping("/search")
	public String rechercheArticle(@RequestParam(name = "name", required = true) String name, Model model) {
		List<SoldArticles> soldArticles = soldArticlesService.findAll().stream()
							.filter(f -> f.getNameArticle().toLowerCase().contains(name.toLowerCase()))
							.collect(Collectors.toList());
		model.addAttribute("soldArticle", soldArticles);
		
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);
		return "index";
	}
	
	
	
}
