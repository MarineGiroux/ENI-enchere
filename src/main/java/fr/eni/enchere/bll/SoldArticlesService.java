package fr.eni.enchere.bll;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.exception.BusinessException;

import java.util.List;

public interface SoldArticlesService {
	
	SoldArticles findById(int id);
	
	List<SoldArticles> findAll();

	SoldArticles add(SoldArticleViewModel soldArticleViewModel) throws BusinessException;

}
