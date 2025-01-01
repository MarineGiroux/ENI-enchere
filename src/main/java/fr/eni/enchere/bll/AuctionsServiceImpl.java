package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.AuctionsDAO;
import fr.eni.enchere.dal.UserDAO;

@Service
public class AuctionsServiceImpl implements AuctionsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuctionsServiceImpl.class);

    private AuctionsDAO auctionsDAO;
    private SoldArticlesDAO soldArticlesDAO;
    private UserDAO userDAO;

    public AuctionsServiceImpl(AuctionsDAO auctionsDAO, SoldArticlesDAO soldArticlesDAO, UserDAO userDAO,
                               CategoryDAO categoryDAO) {
        super();
        this.auctionsDAO = auctionsDAO;
        this.soldArticlesDAO = soldArticlesDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void bid(Auctions auctions) {
        // verif argent suffisant
        if (auctions.getAmountAuctions() <= auctions.getUser().getCredit()) {
            // verif date auctions
            SoldArticles soldArticles = soldArticlesDAO.findByNum(auctions.getSoldArticles().getIdArticle());
            if ((auctions.getDateAuctions().equals(soldArticles.getStartDateAuctions()) || auctions.getDateAuctions().isAfter(soldArticles.getStartDateAuctions()))
                    && (auctions.getDateAuctions().equals(soldArticles.getEndDateAuctions()) || auctions.getDateAuctions().isBefore(soldArticles.getEndDateAuctions()))) {
                // trouver la plus grosse auctions
                final Auctions biggerAuction = auctionsDAO.findBiggerAuction(auctions.getSoldArticles().getIdArticle());
                if (biggerAuction != null && auctions.getAmountAuctions() > biggerAuction.getAmountAuctions()) {
                    // verif auctions suffisante
                    // debiter credit
                    userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit() - auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
                    // rembourser auctions precedente
                    userDAO.updateCredit(userDAO.findByNum(biggerAuction.getIdUser()).getCredit() + biggerAuction.getAmountAuctions(), userDAO.findByNum(biggerAuction.getIdUser()));
                    // verif si l'user a deja une auctions pour cet article
                    if (auctionsDAO.countAuctionsUser(auctions.getSoldArticles().getIdArticle(), auctions.getUser().getIdUser()) > 0) {
                        // modifier l'enchère
                        auctionsDAO.outbid(auctions);
                    } else {
                        // creer l'auctions
                        auctionsDAO.create(auctions);
                    }
                    soldArticlesDAO.updatePriceSale(auctions);
                } else if (biggerAuction == null) {
                    // verif que montant >= miseAPrix
                    if (auctions.getAmountAuctions() >= soldArticles.getInitialPrice()) {
                        // premiere auctions
                        // debit de credit
                        userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit() - auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
                        // creation de l'auctions
                        auctionsDAO.create(auctions);
                        soldArticlesDAO.updatePriceSale(auctions);
                    } else {
                        LOGGER.info("Montant inferieur a la mise a prix");
                        // TODO Lever une BusinessException
                    }
                } else {
                    LOGGER.info("Montant de l'enchère inferieure ou égale aux autres auctions");
                    // TODO Lever une BusinessException
                }
            } else {
                LOGGER.error("Enchère déjà fermée");
                // TODO Lever une BusinessException
            }
        } else {
            LOGGER.info("Crédits insuffisants");
            // TODO Lever une BusinessException
        }

    }

    @Override
    public List<Auctions> recoverAuctions() {
        List<Auctions> auctionList = auctionsDAO.findAll();

        for (Auctions auctions : auctionList) {
            SoldArticles soldArticles = soldArticlesDAO.findByNum(auctions.getSoldArticles().getIdArticle());
            auctions.setSoldArticles(soldArticles);
            auctions.setUser(userDAO.findByNum(auctions.getUser().getIdUser()));
        }
        return auctionList;
    }

    @Override
    public List<Auctions> findByID(int idAuctions) {
        return auctionsDAO.findByArticle(idAuctions);
    }

    @Override
    public int amountAuction(int idArticle) {
        // TODO Auto-generated method stub
        return 0;
    }

}