package fr.eni.enchere.configuration.security;

import fr.eni.enchere.bll.SoldArticlesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiredArticlesCleaner {
    private final SoldArticlesService soldArticlesService;

    public ExpiredArticlesCleaner(SoldArticlesService soldArticlesService) {
        this.soldArticlesService = soldArticlesService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Exécute tous les jours à minuit
    public void cleanExpiredArticles() {
        soldArticlesService.deleteExpiredArticles();
    }
}
