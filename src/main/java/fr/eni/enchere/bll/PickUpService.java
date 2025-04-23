package fr.eni.enchere.bll;

import fr.eni.enchere.bo.PickUp;

public interface PickUpService {
	
	PickUp findByNum(int noArticle);
	
}
