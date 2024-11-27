package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.Auctions;

public interface SoldArticlesDAO {
	
	List<SoldArticles> findByCategory(int idCategory);
	
	SoldArticles findByNum (int idArticle);
	
	void create (SoldArticles soldArticles);

	List<SoldArticles> FindAll();
	
	void updatePriceSale(Auctions auctions);
}
