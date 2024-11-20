package fr.eni.enchere.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.eni.enchere.bll.ArticleVenduService;
import fr.eni.enchere.bll.CategorieService;
import fr.eni.enchere.bll.EnchereService;
import fr.eni.enchere.bll.UtilisateurService;
import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;


@Controller
public class IndexController {
	
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private EnchereService enchereService;
	@Autowired
	private ArticleVenduService articleVenduService;
	@Autowired
	private CategorieService categorieService;
	
	public IndexController(UtilisateurService utilisateurService, EnchereService enchereService,
			ArticleVenduService articleVenduService, CategorieService categorieService) {
		this.utilisateurService = utilisateurService;
		this.enchereService = enchereService;
		this.articleVenduService = articleVenduService;
		this.categorieService = categorieService;
	}

	@GetMapping
	public String index(Model model) {
		List<ArticleVendu> articleVendu = articleVenduService.FindAll();
		model.addAttribute("articleVendu",articleVendu);
		List<Categorie> categorie = categorieService.FindAll();
		model.addAttribute("listeCategorie", categorie);
		return "index";
	}
	
	@GetMapping("/categorie")
	public String filtreArticle(@RequestParam(name = "id", required = true) long id, Model model) {
		List<ArticleVendu> articleVendu = articleVenduService.FindAll().stream()
							.filter(f -> f.getCategorieArticle().getNoCategorie() == id)
							.collect(Collectors.toList());
		model.addAttribute("articleVendu",articleVendu);
		
		List<Categorie> categorie = categorieService.FindAll();
		model.addAttribute("listeCategorie", categorie);
		return "index";
	}
	
	@GetMapping("/rechercher")
	public String rechercheArticle(@RequestParam(name = "nom", required = true) String nom, Model model) {
		List<ArticleVendu> articleVendu = articleVenduService.FindAll().stream()
							.filter(f -> f.getNomArticle().toLowerCase().contains(nom.toLowerCase()))
							.collect(Collectors.toList());
		model.addAttribute("articleVendu",articleVendu);
		
		List<Categorie> categorie = categorieService.FindAll();
		model.addAttribute("listeCategorie", categorie);
		return "index";
	}
	
	
	
}
