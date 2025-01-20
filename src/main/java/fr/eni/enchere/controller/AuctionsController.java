package fr.eni.enchere.controller;

import fr.eni.enchere.bll.AuctionsService;
import fr.eni.enchere.bll.SoldArticlesServiceImpl;
import fr.eni.enchere.bll.UserService;
import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.Clock;
import java.time.LocalDate;


@Controller
@RequestMapping("/sales")
public class AuctionsController {
    private final static Logger LOGGER = LoggerFactory.getLogger(SalesController.class);

    private final AuctionsService auctionsService;
    private final UserService userService;
    private final Clock clock;
    private final SoldArticlesServiceImpl soldArticlesServiceImpl;

    @Autowired
    public AuctionsController(AuctionsService auctionsService, UserService userService, Clock clock, SoldArticlesServiceImpl soldArticlesServiceImpl) {
        this.auctionsService = auctionsService;
        this.userService = userService;
        this.clock = clock;
        this.soldArticlesServiceImpl = soldArticlesServiceImpl;
    }

    @PostMapping("/auctions")
    public String registerAuction(@RequestParam("soldArticles.idArticle") int idArticle,
                                  @RequestParam("amountAuctions") int amountAuction,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            Auctions auctions = new Auctions();
            auctions.setUser(userService.findByEmail(principal.getName()));
            auctions.setSoldArticles(soldArticlesServiceImpl.findById(idArticle).getSoldArticles());
            auctions.setDateAuctions(LocalDate.now(clock));
            auctions.setAmountAuctions(amountAuction);

            this.auctionsService.bid(auctions);
            redirectAttributes.addFlashAttribute("success", "Enchère placée avec succès");

        } catch (BusinessException be) {
            redirectAttributes.addFlashAttribute("error", be.getMessage());
            LOGGER.error("Erreur lors de l'enchère: {}", be.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur inattendue s'est produite");
            LOGGER.error("Erreur inattendue: {}", e.getMessage());
        }

        return "redirect:/sales/detail?id=" + idArticle;
    }
}
