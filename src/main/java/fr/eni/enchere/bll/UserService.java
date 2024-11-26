package fr.eni.enchere.bll;


import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;


public interface UtilisateurService {

	void add(User user, String confirmMdP) throws BusinessException;
	
	User findByEmail(String emailUtilisateur);
	
	void update(User user) throws BusinessException;
	
	void deleteAccountByEmail(String email);
}
