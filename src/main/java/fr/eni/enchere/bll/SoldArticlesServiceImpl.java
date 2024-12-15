package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.PickUpDAO;
import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.UserDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SoldArticlesServiceImpl implements SoldArticlesService {

    private final SoldArticlesDAO soldArticlesDAO;
    private final PickUpDAO pickUpDAO;
    private final CategoryDAO categoryDAO;
    private final UserDAO userDAO;

    public SoldArticlesServiceImpl(SoldArticlesDAO soldArticlesDAO, PickUpDAO pickUpDAO, CategoryDAO categoryDAO, UserDAO userDAO) {
        this.soldArticlesDAO = soldArticlesDAO;
        this.pickUpDAO = pickUpDAO;
        this.categoryDAO = categoryDAO;
        this.userDAO = userDAO;
    }

    @Override
    public SoldArticles findById(int id) {
        SoldArticles a = soldArticlesDAO.findByNum(id);
        return a;
    }

    @Override
    public List<SoldArticleViewModel> findAll() {
        List<SoldArticles> all = soldArticlesDAO.findAll();
        return mapSoldArticlesToViewModel(all);
    }

    @Override
    public List<SoldArticleViewModel> findByIdCategory(int idCategory) {
        List<SoldArticles> articlesFound = soldArticlesDAO.findByCategory(idCategory);
        return mapSoldArticlesToViewModel(articlesFound);
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
        return mapSoldArticlesToViewModel(articlesFound);
    }

    private List<SoldArticleViewModel> mapSoldArticlesToViewModel(List<SoldArticles> articlesFound) {
        return articlesFound.stream().map(soldArticles -> {
            SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
            soldArticleViewModel.setSoldArticles(soldArticles);
            soldArticleViewModel.setSeller(userDAO.findByNum(soldArticles.getIdUser()));
            soldArticleViewModel.setCategory(categoryDAO.findByNum(soldArticles.getIdCategory()));
            return soldArticleViewModel;
        }).toList();
    }

}
