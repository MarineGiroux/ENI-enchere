package fr.eni.enchere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*import fr.eni.enchere.bll.EnchereService;
import fr.eni.enchere.bo.Enchere;

@Controller
@RequestMapping("vente/detail")
public class EnchereController {

    @Autowired
    private EnchereService enchereService;

    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    @GetMapping
    public String encherir(Model model) {
        System.out.println("help");

        model.addAttribute("enchere", new Enchere());
        return"";
    }

}*/