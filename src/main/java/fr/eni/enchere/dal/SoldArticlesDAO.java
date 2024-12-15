package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.Auctions;

public interface SoldArticlesDAO {
	
	List<SoldArticles>findByCategory(int idCategory);
	
	SoldArticles findByNum(int idArticle);

	SoldArticles create(SoldArticles soldArticles);

	List<SoldArticles> findAll();
	
	void updatePriceSale(Auctions auctions);

	List<SoldArticles> searchByName(String searchArticleName);
}
