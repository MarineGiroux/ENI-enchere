package fr.eni.enchere.bo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Utilisateur {
	

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
	
	
	public Utilisateur() {
	}
	
	public Utilisateur(int idUser, String pseudo, String lastName, String firstName, String email, String phone,
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
	
	
	public int getNoUtilisateur() {
		return idUser;
	}
	public void setNoUtilisateur(int noUtilisateur) {
		this.idUser = noUtilisateur;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getNom() {
		return lastName;
	}
	public void setNom(String nom) {
		this.lastName = nom;
	}
	public String getPrenom() {
		return firstName;
	}
	public void setPrenom(String prenom) {
		this.firstName = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return phone;
	}
	public void setTelephone(String telephone) {
		this.phone = telephone;
	}
	public String getRue() {
		return road;
	}
	public void setRue(String rue) {
		this.road = rue;
	}
	public String getCodePostal() {
		return zipPass;
	}
	public void setCodePostal(String codePostal) {
		this.zipPass = codePostal;
	}
	public String getVille() {
		return city;
	}
	public void setVille(String ville) {
		this.city = ville;
	}
	public String getMotDePasse() {
		return passWord;
	}
	public void setMotDePasse(String motDePasse) {
		this.passWord = motDePasse;
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
				"User [idUser=%s, pseudo=%s, lastName=%s, firstName=%s, email=%s, phone=%s, road=%s, zipPass=%s, city=%s, passWord=%s, credit=%s, administrator=%s]",
				idUser, pseudo, lastName, firstName, email, phone, road, zipPass, city, passWord, credit,
				administrator);
	}
	
	
	

}
