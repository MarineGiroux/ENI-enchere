package fr.eni.enchere.controller;

import java.security.Principal;
import java.util.List;

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

import fr.eni.enchere.bll.ArticleVenduService;
import fr.eni.enchere.bll.CategorieService;
import fr.eni.enchere.bll.EnchereService;
import fr.eni.enchere.bll.RetraitService;
import fr.eni.enchere.bll.UtilisateurService;
import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Retrait;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.ArticleVenduDAO;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vente")
public class ArticleVenduController {

	@Autowired
	private ArticleVenduService articleVenduService;
	@Autowired
	private UtilisateurService utilisateurService;
	@Autowired
	private CategorieService categorieService;
	@Autowired
	private RetraitService retraitService;
	@Autowired
	private EnchereService enchereService;


	public ArticleVenduController(ArticleVenduService articleVenduService, UtilisateurService utilisateurService,
			CategorieService categorieService, RetraitService retraitService, EnchereService enchereService) {
		this.articleVenduService = articleVenduService;
		this.utilisateurService = utilisateurService;
		this.categorieService = categorieService;
		this.retraitService = retraitService;
		this.enchereService = enchereService;
	}

	@GetMapping
	public String creerAticleVendu(Model model) {
		System.out.println("début creer article");
		List<Categorie> categorie = categorieService.FindAll();
		model.addAttribute("listeCategorie", categorie);
		model.addAttribute("article", new ArticleVendu());
		// model.addAttribute("utilisateur", new Utilisateur());
		return "vente";
	}

	@PostMapping
	public String creerArticle(@Valid @ModelAttribute("articleVendu") ArticleVendu articleVendu,
			BindingResult bindingResult, Principal principal, Model model) {
		List<Categorie> categorie = categorieService.FindAll();
		model.addAttribute("listeCategorie", categorie);
		if (bindingResult.hasErrors()) {
			System.out.println(bindingResult.getAllErrors());
			return "vente";
		} else {
			System.out.println("Article vendu = " + articleVendu);
			try {
				controlAdresse(articleVendu, principal.getName(), model);
				this.articleVenduService.add(articleVendu, utilisateurService.findByEmail(principal.getName()));
				articleVendu.getLieuRetrait().setNoArticle(articleVendu.getNoArticle());
				this.retraitService.createAdresse(articleVendu.getLieuRetrait());
				return "redirect:/";
			} catch (BusinessException e) {
				e.getListeErreurs().forEach(erreur -> {
					ObjectError error = new ObjectError("globalError", erreur);
					bindingResult.addError(error);
				});
				return "vente";
			}
		}
	}

	private void controlAdresse(ArticleVendu articleVendu, String emailUtilisateur, Model model) {
		Utilisateur utilisateur = utilisateurService.findByEmail(emailUtilisateur);
		if (articleVendu.getLieuRetrait().getRue() == null || articleVendu.getLieuRetrait().getCodePostal() == null
				|| articleVendu.getLieuRetrait().getVille() == null) {
			model.addAttribute("utilisateur", utilisateur);
		} else {
			System.out.println("je sais pas");
		}
	}
	
	@GetMapping("/detail")
	public String afficherArticle(@RequestParam(name = "id", required = true)int id, Model model) {
		System.out.println("Affichage article vendu " + id);
		ArticleVendu articleVendu = articleVenduService.FindById(id);
		articleVendu.setLieuRetrait(retraitService.findByNum(id));
		
		model.addAttribute("articleVendu", articleVendu);
		
		return "detail-vente";
	}
	
	@GetMapping("/enchere")
	public String encherir(Model model) {
		System.out.println("help");
		model.addAttribute("enchere", enchereService.recupererEncheres());
		return"redirect:/detail";
	}

	@PostMapping("/enchere")
	public String enregistrerEnchere(@RequestParam("noArticle") int noArticle,
									 @RequestParam("montantEnchere") int montantEnchere,
									 Principal principal,
									 Model model) {
		Enchere enchere = new Enchere();
		enchere.setUtilisateur(utilisateurService.findByEmail(principal.getName()));
		enchere.setArticleVendu(articleVenduService.FindById(noArticle));
		enchere.setDateEnchere(java.time.LocalDate.now());
		enchere.setMontantEnchere(montantEnchere);
		this.enchereService.encherir(enchere);
		return "redirect:/vente/detail?id=" + noArticle;
	}
	
	

}
