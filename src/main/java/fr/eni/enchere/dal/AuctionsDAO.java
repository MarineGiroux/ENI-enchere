package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Auctions;

public interface EnchereDAO {
	
	
	void create (Auctions auctions);

	Auctions findBynoUtilisateur(int noArticle, int noUtilisateur);

	List<Auctions> findAll();
	
	int countEnchereUtilisateur (int noArticle,int noUtilisateur);
	
	int countEnchere (int noArticle);
	
	void surencherir (Auctions auctions);
	
	List<Auctions> findByArticle(int noArticle);

}
