package fr.eni.enchere.controller;

import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;


@Controller
public class AuctionsController {

    private final AuctionsService auctionsService;
    private final UserService userService;

    @Autowired
    public AuctionsController(AuctionsService auctionsService, UserService userService) {
        this.auctionsService = auctionsService;
        this.userService = userService;
    }

    @PostMapping("/detail?id=")
    public String placeBid(@ModelAttribute("soldArticleViewModel") SoldArticleViewModel viewModel,
                           @RequestParam("amountAuctions") int amount,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            Auctions auction = new Auctions();
            auction.setDateAuctions(LocalDate.now());
            auction.setAmountAuctions(amount);

            User currentUser = userService.findByEmail(authentication.getName());
            auction.setUser(currentUser);

            SoldArticles article = new SoldArticles();
            article.setIdArticle(viewModel.getSoldArticles().getIdArticle());
            auction.setSoldArticles(article);

            auctionsService.bid(auction);

            redirectAttributes.addFlashAttribute("success", "Votre enchère a été placée avec succès!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enchère: " + e.getMessage());
        }

        return "redirect:/sales/detail?id=" + viewModel.getSoldArticles().getIdArticle();
    }
}
