package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;

public interface PickUpDAO {
	
	void create(PickUp pickUp);
	
	PickUp findByIdArticle(int idArticle);

	void update(PickUp pickUp);

}
