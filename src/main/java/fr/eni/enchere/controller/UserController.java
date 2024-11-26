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
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/inscription")
	public String createUser(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@PostMapping("/inscription")
	public String createUser(
			@Valid @ModelAttribute("user") User user,
			BindingResult bindingResult,
			@RequestParam("confirmPassword") String confirmPassword) {

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		try {
			userService.add(user, confirmPassword);
			return "redirect:/login";
		} catch (BusinessException e) {
			e.getlistErrors().forEach(error -> bindingResult.addError(new ObjectError("globalError", error)));
			return "registration";
		}
	}
	
	@GetMapping("/profile")
	public String showUser(Principal principal, Model model) {
//		System.out.println("principal.getName() : " + principal.getName());
		controlUser(principal.getName(), model);
		return "profile";
	
	}
	
	
	@GetMapping("/delete")
	public String deleteUser(Principal principal, Model model) {
		System.out.println("supprimer user : " + principal.getName());
		userService.deleteAccountByEmail(principal.getName());
		return "redirect:/logout";

	}
	
	@GetMapping("/update")
	public String updateUser(User user, Principal principal, Model model) {
		controlUser(principal.getName(), model);
		return "update";
		
	}
	
	@PostMapping("/profile")
	public String editUserSave(
			@Valid @ModelAttribute(name = "user") User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("erreur de validation modif");
			System.out.println(bindingResult.getAllErrors());
			return "update";
		} else {
			System.out.println("user : " + user);
			try {
				this.userService.update(user);
				return "profile";
			} catch (BusinessException e) {
				e.getlistErrors().forEach(
						erreur -> {
							ObjectError error = new ObjectError("globalError", erreur);
							bindingResult.addError(error);
						}
				);
				return "update";
			}
		}
	}
	
	
	private void controlUser(String emailUser, Model model) {
		User user = userService.findByEmail(emailUser);
		if(emailUser != null) {
					model.addAttribute("user", user);
					System.out.println("user" + emailUser);
		}else {
			System.out.println("user inconnu");
		}
		System.out.println("email user = " + emailUser);
	}
	
		
}
