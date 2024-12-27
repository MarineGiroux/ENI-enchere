package fr.eni.enchere.controller;


import java.util.List;

import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
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
	private SoldArticlesService soldArticlesService;
	@Autowired
	private CategoryService categoryService;
	
	public IndexController(SoldArticlesService soldArticlesService, CategoryService categoryService) {
		this.soldArticlesService = soldArticlesService;
		this.categoryService = categoryService;
	}

	@GetMapping
	public String index(Model model) {
		List<SoldArticleViewModel> soldArticles = soldArticlesService.findAll();
		model.addAttribute("soldArticlesViewModel", soldArticles);

		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);

		return "index";
	}
	
	@GetMapping("/category")
	public String filterArticle(@RequestParam(name = "id", required = true) int idCategory, Model model) {
		List<SoldArticleViewModel> soldArticles = soldArticlesService.findByIdCategory(idCategory);
		model.addAttribute("soldArticlesViewModel", soldArticles);

		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);

		return "index";
	}
	
	@GetMapping("/search")
	public String searchByName(@RequestParam(name = "searchArticleName", required = true) String searchArticleName, Model model) {
		List<SoldArticleViewModel> soldArticles = soldArticlesService.searchByName(searchArticleName);
		model.addAttribute("soldArticlesViewModel", soldArticles);
		
		List<Category> category = categoryService.findAll();
		model.addAttribute("listCategory", category);

		return "index";
	}
	
	
	
}
