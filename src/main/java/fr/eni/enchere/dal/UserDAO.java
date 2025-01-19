package fr.eni.enchere.dal;

import fr.eni.enchere.bo.User;

public interface UserDAO {
	
	void create (User user);
	
	User findByEmail (String email);

	int countEmail(String email);
	
	int countPseudo(String pseudo);
	
	void update(User user);

	User findByNum(int idUser);
	
	void updateCredit(int rising, User user);

	void riseCredits(int rising, User user);

	void deleteAccountByEmail(String email);
	
}
