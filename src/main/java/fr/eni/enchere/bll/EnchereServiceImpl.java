package fr.eni.enchere.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.ArticleVenduDAO;
import fr.eni.enchere.dal.CategorieDAO;
import fr.eni.enchere.dal.EnchereDAO;
import fr.eni.enchere.dal.UtilisateurDAO;

@Service
public class EnchereServiceImpl implements EnchereService{
	
	private EnchereDAO enchereDAO;
	private ArticleVenduDAO articleVenduDAO;
	private UtilisateurDAO utilisateurDAO;
	private CategorieDAO categorieDAO;
	
	

	public EnchereServiceImpl(EnchereDAO enchereDAO, ArticleVenduDAO articleVenduDAO, UtilisateurDAO utilisateurDAO,
			CategorieDAO categorieDAO) {
		super();
		this.enchereDAO = enchereDAO;
		this.articleVenduDAO = articleVenduDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.categorieDAO = categorieDAO;
	}

	@Override
	public void encherir(Enchere enchere) {
		//verif argent suffisant
		if (enchere.getMontantEnchere()<= articleVenduDAO.findByNum(enchere.getArticleVendu().getNoArticle()).getMiseAPrix()) {
			//verif date enchere
			if (enchere.getDateEnchere().isAfter(articleVenduDAO.findByNum(enchere.getArticleVendu().getNoArticle()).getDateDebutEnchere())
					&& enchere.getDateEnchere().isBefore(articleVenduDAO.findByNum(enchere.getArticleVendu().getNoArticle()).getDateFinEnchere()) ){
				//verif si une enchere existe pour l'article
				if (enchereDAO.countEnchere(enchere.getArticleVendu().getNoArticle()) != 0) {
					//deja une enchere
					//trouver la plus grosse enchere
					int montantMax = 0;
					int noUtilisateurMax = 0;
					List<Enchere> listeEnchere = enchereDAO.findByArticle(enchere.getArticleVendu().getNoArticle());
					for (Enchere enchere2 : listeEnchere) {
						if (enchere2.getMontantEnchere() > montantMax) {
							montantMax = enchere2.getMontantEnchere();
							noUtilisateurMax = enchere2.getUtilisateur().getNoUtilisateur();
						}
					}
					//verif enchere suffisante
					if (enchere.getMontantEnchere()>montantMax) {
						//debiter credit
						utilisateurDAO.updateCredit(utilisateurDAO.findByNum(enchere.getUtilisateur().getNoUtilisateur()).getCredit()-enchere.getMontantEnchere(), utilisateurDAO.findByNum(enchere.getUtilisateur().getNoUtilisateur()));
						//rembourser enchere precedente
						utilisateurDAO.updateCredit(utilisateurDAO.findByNum(noUtilisateurMax).getCredit() + montantMax,utilisateurDAO.findByNum(noUtilisateurMax));
						//verif si l'utilisateur a deja une enchere pour cet article
						if (enchereDAO.countEnchereUtilisateur(enchere.getArticleVendu().getNoArticle(), enchere.getUtilisateur().getNoUtilisateur())>0) {
							//modifier l'enchère
							enchereDAO.surencherir(enchere);
						} else {
							//creer l'enchere
							enchereDAO.create(enchere);
							articleVenduDAO.updatePrixVente(enchere);
						}
					}else {
						System.out.println("montant de l'enchere inferieur aux autres enchere");
					}
				} else {
					//verif que montant>miseAPrix
					if (enchere.getMontantEnchere() >= articleVenduDAO.findByNum(enchere.getArticleVendu().getNoArticle()).getMiseAPrix()) {
						//premiere enchere
						//debit de credit
						utilisateurDAO.updateCredit(utilisateurDAO.findByNum(enchere.getUtilisateur().getNoUtilisateur()).getCredit()-enchere.getMontantEnchere(), utilisateurDAO.findByNum(enchere.getUtilisateur().getNoUtilisateur()));
						//creation de l'enchere
						enchereDAO.create(enchere);
						articleVenduDAO.updatePrixVente(enchere);
					}else {
						System.out.println("montant inferieur a la mise a prix");
					}	
				}
			}else {
				System.out.println("enchere deja fermée");
			}
		} else {
			System.out.println("argent insufisant pour honorer l'enchere");
		}

	}

	@Override
	public List<Enchere> recupererEncheres() {
		List<Enchere> listeenchere = enchereDAO.findAll();
		
		for (Enchere enchere : listeenchere) {
			ArticleVendu articleVendu = articleVenduDAO.findByNum(enchere.getArticleVendu().getNoArticle());
			articleVendu.setCategorieArticle(categorieDAO.findByNum(articleVendu.getCategorieArticle().getNoCategorie()));
			

			enchere.setArticleVendu(articleVendu);
			enchere.setUtilisateur(utilisateurDAO.findByNum(enchere.getUtilisateur().getNoUtilisateur()));
		}
		return listeenchere;
	}

	@Override
	public List<Enchere> findByID(int idEnchere) {
		List<Enchere> e = enchereDAO.findByArticle(idEnchere);		
		return e;
	}

	@Override
	public int montantEnchere(int noArticle) {
		// TODO Auto-generated method stub
		return 0;
	}

}








































