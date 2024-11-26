package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;
import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.dal.ItemSoldDAO;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.EnchereDAO;
import fr.eni.enchere.dal.UserDAO;

@Service
public class EnchereServiceImpl implements EnchereService{
	
	private EnchereDAO enchereDAO;
	private ItemSoldDAO itemSoldDAO;
	private UserDAO userDAO;
	private CategoryDAO categoryDAO;
	
	

	public EnchereServiceImpl(EnchereDAO enchereDAO, ItemSoldDAO itemSoldDAO, UserDAO userDAO,
							  CategoryDAO categoryDAO) {
		super();
		this.enchereDAO = enchereDAO;
		this.itemSoldDAO = itemSoldDAO;
		this.userDAO = userDAO;
		this.categoryDAO = categoryDAO;
	}

	@Override
	public void encherir(Auctions auctions) {
		//verif argent suffisant
		if (auctions.getAmountAuctions()<= itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getInitialPrice()) {
			//verif date enchere
			if (auctions.getDateAuctions().isAfter(itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getStartDateAuctions())
					&& auctions.getDateAuctions().isBefore(itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getEndDateAuctions()) ){
				//verif si une enchere existe pour l'article
				if (enchereDAO.countEnchere(auctions.getItemSold().getIdArticle()) != 0) {
					//deja une enchere
					//trouver la plus grosse enchere
					int montantMax = 0;
					int noUtilisateurMax = 0;
					List<Auctions> listeAuctions = enchereDAO.findByArticle(auctions.getItemSold().getIdArticle());
					for (Auctions auctions2 : listeAuctions) {
						if (auctions2.getAmountAuctions() > montantMax) {
							montantMax = auctions2.getAmountAuctions();
							noUtilisateurMax = auctions2.getUtilisateur().getIdUser();
						}
					}
					//verif enchere suffisante
					if (auctions.getAmountAuctions()>montantMax) {
						//debiter credit
						userDAO.updateCredit(userDAO.findByNum(auctions.getUtilisateur().getIdUser()).getCredit()- auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUtilisateur().getIdUser()));
						//rembourser enchere precedente
						userDAO.updateCredit(userDAO.findByNum(noUtilisateurMax).getCredit() + montantMax, userDAO.findByNum(noUtilisateurMax));
						//verif si l'utilisateur a deja une enchere pour cet article
						if (enchereDAO.countEnchereUtilisateur(auctions.getItemSold().getIdArticle(), auctions.getUtilisateur().getIdUser())>0) {
							//modifier l'enchère
							enchereDAO.surencherir(auctions);
						} else {
							//creer l'enchere
							enchereDAO.create(auctions);
							itemSoldDAO.updatePrixVente(auctions);
						}
					}else {
						System.out.println("montant de l'enchere inferieur aux autres enchere");
					}
				} else {
					//verif que montant>miseAPrix
					if (auctions.getAmountAuctions() >= itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getInitialPrice()) {
						//premiere enchere
						//debit de credit
						userDAO.updateCredit(userDAO.findByNum(auctions.getUtilisateur().getIdUser()).getCredit()- auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUtilisateur().getIdUser()));
						//creation de l'enchere
						enchereDAO.create(auctions);
						itemSoldDAO.updatePrixVente(auctions);
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
	public List<Auctions> recupererEncheres() {
		List<Auctions> listeenchere = enchereDAO.findAll();
		
		for (Auctions auctions : listeenchere) {
			ItemSold itemSold = itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle());
			itemSold.setCartegoryArticle(categoryDAO.findByNum(itemSold.getCartegoryArticle().getIdCategory()));
			

			auctions.setArticleVendu(itemSold);
			auctions.setUtilisateur(userDAO.findByNum(auctions.getUtilisateur().getIdUser()));
		}
		return listeenchere;
	}

	@Override
	public List<Auctions> findByID(int idEnchere) {
		List<Auctions> e = enchereDAO.findByArticle(idEnchere);
		return e;
	}

	@Override
	public int montantEnchere(int noArticle) {
		// TODO Auto-generated method stub
		return 0;
	}

}








































