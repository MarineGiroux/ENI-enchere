package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;

public interface RetraitDAO {
	
	void create (PickUp pickUp);
	
	PickUp findByNum (int noArticle);

}
