package fr.eni.enchere.bo;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ArticleVendu {
	
	private int noArticle;
	@NotBlank
	private String nomArticle;
	@NotBlank
	private String description;
	@NotNull
	private LocalDate dateDebutEnchere;
	@NotNull
	private LocalDate dateFinEnchere;
	@NotNull
	private int miseAPrix;
	private int prixVente;
	private boolean etatVente;
	
	private Utilisateur achete;
	private Utilisateur vend;
	private Categorie categorieArticle;
	private Retrait lieuRetrait;
	
	public ArticleVendu() {
		lieuRetrait = new Retrait();
		vend = new Utilisateur();
		achete = new Utilisateur();
		categorieArticle = new Categorie();
	}
	
	public ArticleVendu(int noArticle, String nomArticle, String description, LocalDate dateDebutEnchere,
			LocalDate dateFinEnchere, int miseAPrix, int priVente, boolean etatVente, Utilisateur achete,
			Utilisateur vend, Categorie categorieArticle, Retrait lieuRetrait) {
		this.noArticle = noArticle;
		this.nomArticle = nomArticle;
		this.description = description;
		this.dateDebutEnchere = dateDebutEnchere;
		this.dateFinEnchere = dateFinEnchere;
		this.miseAPrix = miseAPrix;
		this.prixVente = priVente;
		this.etatVente = etatVente;
		this.achete = achete;
		this.vend = vend;
		this.categorieArticle = categorieArticle;
		this.lieuRetrait = lieuRetrait;
	}

	public int getNoArticle() {
		return noArticle;
	}
	public void setNoArticle(int noArticle) {
		this.noArticle = noArticle;
	}
	public String getNomArticle() {
		return nomArticle;
	}
	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDateDebutEnchere() {
		return dateDebutEnchere;
	}
	public void setDateDebutEnchere(LocalDate dateDebutEnchere) {
		this.dateDebutEnchere = dateDebutEnchere;
	}
	public LocalDate getDateFinEnchere() {
		return dateFinEnchere;
	}
	public void setDateFinEnchere(LocalDate dateFinEnchere) {
		this.dateFinEnchere = dateFinEnchere;
	}
	public int getMiseAPrix() {
		return miseAPrix;
	}
	public void setMiseAPrix(int miseAPrix) {
		this.miseAPrix = miseAPrix;
	}
	public int getPrixVente() {
		return prixVente;
	}
	public void setPrixVente(int priVente) {
		this.prixVente = priVente;
	}
	public boolean getEtatVente() {
		return etatVente;
	}
	public void setEtatVente(boolean etatVente) {
		this.etatVente = etatVente;
	}
	public Utilisateur getAchete() {
		return achete;
	}
	public void setAchete(Utilisateur achete) {
		this.achete = achete;
	}
	public Utilisateur getVend() {
		return vend;
	}
	public void setVend(Utilisateur vend) {
		this.vend = vend;
	}
	public Categorie getCategorieArticle() {
		return categorieArticle;
	}
	public void setCategorieArticle(Categorie categorieArticle) {
		this.categorieArticle = categorieArticle;
	}
	public Retrait getLieuRetrait() {
		return lieuRetrait;
	}
	public void setLieuRetrait(Retrait lieuRetrait) {
		this.lieuRetrait = lieuRetrait;
	}

	@Override
	public String toString() {
		return String.format(
				"ArticleVendu [noArticle=%s, nomArticle=%s, description=%s, dateDebutEnchere=%s, dateFinEnchere=%s, miseAPrix=%s, priVente=%s, etatVente=%s, achete=%s, vend=%s, categorieArticle=%s, lieuRetrait=%s]",
				noArticle, nomArticle, description, dateDebutEnchere, dateFinEnchere, miseAPrix, prixVente, etatVente,
				achete, vend, categorieArticle, lieuRetrait);
	}
	
	

}
