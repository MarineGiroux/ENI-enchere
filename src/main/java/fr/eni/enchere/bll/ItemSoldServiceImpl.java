package fr.eni.enchere.bll;

import java.util.List;

import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.ArticleVenduDAO;
import fr.eni.enchere.dal.CategorieDAO;
import fr.eni.enchere.dal.UtilisateurDAO;

@Service
public class ArticleVenduServiceImpl implements ArticleVenduService {

	private ArticleVenduDAO articleVenduDAO;
	private UtilisateurDAO utilisateurDAO;
	private CategorieDAO categorieDAO;

	public ArticleVenduServiceImpl(ArticleVenduDAO articleVenduDAO, UtilisateurDAO utilisateurDAO,
			CategorieDAO categorieDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.categorieDAO = categorieDAO;
	}

	@Override
	public ArticleVendu FindById(int id) {
		ArticleVendu a = articleVenduDAO.findByNum(id);
		if (a.getAchete() != null) {
			a.setAchete(utilisateurDAO.findByNum(a.getAchete().getNoUtilisateur()));
		}
		a.setVend(utilisateurDAO.findByNum(a.getVend().getNoUtilisateur()));
		a.setCategorieArticle(categorieDAO.findByNum(a.getCategorieArticle().getNoCategorie()));
		;
		return a;
	}

	@Override
	public List<ArticleVendu> FindAll() {
		List<ArticleVendu> a = articleVenduDAO.FindAll();
		for (ArticleVendu articleVendu : a) {
			if (articleVendu.getAchete() != null) {
				articleVendu.setAchete(utilisateurDAO.findByNum(articleVendu.getAchete().getNoUtilisateur()));
			}
			articleVendu.setVend(utilisateurDAO.findByNum(articleVendu.getVend().getNoUtilisateur()));

			articleVendu
					.setCategorieArticle(categorieDAO.findByNum(articleVendu.getCategorieArticle().getNoCategorie()));
			;
		}
		return a;
	}

	@Override
	@Transactional
	public void add(ArticleVendu articleVendu, Utilisateur utilisateur ) {
		articleVendu.setVend(utilisateur);
		System.out.println("sos " + articleVendu);
		articleVenduDAO.create(articleVendu);
		articleVendu.getLieuRetrait().setNoArticle(articleVendu.getNoArticle());
	}

}
