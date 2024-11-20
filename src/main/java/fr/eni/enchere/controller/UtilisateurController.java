package fr.eni.enchere.controller;

import java.security.Principal;
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

import fr.eni.enchere.bll.UtilisateurService;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/utilisateur")
public class UtilisateurController {
	
	@Autowired
	private UtilisateurService utilisateurService;

		@GetMapping("/inscription")
		public String creerUtilisateur(Model model) {
			System.out.println("début creer utilisateur");
			
			model.addAttribute("utilisateur", new Utilisateur());
			return "inscription";
		}
		
	@PostMapping("/inscription")
		public String creerUtilisateur(
				@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
				BindingResult bindingResult, @RequestParam("confirmMdP") String confirmMdP) {

			System.out.println("mdp utilisateur = " + utilisateur.getMotDePasse());
			
			if (bindingResult.hasErrors()) {
				System.out.println(bindingResult.getAllErrors());
				return "inscription";
			} else {
				System.out.println("UTILISATEUR = " + utilisateur);
				try {
					this.utilisateurService.add(utilisateur, confirmMdP);
					return "redirect:/login";
				} catch (BusinessException e) {
					e.getListeErreurs().forEach(
							erreur -> {
								ObjectError error = new ObjectError("globalError", erreur);
								bindingResult.addError(error);
							}
					);
					return "inscription";
			}
		}
	}
	
	@GetMapping("/profil")
	public String afficherUtilisateur(Principal principal, Model model) {
//		System.out.println("principal.getName() : " + principal.getName());
		controlUtilisateur(principal.getName(), model);
		return "profil";
	
	}
	
	
	@GetMapping("/delete")
	public String deleteUtilisateur(Principal principal, Model model) {
		System.out.println("supprimer utilisateur : " + principal.getName());
		utilisateurService.deleteAccountByEmail(principal.getName());
		return "redirect:/logout";

	}
	
	@GetMapping("/update")
	public String modifierUtilisateur(Utilisateur utilisateur, Principal principal, Model model) {
		controlUtilisateur(principal.getName(), model);
		return "update";
		
	}
	
	@PostMapping("/profil")
	public String modificationUtilisateurEnregistrer(
			@Valid @ModelAttribute(name = "utilisateur") Utilisateur utilisateur, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("erreur de validation modif");
			System.out.println(bindingResult.getAllErrors());
			return "update";
		} else {
			System.out.println("utilisateur : " + utilisateur);
			try {
				this.utilisateurService.update(utilisateur);
				return "profil";
			} catch (BusinessException e) {
				e.getListeErreurs().forEach(
						erreur -> {
							ObjectError error = new ObjectError("globalError", erreur);
							bindingResult.addError(error);
						}
				);
				return "update";
			}
		}
	}
	
	
	private void controlUtilisateur(String emailUtilisateur, Model model) {
		Utilisateur utilisateur = utilisateurService.findByEmail(emailUtilisateur);
		if(emailUtilisateur != null) {
					model.addAttribute("utilisateur",utilisateur);
					System.out.println("utilisateur" + emailUtilisateur);
		}else {
			System.out.println("utilisateur inconnu");
		}
		System.out.println("email utilisateur = " + emailUtilisateur);
	}
	
		
}
