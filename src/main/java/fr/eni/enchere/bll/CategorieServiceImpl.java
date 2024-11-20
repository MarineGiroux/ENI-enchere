package fr.eni.enchere.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.dal.CategorieDAO;

@Service
public class CategorieServiceImpl implements CategorieService {

	private CategorieDAO categorieDAO;
	
	public CategorieServiceImpl(CategorieDAO categorieDAO) {
		this.categorieDAO = categorieDAO;
	}


	@Override
	public List<Categorie> FindAll() {
		return categorieDAO.FindAll();
	}

}
