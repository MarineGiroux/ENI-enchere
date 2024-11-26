package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Enchere;

public interface ArticleVenduDAO {
	
	List<ArticleVendu>  findByCategorie (int noCategorie);
	
	ArticleVendu  findByNum (int noArticle);
	
	void create (ArticleVendu articleVendu);

	List<ArticleVendu> FindAll();
	
	void updatePrixVente(Enchere enchere);
}
