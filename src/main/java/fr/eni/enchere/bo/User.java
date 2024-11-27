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
	private String password;
	private String picture;
	private int credit;
	private int idRole;
	
	
	public User() {
	}
	
	public User(int idUser, String pseudo, String lastName, String firstName, String email, String phone,
				String road, String zipPass, String city, String password, String picture, int credit, int idRole) {
		this.idUser = idUser;
		this.pseudo = pseudo;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.phone = phone;
		this.road = road;
		this.zipPass = zipPass;
		this.city = city;
		this.password = password;
		this.picture = picture;
		this.credit = credit;
		this.idRole = idRole;
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

	public @NotBlank String getPassword() {
		return password;
	}

	public void setPassword(@NotBlank String password) {
		this.password = password;
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

	public int isIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	@Override
	public String toString() {
		return String.format(
				"User [idUser=%s, pseudo=%s, lastName=%s, firstName=%s, email=%s, phone=%s, road=%s, zipPass=%s, city=%s, password=%s, picture=%s, credit=%s, idRole=%s]",
				idUser, pseudo, lastName, firstName, email, phone, road, zipPass, city, password, picture, credit,
				idRole);
	}
	
	
	

}
