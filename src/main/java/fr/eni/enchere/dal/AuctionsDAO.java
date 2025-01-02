package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Auctions;

public interface AuctionsDAO {


	void create (Auctions auctions);

	Auctions findByidUser(int idArticle, int idUser);

	List<Auctions> findAll();

	int countAuctionsUser(int idArticle, int idUser);

	void outbid(Auctions auctions);

	List<Auctions> findByArticle(int idArticle);

	Auctions findBiggerAuction(int idArticle);

}