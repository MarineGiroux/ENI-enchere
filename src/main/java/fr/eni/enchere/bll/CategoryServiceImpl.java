package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Category;
import org.springframework.stereotype.Service;

import fr.eni.enchere.dal.CategoryDAO;

@Service
public class CategorieServiceImpl implements CategorieService {

	private CategoryDAO categoryDAO;
	
	public CategorieServiceImpl(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}


	@Override
	public List<Category> FindAll() {
		return categoryDAO.FindAll();
	}

}
