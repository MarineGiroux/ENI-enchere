package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.bo.Auctions;

public interface ItemSoldDAO {
	
	List<ItemSold> findByCategory(int idCategory);
	
	ItemSold findByNum (int idArticle);
	
	void create (ItemSold itemSold);

	List<ItemSold> FindAll();
	
	void updatePriceSale(Auctions auctions);
}
