package fr.eni.enchere.dal;

import fr.eni.enchere.bo.Retrait;

public interface RetraitDAO {
	
	void create (Retrait retrait);
	
	Retrait findByNum (int noArticle);

}
