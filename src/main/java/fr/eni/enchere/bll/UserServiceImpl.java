package fr.eni.enchere.bll;

import fr.eni.enchere.bo.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.dal.UserDAO;
import fr.eni.enchere.exception.BusinessException;

@Service
public class UserServiceImpl implements UserService {

	private UserDAO userDAO;
	
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	

	@Override
	@Transactional
	public void add(User user, String ConfirmPassword) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valid = validateUniqueEmail(user.getEmail(), be);
				valid &= validateUniquePseudo(user.getPseudo(), be);
				valid &= validateConfirmPassword(user, ConfirmPassword, be);
				valid &= validatePassword(user.getPassword(), be);
		
		if(valid) {
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String pwdEncoder = passwordEncoder.encode(user.getPassword());
			user.setPassword(pwdEncoder);
			userDAO.create(user);
		}else {
			throw be;
		}
	}

	private boolean validatePassword(String password, BusinessException be) {
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$";

		if (!password.matches(passwordRegex)) {
			be.add("Le mot de passe doit contenir au moins 12 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial");
			return false;
		}
		return true;
	}


	private boolean validateUniqueEmail(String email, BusinessException be) {
		int nbEmail = userDAO.countEmail(email);
		
		if(nbEmail == 1) {
			be.add("Email ou mot de passe incorrect");
			return false;
		}
		
		return nbEmail == 0;
	}

	private boolean validateUniquePseudo(String pseudo, BusinessException be) {
		int nbPseudo = userDAO.countPseudo(pseudo);
		
		if(nbPseudo == 1) {
			be.add("Le pseudo existe déja");
			return false;
		}
		return nbPseudo == 0;
	}

	private boolean validateConfirmPassword(User user, String confirmPassword, BusinessException be) {
		if(!user.getPassword().equals(confirmPassword)) {
			be.add("Le mot de passe est différent");
			return false;
		}
		return true;
	}
	
	
	@Override
	public User findByEmail(String emailUser) {
		User u = userDAO.findByEmail(emailUser);
		return u;
	}

	
	@Override
	public void update(User user) {
					PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
					String pwdEncoder = passwordEncoder.encode(user.getPassword());
					user.setPassword(pwdEncoder);
					userDAO.update(user);
	}
			

	@Override
	public void deleteAccountByEmail(String email) {
		userDAO.deleteAccountByEmail(email);
	}
	
}
