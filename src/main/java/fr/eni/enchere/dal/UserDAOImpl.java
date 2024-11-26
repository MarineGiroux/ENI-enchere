package fr.eni.enchere.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{

	private final String INSERT = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, codePostal, ville, motDePasse, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse, :credit, :administrateur)";
	private final String INSERT_ROLE = "INSERT INTO ROLES (email, role) VALUES (:email, :role)";

	private final String FIND_BY_NUM = "select * from UTILISATEURS where noUtilisateur = :noUtilisateur";
	private static final String COUNT_EMAIL = "SELECT COUNT(email) FROM UTILISATEURS WHERE email = :email";
	private static final String COUNT_PSEUDO = "SELECT COUNT(pseudo) FROM UTILISATEURS WHERE pseudo = :pseudo";
	private static final String FIND_BY_EMAIL = "SELECT * from UTILISATEURS where email = :email";
	private static final String UPDATE_USERS = "  UPDATE UTILISATEURS SET pseudo = :pseudo, nom= :nom, prenom = :prenom, telephone = :telephone, rue = :rue, codePostal = :codePostal, ville = :ville, motDePasse = :motDePasse WHERE noUtilisateur = :noUtilisateur";
	private static final String UPDATE_CREDIT = "UPDATE UTILISATEURS SET credit = :credit WHERE noUtilisateur = :noUtilisateur";
	private static final String DELETE_USERS = "  DELETE from UTILISATEURS where email = :email";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(Utilisateur utilisateur) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("pseudo",utilisateur.getPseudo());
		nameParameters.addValue("nom",utilisateur.getNom());
		nameParameters.addValue("prenom",utilisateur.getPrenom());
		nameParameters.addValue("email",utilisateur.getEmail());
		nameParameters.addValue("telephone",utilisateur.getTelephone());
		nameParameters.addValue("rue",utilisateur.getRue());
		nameParameters.addValue("codePostal",utilisateur.getCodePostal());
		nameParameters.addValue("ville",utilisateur.getVille());
		nameParameters.addValue("motDePasse",utilisateur.getMotDePasse());
		nameParameters.addValue("credit",utilisateur.getCredit());
		nameParameters.addValue("administrateur",utilisateur.isAdministrateur());

		namedParameterJdbcTemplate.update(INSERT, nameParameters);
	}

	@Override
	public void createRole(Utilisateur utilisateur) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("email",utilisateur.getEmail());
		nameParameters.addValue("role","MEMBRE");

		namedParameterJdbcTemplate.update(INSERT_ROLE, nameParameters);
	}



	@Override
	public Utilisateur findByNum(int noUtilisateur) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noUtilisateur", noUtilisateur);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NUM, nameParameters,new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public Utilisateur findByEmail(String email) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("email", email);
		System.out.println("email = " + email);

		try {
			return namedParameterJdbcTemplate.queryForObject(FIND_BY_EMAIL, nameParameters,
					new BeanPropertyRowMapper<Utilisateur>(Utilisateur.class));
		} catch (DataAccessException e) {
			return null;
		}

	}

	@Override
	public int countEmail(String email) {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("email", email);

		return namedParameterJdbcTemplate.queryForObject(COUNT_EMAIL, mapSqlParameterSource, Integer.class);
	}

	@Override
	public int countPseudo(String pseudo) {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("pseudo", pseudo);

		return namedParameterJdbcTemplate.queryForObject(COUNT_PSEUDO, mapSqlParameterSource, Integer.class);
	}

	@Override
	public void update(Utilisateur user) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("prenom", user.getPrenom());
		mapSqlParameterSource.addValue("nom", user.getNom());
		mapSqlParameterSource.addValue("pseudo", user.getPseudo());
		mapSqlParameterSource.addValue("ville", user.getVille());
		mapSqlParameterSource.addValue("rue", user.getRue());
		mapSqlParameterSource.addValue("codePostal", user.getCodePostal());
		mapSqlParameterSource.addValue("telephone", user.getTelephone());
		mapSqlParameterSource.addValue("noUtilisateur", user.getNoUtilisateur());
		mapSqlParameterSource.addValue("motDePasse", user.getMotDePasse());
		namedParameterJdbcTemplate.update(UPDATE_USERS, mapSqlParameterSource);
	}
	@Override
	public void updateCredit(int montant, Utilisateur utilisateur) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("credit", montant);
		mapSqlParameterSource.addValue("noUtilisateur", utilisateur.getNoUtilisateur());
		namedParameterJdbcTemplate.update(UPDATE_CREDIT, mapSqlParameterSource);

	}

	@Override
	public void deleteAccountByEmail(String email) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("email",email);
		System.out.println("suppr email" + email);


		namedParameterJdbcTemplate.update(DELETE_USERS, nameParameters);


	}


}
