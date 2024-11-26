package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAOImpl implements CategoryDAO {
	
	private final String INSERT = "INSERT INTO CATEGORIES (wording) VALUES (:wording)";
	private final String FIND_BY_NUM = "select * from CATEGORIES where idCategory = :idCategory";
	private final String Find_All = "select * from CATEGORIES";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Category findByNum(int idCategory) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idCategory", idCategory);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NUM, nameParameters, new BeanPropertyRowMapper<>(Category.class));
	}

	@Override
	public void create(Category category) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("wording", category.getWording());
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters);	
	}
	
	@Override
	public List<Category> FindAll() {
		return namedParameterJdbcTemplate.query(Find_All, new BeanPropertyRowMapper<>(Category.class));
	}

}
