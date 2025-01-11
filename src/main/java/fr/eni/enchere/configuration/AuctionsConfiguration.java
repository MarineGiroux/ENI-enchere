package fr.eni.enchere.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class AuctionsConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
