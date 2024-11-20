package fr.eni.enchere.bo;

import java.time.LocalDate;

public class Enchere {
	
	private LocalDate dateEnchere;
	private int montantEnchere;
	
	private Utilisateur utilisateur;
	private ArticleVendu articleVendu;
	
	public Enchere() {
	}
	
	public Enchere(LocalDate dateEnchere, int montantEnchere, Utilisateur utilisateur, ArticleVendu articleVendu) {
		this.dateEnchere = dateEnchere;
		this.montantEnchere = montantEnchere;
		this.utilisateur = utilisateur;
		this.articleVendu = articleVendu;
	}
	
	public LocalDate getDateEnchere() {
		return dateEnchere;
	}
	public void setDateEnchere(LocalDate dateEnchere) {
		this.dateEnchere = dateEnchere;
	}
	public int getMontantEnchere() {
		return montantEnchere;
	}
	public void setMontantEnchere(int montantEnchere) {
		this.montantEnchere = montantEnchere;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public ArticleVendu getArticleVendu() {
		return articleVendu;
	}
	public void setArticleVendu(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}

	@Override
	public String toString() {
		return String.format("Enchere [dateEnchere=%s, montantEnchere=%s, utilisateur=%s, articleVendu=%s]",
				dateEnchere, montantEnchere, utilisateur, articleVendu);
	}

	
	
}
