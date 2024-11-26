package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;

public interface ItemSoldService {
	
	ItemSold FindById (int id);
	
	List<ItemSold> findAll();
	
	void add(ItemSold itemSold, User user) throws BusinessException;

}
