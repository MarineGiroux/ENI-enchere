package fr.eni.enchere.bo;

public class Category {
	
	private int idCategory;
	private String wording;
	
	
	public Category() {
	}
	
	
	public Category(int noCategorie, String wording) {
		this.idCategory = noCategorie;
		this.wording = wording;
	}


	public int getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}

	public String getWording() {
		return wording;
	}

	public void setWording(String wording) {
		this.wording = wording;
	}

	@Override
	public String toString() {
		return "Category{" +
				"idCategory=" + idCategory +
				", wording='" + wording + '\'' +
				'}';
	}
}
