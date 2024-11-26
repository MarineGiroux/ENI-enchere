package fr.eni.enchere.bll;

import fr.eni.enchere.bo.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.dal.UserDAO;
import fr.eni.enchere.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

	private UserDAO userDAO;
	
	public UtilisateurServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	

	@Override
	@Transactional
	public void add(User user, String confirmMdP) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valid = validerUniqueEmail(user.getEmail(), be);
				valid &= validerUniquePseudo(user.getPseudo(), be);
				valid &= validerConfirmMdP(user, confirmMdP, be);
		
		if(valid) {
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String pwdEncoder = passwordEncoder.encode(user.getPassWord());
			user.setPassWord(pwdEncoder);
			System.out.println("mdp = " + pwdEncoder);
			userDAO.create(user);
			userDAO.createRole(user);
		}else {
			throw be;
		}
	}
	
	
	private boolean validerUniqueEmail(String email, BusinessException be) {
		int nbEmail = userDAO.countEmail(email);
		
		if(nbEmail == 1) {
			be.add("Email ou mot de passe incorrect");
			return false;
		}
		
		return nbEmail == 0;
	}

	private boolean validerUniquePseudo(String pseudo, BusinessException be) {
		int nbPseudo = userDAO.countPseudo(pseudo);
		
		if(nbPseudo == 1) {
			be.add("Le pseudo existe déja");
			return false;
		}
		return nbPseudo == 0;
	}
	
	private boolean validerConfirmMdP(User user, String ConfirmMdP, BusinessException be) {
		if(!user.getPassWord().equals(ConfirmMdP)) {
			be.add("Le mot de passe est différent");
			System.out.println("confirmMdP = " + ConfirmMdP);
			return false;
		}
		return true;
	}
	
	
	@Override
	public User findByEmail(String emailUtilisateur) {
		// Il nous faut l'utilisateur et les informations associées
		User u = userDAO.findByEmail(emailUtilisateur);
		return u;
	}

	
	@Override
	public void update(User user) {
					PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
					String pwdEncoder = passwordEncoder.encode(user.getPassWord());
					user.setMotDePasse(pwdEncoder);
					System.out.println("mdp = " + pwdEncoder);
					userDAO.update(user);
	}
			

	@Override
	public void deleteAccountByEmail(String email) {
		userDAO.deleteAccountByEmail(email);
	}
	
}
