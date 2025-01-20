package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Auctions;

public interface AuctionsDAO {


	void create (Auctions auctions);

	List<Auctions> findAll();

	int countAuctionsUser(int idArticle, int idUser);

	void outbid(Auctions auctions);

	Auctions findBiggerAuction(int idArticle);

}