package fr.eni.enchere.dal;

import fr.eni.enchere.bo.PickUp;
import fr.eni.enchere.bo.SoldArticles;
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
	private static final String UPDATE_PICKUP = """
						UPDATE PICKUP
						SET road = :road,
							zipPass = :zipPass,
							city = :city
						WHERE idArticle = :idArticle
					""";

	@Override
	public void update(PickUp pickUp) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("idArticle", pickUp.getIdArticle());
		params.addValue("road", pickUp.getRoad());
		params.addValue("zipPass", pickUp.getZipPass());
		params.addValue("city", pickUp.getCity());
		namedParameterJdbcTemplate.update(UPDATE_PICKUP, params);
	}

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
