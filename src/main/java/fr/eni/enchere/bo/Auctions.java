package fr.eni.enchere.bo;

import java.time.LocalDate;

public class Enchere {
	
	private LocalDate dateEnchere;
	private int montantEnchere;
	
	private User user;
	private ItemSold itemSold;
	
	public Enchere() {
	}
	
	public Enchere(LocalDate dateEnchere, int montantEnchere, User user, ItemSold itemSold) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.user = user;
		this.itemSold = itemSold;
	}
	
	public LocalDate getDateEnchere() {
		return dateEnchere;
	}
	public void setDateEnchere(LocalDate dateEnchere) {
		this.dateEnchere = dateEnchere;
	}
	public int getMontantEnchere() {
		return montantEnchere;
	}
	public void setMontantEnchere(int montantEnchere) {
		this.montantEnchere = montantEnchere;
	}
	public User getUtilisateur() {
		return user;
	}
	public void setUtilisateur(User user) {
		this.user = user;
	}
	public ItemSold getItemSold() {
		return itemSold;
	}
	public void setArticleVendu(ItemSold itemSold) {
		this.itemSold = itemSold;
	}

	@Override
	public String toString() {
		return String.format("Enchere [dateEnchere=%s, montantEnchere=%s, utilisateur=%s, articleVendu=%s]",
				dateEnchere, montantEnchere, user, itemSold);
	}

	
	
}
