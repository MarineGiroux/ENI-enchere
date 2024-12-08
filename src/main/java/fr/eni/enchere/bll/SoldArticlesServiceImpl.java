package fr.eni.enchere.bll;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
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

    public SoldArticlesServiceImpl(SoldArticlesDAO soldArticlesDAO, PickUpDAO pickUpDAO) {
        this.soldArticlesDAO = soldArticlesDAO;
        this.pickUpDAO = pickUpDAO;
    }

    @Override
    public SoldArticles findById(int id) {
        SoldArticles a = soldArticlesDAO.findByNum(id);
        return a;
    }

    @Override
    public List<SoldArticles> findAll() {
        return soldArticlesDAO.findAll();
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

}
