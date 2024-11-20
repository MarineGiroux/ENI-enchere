package fr.eni.enchere.bo;

public class Retrait {
	
	private int noArticle;
	private String rue;
	private String codePostal;
	private String ville;
	
	
	public Retrait() {
	}
	
	
	public Retrait(String rue, String codePostal, String ville) {
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
	}
	
	
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public int getNoArticle() {
		return noArticle;
	}
	public void setNoArticle(int noArticle) {
		this.noArticle = noArticle;
	}


	@Override
	public String toString() {
		return String.format("Retrait [noArticle=%s, rue=%s, codePostal=%s, ville=%s]", noArticle, rue, codePostal,
				ville);
	}




	
	
	

}
