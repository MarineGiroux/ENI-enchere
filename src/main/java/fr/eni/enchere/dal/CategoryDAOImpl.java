package fr.eni.enchere.dal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
	
	private final String INSERT = "INSERT INTO CATEGORIES (libelle) VALUES (:libelle)";
	private final String FIND_BY_NUM = "select * from CATEGORIES where noCategorie = :noCategorie";
	private final String Find_All = "select * from CATEGORIES";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Categorie findByNum(int noCategorie) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noCategorie", noCategorie);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NUM, nameParameters, new BeanPropertyRowMapper<>(Categorie.class));
	}

	@Override
	public void create(Categorie categorie) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("libelle", categorie.getLibelle());
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters);	
	}
	
	@Override
	public List<Categorie> FindAll() {
		return namedParameterJdbcTemplate.query(Find_All, new BeanPropertyRowMapper<>(Categorie.class));
	}

}
