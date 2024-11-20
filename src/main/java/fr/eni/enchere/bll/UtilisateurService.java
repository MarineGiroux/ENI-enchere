package fr.eni.enchere.bll;


import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.exception.BusinessException;


public interface UtilisateurService {

	void add(Utilisateur utilisateur, String confirmMdP) throws BusinessException;
	
	Utilisateur findByEmail(String emailUtilisateur);
	
	void update(Utilisateur utilisateur) throws BusinessException;
	
	void deleteAccountByEmail(String email);
}
