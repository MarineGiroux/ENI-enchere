package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;

public interface PickUpDAO {
	
	void create (PickUp pickUp);
	
	PickUp findByNum (int idArticle);

}
