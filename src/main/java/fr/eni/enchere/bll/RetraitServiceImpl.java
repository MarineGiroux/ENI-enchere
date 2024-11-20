package fr.eni.enchere.bll;

import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.Retrait;
import fr.eni.enchere.dal.RetraitDAO;

@Service
public class RetraitServiceImpl implements RetraitService {

	private RetraitDAO retraitDAO;

	public RetraitServiceImpl(RetraitDAO retraitDAO) {
		this.retraitDAO = retraitDAO;
	}

	@Override
	public Retrait findByNum(int noArticle) {
		Retrait r = retraitDAO.findByNum(noArticle);
		return r;
	}

	@Override
	public void createAdresse(Retrait retrait) {
		retraitDAO.create(retrait);
	}

}
