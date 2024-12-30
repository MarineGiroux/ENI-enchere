package fr.eni.enchere.bll;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.controller.viewmodel.SoldArticleViewModel;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.PickUpDAO;
import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.UserDAO;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    public SoldArticleViewModel findById(int id) {
        return this.mapSoldArticlesToViewModel(soldArticlesDAO.findByNum(id));
    }

    @Override
    public List<SoldArticleViewModel> findAll() {
        List<SoldArticles> all = soldArticlesDAO.findAll();
        LocalDate today = LocalDate.now();
        List<SoldArticles> activeArticles = all.stream()
                .filter(article -> !article.getEndDateAuctions().isBefore(today))
                .toList();

        return mapSoldArticlesToViewModelList(activeArticles);
    }

    @Override
    public List<SoldArticleViewModel> findByIdCategory(int idCategory) {
        List<SoldArticles> articlesFound = soldArticlesDAO.findByCategory(idCategory);
        return mapSoldArticlesToViewModelList(articlesFound);
    }

    @Override
    @Transactional
    public SoldArticles add(SoldArticleViewModel soldArticleViewModel) {
        SoldArticles soldArticles = soldArticlesDAO.create(soldArticleViewModel.getSoldArticles());
        soldArticleViewModel.getPickUpLocation().setIdArticle(soldArticles.getIdArticle());
        this.pickUpDAO.create(soldArticleViewModel.getPickUpLocation());

        return soldArticles;
    }

    @Override
    public List<SoldArticleViewModel> searchByName(String searchArticleName) {
        List<SoldArticles> articlesFound = soldArticlesDAO.searchByName(searchArticleName);
        return mapSoldArticlesToViewModelList(articlesFound);
    }

    @Override
    @Transactional
    public void update(@Valid SoldArticles article) throws BusinessException {
        BusinessException be = new BusinessException();

        if (article.getEndDateAuctions().isBefore(article.getStartDateAuctions())) {
            be.add("La date de fin doit être postérieure à la date de début");
        }

        if (article.getInitialPrice() <= 0) {
            be.add("Le prix initial doit être supérieur à 0");
        }

        if (be.getlistErrors().isEmpty()) {
            soldArticlesDAO.update(article);
        } else {
            throw be;
        }
    }

    private List<SoldArticleViewModel> mapSoldArticlesToViewModelList(List<SoldArticles> articlesFound) {
        return articlesFound.stream().map(this::mapSoldArticlesToViewModel).toList();
    }

    private SoldArticleViewModel mapSoldArticlesToViewModel(SoldArticles soldArticles) {
        SoldArticleViewModel soldArticleViewModel = new SoldArticleViewModel();
        soldArticleViewModel.setSoldArticles(soldArticles);
        soldArticleViewModel.setSeller(userDAO.findByNum(soldArticles.getIdUser()));
        soldArticleViewModel.setCategory(categoryDAO.findByNum(soldArticles.getIdCategory()));
        soldArticleViewModel.setPickUpLocation(pickUpDAO.findByIdArticle(soldArticles.getIdArticle()));
        return soldArticleViewModel;
    }

    @Override
    public void deleteArticleById(String idArticle) {
        soldArticlesDAO.deleteArticleById(idArticle);
    }

}
