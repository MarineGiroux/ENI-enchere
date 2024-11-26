package fr.eni.enchere.bo;

public class PickUp {
	
	private int idArticle;
	private int idPickUp;
	private String road;
	private String zipPass;
	private String city;
	
	
	public PickUp() {
	}
	
	
	public PickUp(String road, String zipPass, String city) {
		this.road = road;
		this.zipPass = zipPass;
		this.city = city;
	}
	
	
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getZipPass() {
		return zipPass;
	}
	public void setZipPass(String zipPass) {
		this.zipPass = zipPass;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getIdArticle() {
		return idArticle;
	}
	public void setIdArticle(int idArticle) {
		this.idArticle = idArticle;
	}
	public int getIdPickUp() {
		return idPickUp;
	}
	public void setIdPickUp(int idPickUp) {
		this.idPickUp = idPickUp;
	}


	@Override
	public String toString() {
		return String.format("PickUp [idArticle=%s, idPickUp=%s, road=%s, zipPass=%s, city=%s]", idArticle, idPickUp, road, zipPass,
				city);
	}




	
	
	

}
