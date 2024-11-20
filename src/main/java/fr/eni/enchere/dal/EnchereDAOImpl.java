package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Utilisateur;
@Repository
public class EnchereDAOImpl implements EnchereDAO{
	
	private final String INSERT = "INSERT INTO ENCHERES (noUtilisateur,noArticle,dateEnchere,montantEnchere)VALUES (:noUtilisateur,:noArticle,:dateEnchere,:montantEnchere)";
	private final String FIND_ALL = "Select * FROM ENCHERES";
	private final String FIND_BY_noArticle_noUtilisateur = "Select * FROM ENCHERES WHERE noArticle = :noArticle AND noUtilisateur = :noUtilisateur";
	private final String COUNT_NOARTICLE_noUtilisateur = "SELECT COUNT(noArticle) FROM ENCHERES WHERE noArticle = :noArticle AND noUtilisateur = :noUtilisateur";
	private final String COUNT_NOARTICLE = "SELECT COUNT(noArticle) FROM ENCHERES WHERE noArticle = :noArticle";
	private final String UPDATE = "update ENCHERES SET noUtilisateur = :noUtilisateur, montantEnchere = :montantEnchere, dateEnchere = :dateEnchere WHERE noArticle = :noArticle";
	private final String FIND_BY_noArticle = "Select * FROM ENCHERES WHERE noArticle = :noArticle";
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Override
	public Enchere findBynoUtilisateur(int noArticle,int noUtilisateur) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		nameParameters.addValue("noUtilisateur", noUtilisateur);
		nameParameters.addValue("noArticle", noArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_noArticle_noUtilisateur, nameParameters, new BeanPropertyRowMapper<Enchere>(Enchere.class));
	}

	@Override
	public void create(Enchere enchere) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		
		nameParameters.addValue("noUtilisateur", enchere.getUtilisateur().getNoUtilisateur());
		nameParameters.addValue("noArticle", enchere.getArticleVendu().getNoArticle());
		nameParameters.addValue("dateEnchere", enchere.getDateEnchere());
		nameParameters.addValue("montantEnchere", enchere.getMontantEnchere());
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters);
	}

	@Override
	public List<Enchere> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
	}

	@Override
	public int countEnchereUtilisateur(int noArticle,int noUtilisateur) {

		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noUtilisateur", noUtilisateur);
		nameParameters.addValue("noArticle" , noArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_NOARTICLE_noUtilisateur, nameParameters, Integer.class);
	}

	@Override
	public void surencherir(Enchere enchere) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noUtilisateur", enchere.getUtilisateur().getNoUtilisateur());
		nameParameters.addValue("montantEnchere", enchere.getMontantEnchere());
		nameParameters.addValue("dateEnchere", enchere.getDateEnchere());
		nameParameters.addValue("noArticle", enchere.getArticleVendu().getNoArticle());
		
		namedParameterJdbcTemplate.update(UPDATE, nameParameters);
		
	}

	@Override
	public List<Enchere> findByArticle(int noArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle", noArticle);
		
		return namedParameterJdbcTemplate.query(FIND_BY_noArticle, nameParameters,new EnchereRowMapper());
	}

	@Override
	public int countEnchere(int noArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle" , noArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_NOARTICLE, nameParameters, Integer.class);
	}



}
class EnchereRowMapper implements org.springframework.jdbc.core.RowMapper<Enchere>{

	@Override
	public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
		Enchere enchere = new Enchere();
		enchere.setMontantEnchere(rs.getInt("montantEnchere"));
		enchere.setDateEnchere(rs.getDate("dateEnchere").toLocalDate());
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNoUtilisateur(rs.getInt("noUtilisateur"));
		enchere.setUtilisateur(utilisateur);
		
		ArticleVendu articleVendu = new ArticleVendu();
		articleVendu.setNoArticle(rs.getInt("noArticle"));
		enchere.setArticleVendu(articleVendu);		
		
		return enchere;
	}
	
	
}
































