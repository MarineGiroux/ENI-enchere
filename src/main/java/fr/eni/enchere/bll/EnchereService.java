package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Enchere;


public interface EnchereService {

	void encherir(Enchere enchere);
	
	List<Enchere> recupererEncheres();
	
	List<Enchere> findByID(int idEnchere);
	
	int montantEnchere (int noArticle);

}
