package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.dal.SoldArticlesDAO;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.UserDAO;

@Service
public class SoldArticlesServiceImpl implements SoldArticlesService {

	private SoldArticlesDAO soldArticlesDAO;
	private UserDAO userDAO;
	private CategoryDAO categoryDAO;

	public SoldArticlesServiceImpl(SoldArticlesDAO soldArticlesDAO, UserDAO userDAO,
								   CategoryDAO categoryDAO) {
		this.soldArticlesDAO = soldArticlesDAO;
		this.userDAO = userDAO;
		this.categoryDAO = categoryDAO;
	}

	@Override
	public SoldArticles FindById(int id) {
		SoldArticles a = soldArticlesDAO.findByNum(id);
		if (a.getBuy() != null) {
			a.setBuy(userDAO.findByNum(a.getBuy().getIdUser()));
		}
		a.setSell(userDAO.findByNum(a.getSell().getIdUser()));
		a.setCartegoryArticle(categoryDAO.findByNum(a.getCartegoryArticle().getIdCategory()));
		;
		return a;
	}

	@Override
	public List<SoldArticles> findAll() {
		List<SoldArticles> a = soldArticlesDAO.FindAll();
		for (SoldArticles soldArticles : a) {
			if (soldArticles.getBuy() != null) {
				soldArticles.setBuy(userDAO.findByNum(soldArticles.getBuy().getIdUser()));
			}
			soldArticles.setSell(userDAO.findByNum(soldArticles.getSell().getIdUser()));

			soldArticles
					.setCartegoryArticle(categoryDAO.findByNum(soldArticles.getCartegoryArticle().getIdCategory()));
			;
		}
		return a;
	}

	@Override
	@Transactional
	public void add(SoldArticles soldArticles, User user) {
		soldArticles.setSell(user);
		System.out.println("sos " + soldArticles);
		soldArticlesDAO.create(soldArticles);
		soldArticles.getPickUpLocation().setIdArticle(soldArticles.getIdArticle());
	}

}
