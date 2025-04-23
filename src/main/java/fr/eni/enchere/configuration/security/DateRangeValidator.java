package fr.eni.enchere.configuration.security;

import fr.eni.enchere.bo.SoldArticles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<DateRange, SoldArticles> {
    @Override
    public void initialize(DateRange constraintAnnotation) {
    }

    @Override
    public boolean isValid(SoldArticles article, ConstraintValidatorContext context) {
        if (article.getStartDateAuctions() == null || article.getEndDateAuctions() == null) {
            return true;
        }
        return article.getEndDateAuctions().isAfter(article.getStartDateAuctions());
    }
}
