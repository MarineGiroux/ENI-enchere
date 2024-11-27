package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;

public interface SoldArticlesService {
	
	SoldArticles FindById (int id);
	
	List<SoldArticles> findAll();
	
	void add(SoldArticles soldArticles, User user) throws BusinessException;

}
