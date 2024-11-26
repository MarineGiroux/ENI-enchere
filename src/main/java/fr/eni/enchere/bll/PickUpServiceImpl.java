package fr.eni.enchere.bll;

import fr.eni.enchere.bo.PickUp;
import org.springframework.stereotype.Service;

import fr.eni.enchere.dal.RetraitDAO;

@Service
public class RetraitServiceImpl implements RetraitService {

	private RetraitDAO retraitDAO;

	public RetraitServiceImpl(RetraitDAO retraitDAO) {
		this.retraitDAO = retraitDAO;
	}

	@Override
	public PickUp findByNum(int noArticle) {
		PickUp r = retraitDAO.findByNum(noArticle);
		return r;
	}

	@Override
	public void createAdresse(PickUp pickUp) {
		retraitDAO.create(pickUp);
	}

}
