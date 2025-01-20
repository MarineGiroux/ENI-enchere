package fr.eni.enchere.bll;

import fr.eni.enchere.bo.PickUp;
import org.springframework.stereotype.Service;

import fr.eni.enchere.dal.PickUpDAO;

@Service
public class PickUpServiceImpl implements PickUpService {

	private PickUpDAO pickUpDAO;

	public PickUpServiceImpl(PickUpDAO pickUpDAO) {
		this.pickUpDAO = pickUpDAO;
	}

	@Override
	public PickUp findByNum(int noArticle) {
		PickUp p = pickUpDAO.findByIdArticle(noArticle);
		return p;
	}

}
