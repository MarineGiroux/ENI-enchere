package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Enchere;

public interface EnchereDAO {
	
	
	void create (Enchere enchere);

	Enchere findBynoUtilisateur(int noArticle,int noUtilisateur);

	List<Enchere> findAll();
	
	int countEnchereUtilisateur (int noArticle,int noUtilisateur);
	
	int countEnchere (int noArticle);
	
	void surencherir (Enchere enchere);
	
	List<Enchere> findByArticle(int noArticle);

}
