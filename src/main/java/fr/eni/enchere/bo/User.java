package fr.eni.enchere.bo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class User {
	

	private int idUser;
	@NotBlank
	@Pattern(regexp = "\\w+")
	private String pseudo;
	@NotBlank
	private String lastName;
	@NotBlank
	private String firstName;
	@NotBlank
	@Email
	private String email;
	private String phone;
	private String road;
	private String zipPass;
	private String city;
	@NotBlank
	private String passWord;
	private String picture;
	private int credit;
	private boolean administrator;
	
	
	public User() {
	}
	
	public User(int idUser, String pseudo, String lastName, String firstName, String email, String phone,
				String road, String zipPass, String city, String passWord, String picture, int credit, boolean administrator) {
		this.idUser = idUser;
		this.pseudo = pseudo;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
		this.road = road;
		this.zipPass = zipPass;
		this.city = city;
		this.passWord = passWord;
		this.picture = picture;
		this.credit = credit;
		this.administrator = administrator;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public @NotBlank @Pattern(regexp = "\\w+") String getPseudo() {
		return pseudo;
	}

	public void setPseudo(@NotBlank @Pattern(regexp = "\\w+") String pseudo) {
		this.pseudo = pseudo;
	}

	public @NotBlank String getLastName() {
		return lastName;
	}

	public void setLastName(@NotBlank String lastName) {
		this.lastName = lastName;
	}

	public @NotBlank String getFirstName() {
		return firstName;
	}

	public void setFirstName(@NotBlank String firstName) {
		this.firstName = firstName;
	}

	public @NotBlank @Email String getEmail() {
		return email;
	}

	public void setEmail(@NotBlank @Email String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public @NotBlank String getPassWord() {
		return passWord;
	}

	public void setPassWord(@NotBlank String passWord) {
		this.passWord = passWord;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	@Override
	public String toString() {
		return String.format(
				"User [idUser=%s, pseudo=%s, lastName=%s, firstName=%s, email=%s, phone=%s, road=%s, zipPass=%s, city=%s, passWord=%s, picture=%s, credit=%s, administrator=%s]",
				idUser, pseudo, lastName, firstName, email, phone, road, zipPass, city, passWord, picture, credit,
				administrator);
	}
	
	
	

}
