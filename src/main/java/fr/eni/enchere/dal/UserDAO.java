package fr.eni.enchere.dal;

import fr.eni.enchere.bo.User;

public interface UserDAO {
	
	void create (User user);
	
	User findByEmail (String idUser);

	int countEmail(String email);
	
	int countPseudo(String pseudo);
	
	void update(User user);

	void createRole(User user);
	
	User findByNum(int idUser);
	
	void updateCredit(int rising, User user);

	void deleteAccountByEmail(String email);
	
}
