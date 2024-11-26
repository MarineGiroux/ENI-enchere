package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;


public interface AuctionsService {

	void bid(Auctions auctions);
	
	List<Auctions> recoverAuctions();
	
	List<Auctions> findByID(int idAuctions);
	
	int amountAuction(int idArticle);

}
