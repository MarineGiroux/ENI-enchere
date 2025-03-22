package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AuctionsServiceImplTest {

    // TEST pas assez d'argent
    @Test
    void test_checkAuction_not_enough_credit() {
        // Given
        final Auctions auctions = new Auctions();
        auctions.setAmountAuctions(10);
        final User user = new User();
        user.setCredit(5);
        auctions.setUser(user);

        // When / Then
        Assertions.assertThrows(BusinessException.class,
                () -> AuctionsServiceImpl.checkAuction(auctions, null));
    }

    // TEST enchère fermée
    @Test
    void test_checkAuction_auction_not_yed_opened() {
        // Given
        final Auctions auctions = new Auctions();
        auctions.setAmountAuctions(10);
        auctions.setDateAuctions(LocalDate.of(2024, 12, 31));
        final User user = new User();
        user.setCredit(15);
        auctions.setUser(user);
        final SoldArticles soldArticles = new SoldArticles();
        soldArticles.setStartDateAuctions(LocalDate.of(2025, 1, 1));
        soldArticles.setEndDateAuctions(LocalDate.of(2025, 1, 1));

        // When / Then
        Assertions.assertThrows(BusinessException.class,
                () -> AuctionsServiceImpl.checkAuction(auctions, soldArticles));
    }

    // TEST enchère fermée
    @Test
    void test_checkAuction_auction_closed() {
        // Given
        final Auctions auctions = new Auctions();
        auctions.setAmountAuctions(10);
        auctions.setDateAuctions(LocalDate.of(2025, 1, 2));
        final User user = new User();
        user.setCredit(15);
        auctions.setUser(user);
        final SoldArticles soldArticles = new SoldArticles();
        soldArticles.setStartDateAuctions(LocalDate.of(2025, 1, 1));
        soldArticles.setEndDateAuctions(LocalDate.of(2025, 1, 1));

        // When / Then
        Assertions.assertThrows(BusinessException.class,
                () -> AuctionsServiceImpl.checkAuction(auctions, soldArticles));
    }

    // TEST checkAuction valide (assez d'argent et ouverte)
    @Test
    void test_checkAuction_ok() {
        // Given
        final Auctions auctions = new Auctions();
        auctions.setAmountAuctions(10);
        auctions.setDateAuctions(LocalDate.of(2025, 1, 2));
        final User user = new User();
        user.setCredit(15);
        auctions.setUser(user);
        final SoldArticles soldArticles = new SoldArticles();
        soldArticles.setStartDateAuctions(LocalDate.of(2025, 1, 2));
        soldArticles.setEndDateAuctions(LocalDate.of(2025, 1, 2));

        // When / Then
        Assertions.assertDoesNotThrow(() -> AuctionsServiceImpl.checkAuction(auctions, soldArticles));
    }
}
