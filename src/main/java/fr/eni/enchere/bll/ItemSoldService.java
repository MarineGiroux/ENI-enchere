package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.exception.BusinessException;

public interface ArticleVenduService {
	
	ArticleVendu FindById (int id);
	
	List<ArticleVendu> FindAll();
	
	void add(ArticleVendu articleVendu, Utilisateur utilisateur) throws BusinessException;

}
