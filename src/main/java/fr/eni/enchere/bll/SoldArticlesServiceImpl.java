package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.PickUpDAO;
import fr.eni.enchere.dal.SoldArticlesDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SoldArticlesServiceImpl implements SoldArticlesService {

    private final SoldArticlesDAO soldArticlesDAO;
    private final PickUpDAO pickUpDAO;
    private final CategoryDAO categoryDAO;

    public SoldArticlesServiceImpl(SoldArticlesDAO soldArticlesDAO, PickUpDAO pickUpDAO, CategoryDAO categoryDAO) {
        this.soldArticlesDAO = soldArticlesDAO;
        this.pickUpDAO = pickUpDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public SoldArticles findById(int id) {
        SoldArticles a = soldArticlesDAO.findByNum(id);
        return a;
    }

    @Override
    public List<SoldArticleViewModel> findAll() {
        List<SoldArticles> all = soldArticlesDAO.findAll();
        return all.stream().map(soldArticles -> {
            SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
            soldArticleViewModel.setSoldArticles(soldArticles);

            Category soldArticleCategory = categoryDAO.findByNum(soldArticles.getIdCategory());
            soldArticleViewModel.setCategory(soldArticleCategory);
            return soldArticleViewModel;
        }).toList();
    }

    @Override
    public List<SoldArticleViewModel> findByIdCategory(int idCategory) {
        Category category = categoryDAO.findByNum(idCategory);
        List<SoldArticles> articlesFound = soldArticlesDAO.findByCategory(idCategory);
        return articlesFound.stream().map(soldArticles -> {
            SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
            soldArticleViewModel.setSoldArticles(soldArticles);
            soldArticleViewModel.setCategory(category);
            return soldArticleViewModel;
        }).toList();
    }

    @Override
    @Transactional
    public SoldArticles add(SoldArticleViewModel soldArticleViewModel) {
        // Create article to sold
        SoldArticles soldArticles = soldArticlesDAO.create(soldArticleViewModel.getSoldArticles());

        // Assign pickup location to the article
        soldArticleViewModel.getPickUpLocation().setIdArticle(soldArticles.getIdArticle());
        this.pickUpDAO.create(soldArticleViewModel.getPickUpLocation());

        return soldArticles;
    }

    @Override
    public List<SoldArticleViewModel> searchByName(String searchArticleName) {
        List<SoldArticles> articlesFound = soldArticlesDAO.searchByName(searchArticleName);
        return articlesFound.stream().map(soldArticles -> {
            SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
            soldArticleViewModel.setSoldArticles(soldArticles);

            Category soldArticleCategory = categoryDAO.findByNum(soldArticles.getIdCategory());
            soldArticleViewModel.setCategory(soldArticleCategory);
            return soldArticleViewModel;
        }).toList();
    }

}
