package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Category;
import org.springframework.stereotype.Service;

import fr.eni.enchere.dal.CategoryDAO;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryDAO categoryDAO;
	
	public CategoryServiceImpl(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}


	@Override
	public List<Category> findAll() {
		return categoryDAO.FindAll();
	}

}
