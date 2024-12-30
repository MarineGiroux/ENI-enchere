package fr.eni.enchere.bll;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

import java.util.List;

public interface SoldArticlesService {
	
	SoldArticleViewModel findById(int id);

	List<SoldArticleViewModel> findAll();

	List<SoldArticleViewModel> findByIdCategory(int idCategory);

	SoldArticles add(SoldArticleViewModel soldArticleViewModel) throws BusinessException;

	List<SoldArticleViewModel> searchByName(String searchArticleName);

	void update(@Valid SoldArticles soldArticleViewModel) throws BusinessException;

	void deleteArticleById(String idArticle);

}
