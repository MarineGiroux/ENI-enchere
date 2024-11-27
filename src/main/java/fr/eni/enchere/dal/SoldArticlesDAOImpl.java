package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SoldArticlesDAOImpl implements SoldArticlesDAO {
	
	private final String INSERT = "INSERT INTO SOLD_ARTICLES (idArticle,description,startDateAuctions,endDateAuctions,initialPrice,priceSale,idUser,idCategory) VALUES ( :idArticle, :description, :startDateAuctions, :endDateAuctions, :initialPrice, :priceSale, :idUser, :idCategory)";
	private final String Find_All = "select * from SOLD_ARTICLES";
	private final String FIND_BY_CATEGORIE = "SELECT * FROM SOLD_ARTICLES WHERE idCategory = :idCategory";
	private final String FIND_BY_NO = "SELECT * FROM SOLD_ARTICLES WHERE idArticle = :idArticle";
	private final String UPDATE_PRIX_VENTE = "UPDATE SOLD_ARTICLES SET prixVente = :prixVente where idArticle = :idArticle";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	

	@Override
	public void create(SoldArticles soldArticles) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		nameParameters.addValue("nomArticle", soldArticles.getNameArticle());
		nameParameters.addValue("description", soldArticles.getDescription());
		nameParameters.addValue("startDateAuctions", soldArticles.getStartDateAuctions());
		nameParameters.addValue("endDateAuctions", soldArticles.getEndDateAuctions());
		nameParameters.addValue("initialPrice", soldArticles.getInitialPrice());
		nameParameters.addValue("priceSale", soldArticles.getPriceSale());
		nameParameters.addValue("idUser", soldArticles.getSell().getIdUser());
		nameParameters.addValue("idCategory", soldArticles.getCartegoryArticle().getIdCategory());
		
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters, keyHolder);
		
		if (keyHolder != null && keyHolder.getKey() != null) {
			soldArticles.setIdArticle(keyHolder.getKey().intValue());
		}
	}

	

	@Override
	public List<SoldArticles> FindAll() {
		return namedParameterJdbcTemplate.query(Find_All, new SoldArticlesRowMapper());
	}

	@Override
	public List<SoldArticles> findByCategory(int idCategory) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idCategory", idCategory);
		
		
		return namedParameterJdbcTemplate.query(FIND_BY_CATEGORIE,nameParameters, new SoldArticlesRowMapper());
	}

	@Override
	public SoldArticles findByNum(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", idArticle);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NO, nameParameters, new SoldArticlesRowMapper());
	}



	@Override
	public void updatePriceSale(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", auctions.getItemSold().getIdArticle());
		nameParameters.addValue("priceSale", auctions.getAmountAuctions());
		
		namedParameterJdbcTemplate.update(UPDATE_PRIX_VENTE, nameParameters);
		
		
	}
	
	

}
class SoldArticlesRowMapper implements RowMapper<SoldArticles>{

	@Override
	public SoldArticles mapRow(ResultSet rs, int rowNum) throws SQLException {
		SoldArticles soldArticles = new SoldArticles();
		
		soldArticles.setIdArticle(rs.getInt("idArticle"));
		soldArticles.setNameArticle(rs.getString("nomArticle"));
		soldArticles.setDescription(rs.getString("description"));
		soldArticles.setStartDateAuctions(rs.getDate("startDateAuctions").toLocalDate());
		soldArticles.setEndDateAuctions(rs.getDate("endDateAuctions").toLocalDate());
		soldArticles.setInitialPrice(rs.getInt("initialPrice"));
		soldArticles.setPriceSale(rs.getInt("priceSale"));
		
		User vendeur = new User();
		vendeur.setIdUser(rs.getInt("idUser"));
		soldArticles.setSell(vendeur);
		
//		Utilisateur achete = new Utilisateur();
//		if (itemSold.getAchete() != null) {
//			achete.setidUser(rs.getInt("noAcheteur"));
//			itemSold.setAchete(achete);
//		}
//		
		soldArticles.setBuy(null);
		
		Category category = new Category();
		category.setIdCategory(rs.getInt("idCategory"));
		soldArticles.setCartegoryArticle(category);

		return soldArticles;
	}
	
}




































