package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Retrait;

public interface RetraitService {
	
	void createAdresse(Retrait retrait);

	Retrait findByNum(int noArticle);
	
}
