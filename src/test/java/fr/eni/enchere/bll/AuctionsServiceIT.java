package fr.eni.enchere.bll;

import fr.eni.enchere.AuctionsApplicationIT;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class AuctionsServiceIT extends AuctionsApplicationIT {

    @Autowired
    AuctionsService auctionsService;

    @Autowired
    SoldArticlesDAO soldArticlesDAO;

    @Autowired
    UserDAO userDAO;

    @MockBean
    Clock clock;

    private final static String SELLER_EMAIL = "john.doe@email.com";

    @Test
    @DisplayName("No auctions to close : the credits has not changed")
    @Sql("/sql/auctions/closeOutdatedAuctions_noAuctionsToClose.sql")
    void test_closeOutdatedAuctions_noAuctionsToClose() {
        // Given
        when(clock.instant()).thenReturn(Instant.parse("2025-01-01T10:00:00.00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("Europe/Paris"));

        // When
        auctionsService.closeOutdatedAuctions(SELLER_EMAIL);

        // Then
        assertThat(userDAO.findByEmail(SELLER_EMAIL).getCredit()).isEqualTo(100);
    }

    @Test
    @DisplayName("One auction to close : the auction is closed and the credits have been raised")
    @Sql("/sql/auctions/closeOutdatedAuctions_oneAuctionToClose.sql")
    void test_closeOutdatedAuctions_oneAuctionToClose() {
        // Given
        when(clock.instant()).thenReturn(Instant.parse("2025-01-01T10:00:00.00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("Europe/Paris"));

        // When
        auctionsService.closeOutdatedAuctions(SELLER_EMAIL);

        // Then
        final SoldArticles soldArticles = soldArticlesDAO.searchByName("Souris").getFirst();
        assertThat(soldArticles.isSaleStatus()).isTrue();
        assertThat(userDAO.findByEmail(SELLER_EMAIL).getCredit()).isEqualTo(200);
    }
}
