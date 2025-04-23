package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Category;

public interface CategoryDAO {
	
	Category findByNum (int idCategory);
	
	void create (Category category);

	List<Category> FindAll();

}
