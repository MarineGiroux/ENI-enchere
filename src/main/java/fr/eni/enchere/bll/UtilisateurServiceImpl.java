package fr.eni.enchere.bll;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.UtilisateurDAO;
import fr.eni.enchere.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService{

	private UtilisateurDAO utilisateurDAO;
	
	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}
	

	@Override
	@Transactional
	public void add(Utilisateur utilisateur, String confirmMdP) throws BusinessException {
		BusinessException be = new BusinessException();
		boolean valid = validerUniqueEmail(utilisateur.getEmail(), be);
				valid &= validerUniquePseudo(utilisateur.getPseudo(), be);
				valid &= validerConfirmMdP(utilisateur, confirmMdP, be);
		
		if(valid) {
			PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			String pwdEncoder = passwordEncoder.encode(utilisateur.getMotDePasse());
			utilisateur.setMotDePasse(pwdEncoder);
			System.out.println("mdp = " + pwdEncoder);
			utilisateurDAO.create(utilisateur);		
			utilisateurDAO.createRole(utilisateur);
		}else {
			throw be;
		}
	}
	
	
	private boolean validerUniqueEmail(String email, BusinessException be) {
		int nbEmail = utilisateurDAO.countEmail(email);
		
		if(nbEmail == 1) {
			be.add("Email ou mot de passe incorrect");
			return false;
		}
		
		return nbEmail == 0;
	}

	private boolean validerUniquePseudo(String pseudo, BusinessException be) {
		int nbPseudo = utilisateurDAO.countPseudo(pseudo);
		
		if(nbPseudo == 1) {
			be.add("Le pseudo existe déja");
			return false;
		}
		return nbPseudo == 0;
	}
	
	private boolean validerConfirmMdP(Utilisateur utilisateur, String ConfirmMdP, BusinessException be) {
		if(!utilisateur.getMotDePasse().equals(ConfirmMdP)) {
			be.add("Le mot de passe est différent");
			System.out.println("confirmMdP = " + ConfirmMdP);
			return false;
		}
		return true;
	}
	
	
	@Override
	public Utilisateur findByEmail(String emailUtilisateur) {
		// Il nous faut l'utilisateur et les informations associées
		Utilisateur u = utilisateurDAO.findByEmail(emailUtilisateur);		
		return u;
	}

	
	@Override
	public void update(Utilisateur utilisateur) {		
					PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
					String pwdEncoder = passwordEncoder.encode(utilisateur.getMotDePasse());
					utilisateur.setMotDePasse(pwdEncoder);
					System.out.println("mdp = " + pwdEncoder);
					utilisateurDAO.update(utilisateur);
	}
			

	@Override
	public void deleteAccountByEmail(String email) {
		utilisateurDAO.deleteAccountByEmail(email);
	}
	
}
