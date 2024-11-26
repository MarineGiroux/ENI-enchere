package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.bo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.dal.ItemSoldDAO;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.UserDAO;

@Service
public class ItemSoldServiceImpl implements ItemSoldService {

	private ItemSoldDAO itemSoldDAO;
	private UserDAO userDAO;
	private CategoryDAO categoryDAO;

	public ItemSoldServiceImpl(ItemSoldDAO itemSoldDAO, UserDAO userDAO,
							   CategoryDAO categoryDAO) {
		this.itemSoldDAO = itemSoldDAO;
		this.userDAO = userDAO;
		this.categoryDAO = categoryDAO;
	}

	@Override
	public ItemSold FindById(int id) {
		ItemSold a = itemSoldDAO.findByNum(id);
		if (a.getBuy() != null) {
			a.setBuy(userDAO.findByNum(a.getBuy().getIdUser()));
		}
		a.setSell(userDAO.findByNum(a.getSell().getIdUser()));
		a.setCartegoryArticle(categoryDAO.findByNum(a.getCartegoryArticle().getIdCategory()));
		;
		return a;
	}

	@Override
	public List<ItemSold> findAll() {
		List<ItemSold> a = itemSoldDAO.FindAll();
		for (ItemSold itemSold : a) {
			if (itemSold.getBuy() != null) {
				itemSold.setBuy(userDAO.findByNum(itemSold.getBuy().getIdUser()));
			}
			itemSold.setSell(userDAO.findByNum(itemSold.getSell().getIdUser()));

			itemSold
					.setCartegoryArticle(categoryDAO.findByNum(itemSold.getCartegoryArticle().getIdCategory()));
			;
		}
		return a;
	}

	@Override
	@Transactional
	public void add(ItemSold itemSold, User user) {
		itemSold.setSell(user);
		System.out.println("sos " + itemSold);
		itemSoldDAO.create(itemSold);
		itemSold.getPickUpLocation().setIdArticle(itemSold.getIdArticle());
	}

}
