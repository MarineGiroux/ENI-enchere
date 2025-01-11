package fr.eni.enchere.bll;

import fr.eni.enchere.AuctionsApplicationIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.mockito.Mockito.when;

class AuctionsServiceIT extends AuctionsApplicationIT {

    @Autowired
    AuctionsService auctionsService;
    @MockBean
    Clock clock;

    @Test
    void test_closeOutdatedAuctions() {
        // Given
        when(clock.instant()).thenReturn(Instant.parse("2025-01-01T10:00:00.00Z"));
        when(clock.getZone()).thenReturn(ZoneId.of("Europe/Paris"));
        String userEmail = "toto@titi.fr";

        // When
        auctionsService.closeOutdatedAuctions(userEmail);

        // Then

    }
}
