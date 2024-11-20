package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Categorie;

public interface CategorieDAO {
	
	Categorie findByNum (int noCategorie);
	
	void create (Categorie categorie);

	List<Categorie> FindAll();

}
