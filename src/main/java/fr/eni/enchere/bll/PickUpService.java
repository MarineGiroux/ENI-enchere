package fr.eni.enchere.bll;

import fr.eni.enchere.bo.PickUp;

public interface RetraitService {
	
	void createAdresse(PickUp pickUp);

	PickUp findByNum(int noArticle);
	
}
