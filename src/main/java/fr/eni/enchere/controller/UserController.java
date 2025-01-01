package fr.eni.enchere.controller;

import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.dal.UserDAOImpl;
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
@RequestMapping("/user")
public class UserController {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
    @Autowired
    private UserDAOImpl userDAOImpl;

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
		controlUser(principal.getName(), model);
		return "profile";
	
	}
	
	
	@GetMapping("/delete")
	public String deleteUser(Principal principal, Model model) {
		userService.deleteAccountByEmail(principal.getName());
		return "redirect:/logout";

	}
	
	@GetMapping("/update")
	public String updateUser(User user, Principal principal, Model model) {
		LOGGER.info("Update user : {}", user);
		controlUser(principal.getName(), model);
		return "update";
		
	}
	
	@PostMapping("/profile")
	public String editUserSave(
			@Valid @ModelAttribute(name = "user") User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "update";
		} else {
			try {
				this.userService.update(user);
				return "profile";
			} catch (BusinessException e) {
				LOGGER.error("Error while updating user {}", user, e);
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
		if (emailUser != null) {
			LOGGER.debug("User : {}", user);
			model.addAttribute("user", user);
		} else {
			LOGGER.warn("User inconnu");
		}
	}

	@GetMapping("/seller/{id}")
	public String showSellerProfile(@PathVariable("id") int sellerId, Model model) {
		User seller = userDAOImpl.findByNum(sellerId);
		if (seller != null) {
			model.addAttribute("user", seller);
			model.addAttribute("isSeller", true);
			return "profile";
		}
		return "redirect:/error";
	}

}
