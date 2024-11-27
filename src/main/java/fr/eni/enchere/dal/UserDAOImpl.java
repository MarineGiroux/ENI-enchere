package fr.eni.enchere.dal;

import fr.eni.enchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

	private final String INSERT = "INSERT INTO USERS (pseudo, lastName, firstName, email, phone, road, zipPass, city, password, credit, idRole) VALUES (:pseudo, :lastName, :firstName, :email, :phone, :road, :zipPass, :city, :password, :credit, :idRole)";
	private final String INSERT_ROLE = "INSERT INTO ROLES (idUser, role) VALUES (:idUser, :role)";

	private final String FIND_BY_NUM = "select * from USERS where idUser = :idUser";
	private static final String COUNT_EMAIL = "SELECT COUNT(email) FROM USERS WHERE email = :email";
	private static final String COUNT_PSEUDO = "SELECT COUNT(pseudo) FROM USERS WHERE pseudo = :pseudo";
	private static final String FIND_BY_EMAIL = "SELECT * from USERS where email = :email";
	private static final String UPDATE_USERS = "  UPDATE USERS SET pseudo = :pseudo, lastName= :lastName, firstName = :firstName, phone = :phone, rue = :rue, zipPass = :zipPass, city = :city, password = :password WHERE idUser = :idUser";
	private static final String UPDATE_CREDIT = "UPDATE USERS SET credit = :credit WHERE idUser = :idUser";
	private static final String DELETE_USERS = "  DELETE from USERS where email = :email";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void create(User user) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("pseudo", user.getPseudo());
		nameParameters.addValue("lastName", user.getLastName());
		nameParameters.addValue("firstName", user.getFirstName());
		nameParameters.addValue("email", user.getEmail());
		nameParameters.addValue("phone", user.getPhone());
		nameParameters.addValue("road", user.getRoad());
		nameParameters.addValue("zipPass", user.getZipPass());
		nameParameters.addValue("city", user.getCity());
		nameParameters.addValue("password", user.getPassword());
		nameParameters.addValue("credit", user.getCredit());
		nameParameters.addValue("idRole", 2);
		namedParameterJdbcTemplate.update(INSERT, nameParameters, keyHolder, new String[]{"idUser"});

		int generatedId = keyHolder.getKey().intValue();
		user.setIdUser(generatedId);
	}

	@Override
	public User findByNum(int idUser) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idUser", idUser);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NUM, nameParameters,new BeanPropertyRowMapper<>(User.class));
	}

	@Override
	public User findByEmail(String idUser) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("email", idUser);
		System.out.println("email = " + idUser);

		try {
			return namedParameterJdbcTemplate.queryForObject(FIND_BY_EMAIL, nameParameters,
					new BeanPropertyRowMapper<User>(User.class));
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
	public void update(User user) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("firstName", user.getFirstName());
		mapSqlParameterSource.addValue("lastName", user.getLastName());
		mapSqlParameterSource.addValue("pseudo", user.getPseudo());
		mapSqlParameterSource.addValue("city", user.getCity());
		mapSqlParameterSource.addValue("road", user.getRoad());
		mapSqlParameterSource.addValue("zipPass", user.getZipPass());
		mapSqlParameterSource.addValue("phone", user.getPhone());
		mapSqlParameterSource.addValue("idUser", user.getIdUser());
		mapSqlParameterSource.addValue("password", user.getPassword());
		namedParameterJdbcTemplate.update(UPDATE_USERS, mapSqlParameterSource);
	}
	@Override
	public void updateCredit(int rising, User user) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("credit", rising);
		mapSqlParameterSource.addValue("idUser", user.getIdUser());
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
