package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PickUpDAOImpl implements PickUpDAO {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


	private final String INSERT = """
									INSERT INTO PICKUP(idArticle, road, zipPass, city)
									VALUES (:idArticle, :road, :zipPass, :city)
								""";
	private final String FIND_BY_ID_ARTICLE = """
									SELECT * FROM PICKUP WHERE idArticle = :idArticle
								""";

	@Override
	public void create(PickUp pickUp) {

		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("road", pickUp.getRoad());
		nameParameters.addValue("zipPass", pickUp.getZipPass());
		nameParameters.addValue("city", pickUp.getCity());
		nameParameters.addValue("idArticle", pickUp.getIdArticle());

		namedParameterJdbcTemplate.update(INSERT, nameParameters);

	}

	@Override
	public PickUp findByIdArticle(int idArticle) {
		MapSqlParameterSource namMapSqlParameterSource = new MapSqlParameterSource();
		namMapSqlParameterSource.addValue("idArticle", idArticle);

		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID_ARTICLE, namMapSqlParameterSource, new BeanPropertyRowMapper<>(PickUp.class));
	}

}
