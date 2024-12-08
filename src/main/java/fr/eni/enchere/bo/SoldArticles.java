package fr.eni.enchere.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SoldArticles {

    private int idArticle;
    @NotBlank
    private String nameArticle;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate startDateAuctions;
    @NotNull
    private LocalDate endDateAuctions;
    @NotNull
    private int initialPrice;
    private int priceSale;
    private String picture;
    private boolean saleStatus;

    private int idUser;
    private int idCategory;

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public @NotBlank String getNameArticle() {
        return nameArticle;
    }

    public void setNameArticle(@NotBlank String nameArticle) {
        this.nameArticle = nameArticle;
    }

    public @NotBlank String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank String description) {
        this.description = description;
    }

    public @NotNull LocalDate getStartDateAuctions() {
        return startDateAuctions;
    }

    public void setStartDateAuctions(@NotNull LocalDate startDateAuctions) {
        this.startDateAuctions = startDateAuctions;
    }

    public @NotNull LocalDate getEndDateAuctions() {
        return endDateAuctions;
    }

    public void setEndDateAuctions(@NotNull LocalDate endDateAuctions) {
        this.endDateAuctions = endDateAuctions;
    }

    @NotNull
    public int getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(@NotNull int initialPrice) {
        this.initialPrice = initialPrice;
    }

    public int getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(int priceSale) {
        this.priceSale = priceSale;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(boolean saleStatus) {
        this.saleStatus = saleStatus;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "SoldArticles{" +
                "idArticle=" + idArticle +
                ", nameArticle='" + nameArticle + '\'' +
                ", description='" + description + '\'' +
                ", startDateAuctions=" + startDateAuctions +
                ", endDateAuctions=" + endDateAuctions +
                ", initialPrice=" + initialPrice +
                ", priceSale=" + priceSale +
                ", picture='" + picture + '\'' +
                ", saleStatus=" + saleStatus +
                ", idUser='" + idUser + '\'' +
                ", idCategory=" + idCategory +
                '}';
    }
}
