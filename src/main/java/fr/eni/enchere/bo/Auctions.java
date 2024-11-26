package fr.eni.enchere.bo;

import java.time.LocalDate;

public class Auctions {
	
	private LocalDate dateAuctions;
	private int amountAuctions;
	
	private User user;
	private ItemSold itemSold;
	
	public Auctions() {
	}

	public Auctions(LocalDate dateAuctions, int amountAuctions, User user, ItemSold itemSold) {
		this.dateAuctions = dateAuctions;
		this.amountAuctions = amountAuctions;
		this.user = user;
		this.itemSold = itemSold;
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

	public ItemSold getItemSold() {
		return itemSold;
	}

	public void setItemSold(ItemSold itemSold) {
		this.itemSold = itemSold;
	}

	@Override
	public String toString() {
		return "Auctions{" +
				"dateAuctions=" + dateAuctions +
				", amountAuctions=" + amountAuctions +
				", user=" + user +
				", itemSold=" + itemSold +
				'}';
	}
}
