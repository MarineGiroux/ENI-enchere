package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.dal.AuctionsDAO;
import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.UserDAO;
import fr.eni.enchere.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuctionsServiceImpl implements AuctionsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuctionsServiceImpl.class);

    private AuctionsDAO auctionsDAO;
    private SoldArticlesDAO soldArticlesDAO;
    private UserDAO userDAO;

    public AuctionsServiceImpl(AuctionsDAO auctionsDAO, SoldArticlesDAO soldArticlesDAO, UserDAO userDAO) {
        super();
        this.auctionsDAO = auctionsDAO;
        this.soldArticlesDAO = soldArticlesDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void bid(Auctions auctions) throws BusinessException {
        final SoldArticles soldArticles = auctions.getSoldArticles();
        checkAuction(auctions, soldArticles);

        // Vérifier que l'enchérisseur n'est pas le vendeur
        if (auctions.getUser().getIdUser() != soldArticles.getIdUser()) {
            // trouver la plus grosse enchère
            final Auctions biggerAuction = auctionsDAO.findBiggerAuction(auctions.getSoldArticles().getIdArticle());
            // aucune enchère trouvée
            if (biggerAuction == null) {
                // verif que montant >= miseAPrix
                if (auctions.getAmountAuctions() >= soldArticles.getInitialPrice()) {
                    // debit de credit
                    userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit() - auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
                    // creation de l'enchère
                    auctionsDAO.create(auctions);
                    soldArticlesDAO.updatePriceSale(auctions);
                } else {
                    LOGGER.info("Montant inferieur a la mise a prix");
                    throw new BusinessException("Le montant doit être supérieur à la mise à prix");
                }
            } else if (auctions.getUser().getIdUser() != biggerAuction.getIdUser()) {
                // autre enchérisseur que celui de la plus grosse enchère
                if (auctions.getAmountAuctions() > biggerAuction.getAmountAuctions()) {
                    // debiter credit
                    userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit() - auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
                    // rembourser dernier enchérisseur
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
                } else {
                    LOGGER.info("Montant de l'enchère inferieure ou égale aux autres auctions");
                    throw new BusinessException("Le montant doit être supérieur à la dernière enchère");
                }
            } else {
                LOGGER.error("L'utilisateur ne peut pas surenchérir sur sa propre enchère");
                throw new BusinessException("Tu ne peux pas surenchérir sur ta propre enchère");
            }
        } else {
            LOGGER.error("L'utilisateur ne peut pas enchérir sur son propre article");
            throw new BusinessException("Tu ne peux pas enchérir sur ton propre article");
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

    protected static void checkAuction(Auctions auctions, SoldArticles soldArticles) throws BusinessException {
        if (auctions.getAmountAuctions() > auctions.getUser().getCredit()) {
            // pas assez d'argent
            throw new BusinessException("Tu n'as pas assez de crédit");
        } else if (auctions.getDateAuctions().isBefore(soldArticles.getStartDateAuctions())
                || auctions.getDateAuctions().isAfter(soldArticles.getEndDateAuctions())) {
            // enchère fermée
            throw new BusinessException("L'enchère est déjà terminée");
        }
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

    @Transactional
    @Override
    public void closeOutdatedAuctions(String userEmail) {
        User user = userDAO.findByEmail(userEmail);
        int creditToAdd = soldArticlesDAO.closeOutdatedAuctionsAndGetCreditAmount(user.getIdUser());
        if (creditToAdd > 0) {
            LOGGER.info("Augmentation de {} crédits pour le user {}", creditToAdd, userEmail);
            userDAO.updateCredit(creditToAdd, user);
        }
    }

}