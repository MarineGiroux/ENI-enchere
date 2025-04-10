package fr.eni.enchere.bo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class Auctions {

	@NotNull
	private int idUser;
	@NotNull
	private int idArticle;
	@NotNull
	private LocalDate dateAuctions;
	@NotNull
	@Positive
	private int amountAuctions;

	private User user;
	private SoldArticles soldArticles;

	public Auctions() {
	}

	public Auctions(int idUser, int idArticle, LocalDate dateAuctions, int amountAuctions, User user, SoldArticles soldArticles) {
		this.idUser = idUser;
		this.idArticle = idArticle;
		this.dateAuctions = dateAuctions;
		this.amountAuctions = amountAuctions;
		this.user = user;
		this.soldArticles = soldArticles;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdArticle() {
		return idArticle;
	}

	public void setIdArticle(int idArticle) {
		this.idArticle = idArticle;
	}

	public LocalDate getDateAuctions() {
		return dateAuctions;
	}

	public void setDateAuctions(LocalDate dateAuctions) {
		this.dateAuctions = dateAuctions;
	}

	public int getAmountAuctions() {
		return amountAuctions;
	}

	public void setAmountAuctions(int amountAuctions) {
		this.amountAuctions = amountAuctions;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public SoldArticles getSoldArticles() {
		return soldArticles;
	}

	public void setSoldArticles(SoldArticles soldArticles) {
		this.soldArticles = soldArticles;
	}

	@Override
	public String toString() {
		return "Auctions{" +
				"idUser=" + idUser +
				", idArticle=" + idArticle +
				", dateAuctions=" + dateAuctions +
				", amountAuctions=" + amountAuctions +
				", user=" + user +
				", soldArticles=" + soldArticles +
				'}';
	}
}
