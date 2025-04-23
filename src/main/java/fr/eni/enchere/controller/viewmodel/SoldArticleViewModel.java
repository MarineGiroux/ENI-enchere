package fr.eni.enchere.controller.viewmodel;

import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.PickUp;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import jakarta.validation.Valid;

public class SoldArticleViewModel {
    @Valid
    SoldArticles soldArticles;
    Category category;
    PickUp pickUpLocation;
    User seller;
    User buyer;

    public SoldArticles getSoldArticles() {
        return soldArticles;
    }
    public void setSoldArticles(SoldArticles soldArticles) {
        this.soldArticles = soldArticles;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public PickUp getPickUpLocation() {
        return pickUpLocation;
    }
    public void setPickUpLocation(PickUp pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
    public User getSeller() {
        return seller;
    }
    public void setSeller(User seller) {
        this.seller = seller;
    }
    public User getBuyer() {
        return buyer;
    }
    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    @Override
    public String toString() {
        return "SoldArticleViewModel{" +
                "soldArticles=" + soldArticles +
                ", category=" + category +
                ", pickUp=" + pickUpLocation +
                ", seller=" + seller +
                ", buyer=" + buyer +
                '}';
    }
}
