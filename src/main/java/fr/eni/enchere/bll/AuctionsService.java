package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;


public interface EnchereService {

	void encherir(Auctions auctions);
	
	List<Auctions> recupererEncheres();
	
	List<Auctions> findByID(int idEnchere);
	
	int montantEnchere (int noArticle);

}
