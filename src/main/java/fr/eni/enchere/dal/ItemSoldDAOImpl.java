package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Utilisateur;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO{
	
	private final String INSERT = "INSERT INTO ARTICLESVENDUS (nomArticle,description,dateDebutEncheres,dateFinEncheres,prixInitial,prixVente,noUtilisateur,noCategorie) VALUES ( :nomArticle, :description, :dateDebutEncheres, :dateFinEncheres, :prixInitial, :prixVente, :noUtilisateur, :noCategorie)";
	private final String Find_All = "select * from ARTICLESVENDUS";
	private final String FIND_BY_CATEGORIE = "SELECT * FROM ARTICLESVENDUS WHERE noCategorie = :noCategorie";
	private final String FIND_BY_NO = "SELECT * FROM ARTICLESVENDUS WHERE noArticle = :noArticle";
	private final String UPDATE_PRIX_VENTE = "UPDATE ARTICLESVENDUS SET prixVente = :prixVente where noArticle = :noArticle";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	

	@Override
	public void create(ArticleVendu articleVendu) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		nameParameters.addValue("nomArticle", articleVendu.getNomArticle());
		nameParameters.addValue("description", articleVendu.getDescription());
		nameParameters.addValue("dateDebutEncheres", articleVendu.getDateDebutEnchere());
		nameParameters.addValue("dateFinEncheres", articleVendu.getDateFinEnchere());
		nameParameters.addValue("prixInitial", articleVendu.getMiseAPrix());
		nameParameters.addValue("prixVente", articleVendu.getPrixVente());
		nameParameters.addValue("noUtilisateur", articleVendu.getVend().getNoUtilisateur());
		nameParameters.addValue("noCategorie", articleVendu.getCategorieArticle().getNoCategorie());
		
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters, keyHolder);
		
		if (keyHolder != null && keyHolder.getKey() != null) {
			articleVendu.setNoArticle(keyHolder.getKey().intValue());
		}
	}

	

	@Override
	public List<ArticleVendu> FindAll() {
		return namedParameterJdbcTemplate.query(Find_All, new ArticleVenduRowMapper());
	}

	@Override
	public List<ArticleVendu> findByCategorie(int noCategorie) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noCategorie", noCategorie);
		
		
		return namedParameterJdbcTemplate.query(FIND_BY_CATEGORIE,nameParameters, new ArticleVenduRowMapper());
	}

	@Override
	public ArticleVendu findByNum(int noArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle", noArticle);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NO, nameParameters, new ArticleVenduRowMapper());
	}



	@Override
	public void updatePrixVente(Enchere enchere) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle", enchere.getArticleVendu().getNoArticle());
		nameParameters.addValue("prixVente", enchere.getMontantEnchere());
		
		namedParameterJdbcTemplate.update(UPDATE_PRIX_VENTE, nameParameters);
		
		
	}
	
	

}
class ArticleVenduRowMapper implements RowMapper<ArticleVendu>{

	@Override
	public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
		ArticleVendu articleVendu = new ArticleVendu();
		
		articleVendu.setNoArticle(rs.getInt("noArticle"));
		articleVendu.setNomArticle(rs.getString("nomArticle"));
		articleVendu.setDescription(rs.getString("description"));
		articleVendu.setDateDebutEnchere(rs.getDate("dateDebutEncheres").toLocalDate());
		articleVendu.setDateFinEnchere(rs.getDate("dateFinEncheres").toLocalDate());
		articleVendu.setMiseAPrix(rs.getInt("prixInitial"));
		articleVendu.setPrixVente(rs.getInt("prixVente"));
		
		Utilisateur vendeur = new Utilisateur();
		vendeur.setNoUtilisateur(rs.getInt("noUtilisateur"));
		articleVendu.setVend(vendeur);
		
//		Utilisateur achete = new Utilisateur();
//		if (articleVendu.getAchete() != null) {
//			achete.setNoUtilisateur(rs.getInt("noAcheteur"));
//			articleVendu.setAchete(achete);
//		}
//		
		articleVendu.setAchete(null);
		
		Categorie categorie = new Categorie();
		categorie.setNoCategorie(rs.getInt("noCategorie"));
		articleVendu.setCategorieArticle(categorie);

		return articleVendu;
	}
	
}




































