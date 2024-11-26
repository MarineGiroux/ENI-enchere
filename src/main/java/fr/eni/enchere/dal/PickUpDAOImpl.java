package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RetraitDAOImpl implements RetraitDAO{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	private final String INSERT = "INSERT INTO RETRAITS(noArticle, rue, codePostal, ville) VALUES (:noArticle, :rue, :codePostal, :ville)";
	private final String FIND_BY_ID ="SELECT * FROM RETRAITS WHERE noArticle = :noArticle";

	@Override
	public void create(PickUp pickUp) {

		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("rue", pickUp.getRoad());
		nameParameters.addValue("codePostal", pickUp.getZipPass());
		nameParameters.addValue("ville", pickUp.getCity());
		nameParameters.addValue("noArticle", pickUp.getIdArticle());

		namedParameterJdbcTemplate.update(INSERT, nameParameters);

	}

	@Override
	public PickUp findByNum(int noArticle) {
		MapSqlParameterSource namMapSqlParameterSource = new MapSqlParameterSource();
		namMapSqlParameterSource.addValue("noArticle",noArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namMapSqlParameterSource, new BeanPropertyRowMapper<PickUp>(PickUp.class));
	}

}
