package fr.eni.enchere.dal;

import fr.eni.enchere.bo.Utilisateur;

public interface UtilisateurDAO {
	
	void create (Utilisateur utilisateur);
	
	Utilisateur findByEmail (String noUtilisateur);

	int countEmail(String email);
	
	int countPseudo(String pseudo);
	
	void update(Utilisateur utilisateur);

	void createRole(Utilisateur utilisateur);
	
	Utilisateur findByNum(int noUtilisateur);
	
	void updateCredit(int montant, Utilisateur utilisateur);

	void deleteAccountByEmail(String email);
	
}
