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

        if (isSeller(auctions, soldArticles)) {
            throw new BusinessException("Tu ne peux pas enchérir sur ton propre article");
        }

        final Auctions biggerAuction = auctionsDAO.findBiggerAuction(soldArticles.getIdArticle());

        if (biggerAuction == null) {
            handleNoPreviousAuction(auctions, soldArticles);
        } else {
            handleExistingAuction(auctions, biggerAuction);
        }
    }

    private static boolean isSeller(Auctions auctions, SoldArticles soldArticles) {
        return auctions.getUser().getIdUser() == soldArticles.getIdUser();
    }

    private void handleNoPreviousAuction(Auctions auctions, SoldArticles soldArticles) throws BusinessException {
        if (auctions.getAmountAuctions() >= soldArticles.getInitialPrice()) {
            debitUserCredit(auctions);
            auctionsDAO.create(auctions);
            soldArticlesDAO.updatePriceSale(auctions);
        } else {
            throw new BusinessException("Le montant doit être supérieur à la mise à prix");
        }
    }

    private void handleExistingAuction(Auctions auctions, Auctions biggerAuction) throws BusinessException {
        if (auctions.getUser().getIdUser() == biggerAuction.getIdUser()) {
            throw new BusinessException("Tu ne peux pas surenchérir sur ta propre enchère");
        }

        if (auctions.getAmountAuctions() > biggerAuction.getAmountAuctions()) {
            processHigherBid(auctions, biggerAuction);
        } else {
            throw new BusinessException("Le montant doit être supérieur à la dernière enchère");
        }
    }

    private void processHigherBid(Auctions auctions, Auctions biggerAuction) {
        debitUserCredit(auctions);
        refundPreviousBidder(biggerAuction);

        if (auctionsDAO.countAuctionsUser(auctions.getSoldArticles().getIdArticle(), auctions.getUser().getIdUser()) > 0) {
            auctionsDAO.outbid(auctions);
        } else {
            auctionsDAO.create(auctions);
        }

        soldArticlesDAO.updatePriceSale(auctions);
    }

    private void debitUserCredit(Auctions auctions) {
        int userId = auctions.getUser().getIdUser();
        int newCredit = userDAO.findByNum(userId).getCredit() - auctions.getAmountAuctions();
        userDAO.updateCredit(newCredit, userDAO.findByNum(userId));
    }

    private void refundPreviousBidder(Auctions biggerAuction) {
        int previousUserId = biggerAuction.getIdUser();
        int newCredit = userDAO.findByNum(previousUserId).getCredit() + biggerAuction.getAmountAuctions();
        userDAO.updateCredit(newCredit, userDAO.findByNum(previousUserId));
    }

    protected static void checkAuction(Auctions auctions, SoldArticles soldArticles) throws BusinessException {
        if (auctions.getAmountAuctions() > auctions.getUser().getCredit()) {
            throw new BusinessException("Tu n'as pas assez de crédit");
        }

        if (auctions.getDateAuctions().isBefore(soldArticles.getStartDateAuctions()) ||
                auctions.getDateAuctions().isAfter(soldArticles.getEndDateAuctions())) {
            throw new BusinessException("L'enchère est déjà terminée");
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

    @Transactional
    @Override
    public void closeOutdatedAuctions(String userEmail) {
        User user = userDAO.findByEmail(userEmail);
        int creditToAdd = soldArticlesDAO.closeOutdatedAuctionsAndGetCreditAmount(user.getIdUser());
        if (creditToAdd > 0) {
            LOGGER.info("Augmentation de {} crédits pour le user {}", creditToAdd, userEmail);
            userDAO.riseCredits(creditToAdd, user);
        }
    }

}