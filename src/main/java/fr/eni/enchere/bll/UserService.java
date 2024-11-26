package fr.eni.enchere.bll;


import fr.eni.enchere.bo.User;
import fr.eni.enchere.exception.BusinessException;


public interface UserService {

	void add(User user, String ConfirmPassword) throws BusinessException;
	
	User findByEmail(String emailUser);
	
	void update(User user) throws BusinessException;
	
	void deleteAccountByEmail(String email);
}
