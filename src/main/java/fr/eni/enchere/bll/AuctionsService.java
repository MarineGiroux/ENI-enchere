package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.exception.BusinessException;


public interface AuctionsService {

	void bid(Auctions auctions) throws BusinessException;
	
	List<Auctions> recoverAuctions();

    void closeOutdatedAuctions(String userEmail);
}
