package fr.eni.enchere.bo;

public class Categorie {
	
	private int noCategorie;
	private String libelle;
	
	
	public Categorie() {
	}
	
	
	public Categorie(int noCategorie, String libelle) {
		this.noCategorie = noCategorie;
		this.libelle = libelle;
	}
	
	
	public int getNoCategorie() {
		return noCategorie;
	}
	public void setNoCategorie(int noCategorie) {
		this.noCategorie = noCategorie;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	@Override
	public String toString() {
		return String.format("Categorie [noCategorie=%s, libelle=%s]", noCategorie, libelle);
	}
	
	

}
