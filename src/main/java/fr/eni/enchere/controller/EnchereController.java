package fr.eni.enchere.controller;

import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bo.Auctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
@Controller
@RequestMapping("sell/detail")
public class AuctionsController {

    @Autowired
    private AuctionsService auctionsService;

    public AuctionsController(AuctionsService auctionsService) {
        this.auctionsService = auctionsService;
    }

    @GetMapping
    public String bid(Model model) {
        System.out.println("help");

        model.addAttribute("auctions", new Auctions());
        return"";
    }

}*/