package fr.eni.enchere.bo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
	
	private User Buy;
	private User Sell;
	private Category cartegoryArticle;
	private PickUp pickUpLocation;
	
	public SoldArticles() {
		pickUpLocation = new PickUp();
		Sell = new User();
		Buy = new User();
		cartegoryArticle = new Category();
	}

	public SoldArticles(int idArticle, String nameArticle, String description, LocalDate startDateAuctions, LocalDate endDateAuctions, int initialPrice, int priceSale, String picture, boolean saleStatus, User buy, User sell, Category cartegoryArticle, PickUp pickUpLocation) {
		this.idArticle = idArticle;
		this.nameArticle = nameArticle;
		this.description = description;
		this.startDateAuctions = startDateAuctions;
		this.endDateAuctions = endDateAuctions;
		this.initialPrice = initialPrice;
		this.priceSale = priceSale;
		this.picture = picture;
		this.saleStatus = saleStatus;
		Buy = buy;
		Sell = sell;
		this.cartegoryArticle = cartegoryArticle;
		this.pickUpLocation = pickUpLocation;
	}

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

	public User getBuy() {
		return Buy;
	}

	public void setBuy(User buy) {
		Buy = buy;
	}

	public User getSell() {
		return Sell;
	}

	public void setSell(User sell) {
		Sell = sell;
	}

	public Category getCartegoryArticle() {
		return cartegoryArticle;
	}

	public void setCartegoryArticle(Category cartegoryArticle) {
		this.cartegoryArticle = cartegoryArticle;
	}

	public PickUp getPickUpLocation() {
		return pickUpLocation;
	}

	public void setPickUpLocation(PickUp pickUpLocation) {
		this.pickUpLocation = pickUpLocation;
	}

	@Override
	public String toString() {
		return "ItemSold{" +
				"idArticle=" + idArticle +
				", name='" + nameArticle + '\'' +
				", description='" + description + '\'' +
				", startDateAuctions=" + startDateAuctions +
				", endDateAuctions=" + endDateAuctions +
				", initialPrice=" + initialPrice +
				", priceSale=" + priceSale +
				", picture='" + picture + '\'' +
				", saleStatus=" + saleStatus +
				", Buy=" + Buy +
				", Sell=" + Sell +
				", cartegoryArticle=" + cartegoryArticle +
				", pickUpLocation=" + pickUpLocation +
				'}';
	}
}
