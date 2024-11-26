package fr.eni.enchere.controller;

import java.security.Principal;

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

import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/utilisateur")
public class UtilisateurController {
	
	@Autowired
	private UserService userService;

		@GetMapping("/inscription")
		public String creerUtilisateur(Model model) {
			System.out.println("début creer user");
			
			model.addAttribute("utilisateur", new User());
			return "inscription";
		}
		
	@PostMapping("/inscription")
		public String creerUtilisateur(
				@Valid @ModelAttribute("utilisateur") User user,
				BindingResult bindingResult, @RequestParam("confirmMdP") String confirmMdP) {

			System.out.println("mdp user = " + user.getPassWord());
			
			if (bindingResult.hasErrors()) {
				System.out.println(bindingResult.getAllErrors());
				return "inscription";
			} else {
				System.out.println("UTILISATEUR = " + user);
				try {
					this.userService.add(user, confirmMdP);
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
		System.out.println("supprimer user : " + principal.getName());
		userService.deleteAccountByEmail(principal.getName());
		return "redirect:/logout";

	}
	
	@GetMapping("/update")
	public String modifierUtilisateur(User user, Principal principal, Model model) {
		controlUtilisateur(principal.getName(), model);
		return "update";
		
	}
	
	@PostMapping("/profil")
	public String modificationUtilisateurEnregistrer(
			@Valid @ModelAttribute(name = "utilisateur") User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("erreur de validation modif");
			System.out.println(bindingResult.getAllErrors());
			return "update";
		} else {
			System.out.println("user : " + user);
			try {
				this.userService.update(user);
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
		User user = userService.findByEmail(emailUtilisateur);
		if(emailUtilisateur != null) {
					model.addAttribute("utilisateur", user);
					System.out.println("user" + emailUtilisateur);
		}else {
			System.out.println("user inconnu");
		}
		System.out.println("email user = " + emailUtilisateur);
	}
	
		
}
