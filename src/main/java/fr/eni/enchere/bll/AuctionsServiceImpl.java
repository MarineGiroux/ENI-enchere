package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;
import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.dal.ItemSoldDAO;
import fr.eni.enchere.dal.CategoryDAO;
import fr.eni.enchere.dal.AuctionsDAO;
import fr.eni.enchere.dal.UserDAO;

@Service
public class AuctionsServiceImpl implements AuctionsService {
	
	private AuctionsDAO auctionsDAO;
	private ItemSoldDAO itemSoldDAO;
	private UserDAO userDAO;
	private CategoryDAO categoryDAO;
	
	

	public AuctionsServiceImpl(AuctionsDAO auctionsDAO, ItemSoldDAO itemSoldDAO, UserDAO userDAO,
							   CategoryDAO categoryDAO) {
		super();
		this.auctionsDAO = auctionsDAO;
		this.itemSoldDAO = itemSoldDAO;
		this.userDAO = userDAO;
		this.categoryDAO = categoryDAO;
	}

	@Override
	public void bid(Auctions auctions) {
		//verif argent suffisant
		if (auctions.getAmountAuctions()<= itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getInitialPrice()) {
			//verif date auctions
			if (auctions.getDateAuctions().isAfter(itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getStartDateAuctions())
					&& auctions.getDateAuctions().isBefore(itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getEndDateAuctions()) ){
				//verif si une auctions existe pour l'article
				if (auctionsDAO.countAuction(auctions.getItemSold().getIdArticle()) != 0) {
					//deja une auctions
					//trouver la plus grosse auctions
					int maxAmount = 0;
					int idUserMax = 0;
					List<Auctions> listAuctions = auctionsDAO.findByArticle(auctions.getItemSold().getIdArticle());
					for (Auctions auctions2 : listAuctions) {
						if (auctions2.getAmountAuctions() > maxAmount) {
							maxAmount = auctions2.getAmountAuctions();
							idUserMax = auctions2.getUser().getIdUser();
						}
					}
					//verif auctions suffisante
					if (auctions.getAmountAuctions()>maxAmount) {
						//debiter credit
						userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit()- auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
						//rembourser auctions precedente
						userDAO.updateCredit(userDAO.findByNum(idUserMax).getCredit() + maxAmount, userDAO.findByNum(idUserMax));
						//verif si l'user a deja une auctions pour cet article
						if (auctionsDAO.countAuctionsUser(auctions.getItemSold().getIdArticle(), auctions.getUser().getIdUser())>0) {
							//modifier l'enchère
							auctionsDAO.outbid(auctions);
						} else {
							//creer l'auctions
							auctionsDAO.create(auctions);
							itemSoldDAO.updatePriceSale(auctions);
						}
					}else {
						System.out.println("montant de l'auctions inferieur aux autres auctions");
					}
				} else {
					//verif que montant>miseAPrix
					if (auctions.getAmountAuctions() >= itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle()).getInitialPrice()) {
						//premiere auctions
						//debit de credit
						userDAO.updateCredit(userDAO.findByNum(auctions.getUser().getIdUser()).getCredit()- auctions.getAmountAuctions(), userDAO.findByNum(auctions.getUser().getIdUser()));
						//creation de l'auctions
						auctionsDAO.create(auctions);
						itemSoldDAO.updatePriceSale(auctions);
					}else {
						System.out.println("montant inferieur a la mise a prix");
					}	
				}
			}else {
				System.out.println("auctions deja fermée");
			}
		} else {
			System.out.println("argent insufisant pour honorer l'auctions");
		}

	}

	@Override
	public List<Auctions> recoverAuctions() {
		List<Auctions> auctionList = auctionsDAO.findAll();
		
		for (Auctions auctions : auctionList) {
			ItemSold itemSold = itemSoldDAO.findByNum(auctions.getItemSold().getIdArticle());
			itemSold.setCartegoryArticle(categoryDAO.findByNum(itemSold.getCartegoryArticle().getIdCategory()));
			

			auctions.setItemSold(itemSold);
			auctions.setUser(userDAO.findByNum(auctions.getUser().getIdUser()));
		}
		return auctionList;
	}

	@Override
	public List<Auctions> findByID(int idAuctions) {
		List<Auctions> e = auctionsDAO.findByArticle(idAuctions);
		return e;
	}

	@Override
	public int amountAuction(int idArticle) {
		// TODO Auto-generated method stub
		return 0;
	}

}