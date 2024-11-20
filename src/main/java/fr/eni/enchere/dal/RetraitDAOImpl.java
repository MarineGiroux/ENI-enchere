package fr.eni.enchere.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Retrait;
@Repository
public class RetraitDAOImpl implements RetraitDAO{
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	
	private final String INSERT = "INSERT INTO RETRAITS(noArticle, rue, codePostal, ville) VALUES (:noArticle, :rue, :codePostal, :ville)";
	private final String FIND_BY_ID ="SELECT * FROM RETRAITS WHERE noArticle = :noArticle";

	@Override
	public void create(Retrait retrait) {

		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("rue",retrait.getRue());
		nameParameters.addValue("codePostal",retrait.getCodePostal());
		nameParameters.addValue("ville",retrait.getVille());
		nameParameters.addValue("noArticle",retrait.getNoArticle());

		namedParameterJdbcTemplate.update(INSERT, nameParameters);

	}

	@Override
	public Retrait findByNum(int noArticle) {
		MapSqlParameterSource namMapSqlParameterSource = new MapSqlParameterSource();
		namMapSqlParameterSource.addValue("noArticle",noArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_ID, namMapSqlParameterSource, new BeanPropertyRowMapper<Retrait>(Retrait.class));
	}

}
