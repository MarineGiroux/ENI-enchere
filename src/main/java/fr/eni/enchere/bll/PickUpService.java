package fr.eni.enchere.bll;

import fr.eni.enchere.bo.PickUp;

public interface PickUpService {
	
	void createAdress(PickUp pickUp);

	PickUp findByNum(int noArticle);
	
}
