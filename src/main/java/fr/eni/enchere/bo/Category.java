package fr.eni.enchere.bo;

public class Category {
	
	private int idCategory;
	private String categoryName;
	
	
	public Category() {
	}
	
	
	public Category(int idCategory, String categoryName) {
		this.idCategory = idCategory;
		this.categoryName = categoryName;
	}


	public int getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(int idCategory) {
		this.idCategory = idCategory;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "Category{" +
				"idCategory=" + idCategory +
				", categoryName='" + categoryName + '\'' +
				'}';
	}
}
