package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.ItemSold;
import fr.eni.enchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ItemSoldDAOImpl implements ItemSoldDAO {
	
	private final String INSERT = "INSERT INTO ITEMSOLD (idArticle,description,startDateAuctions,endDateAuctions,initialPrice,priceSale,idUser,idCategory) VALUES ( :idArticle, :description, :startDateAuctions, :endDateAuctions, :initialPrice, :priceSale, :idUser, :idCategory)";
	private final String Find_All = "select * from ITEMSOLD";
	private final String FIND_BY_CATEGORIE = "SELECT * FROM ITEMSOLD WHERE idCategory = :idCategory";
	private final String FIND_BY_NO = "SELECT * FROM ITEMSOLD WHERE idArticle = :idArticle";
	private final String UPDATE_PRIX_VENTE = "UPDATE ITEMSOLD SET prixVente = :prixVente where idArticle = :idArticle";
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	

	@Override
	public void create(ItemSold itemSold) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		nameParameters.addValue("nomArticle", itemSold.getName());
		nameParameters.addValue("description", itemSold.getDescription());
		nameParameters.addValue("startDateAuctions", itemSold.getStartDateAuctions());
		nameParameters.addValue("endDateAuctions", itemSold.getEndDateAuctions());
		nameParameters.addValue("initialPrice", itemSold.getInitialPrice());
		nameParameters.addValue("priceSale", itemSold.getPriceSale());
		nameParameters.addValue("idUser", itemSold.getSell().getIdUser());
		nameParameters.addValue("idCategory", itemSold.getCartegoryArticle().getIdCategory());
		
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters, keyHolder);
		
		if (keyHolder != null && keyHolder.getKey() != null) {
			itemSold.setIdArticle(keyHolder.getKey().intValue());
		}
	}

	

	@Override
	public List<ItemSold> FindAll() {
		return namedParameterJdbcTemplate.query(Find_All, new ArticleVenduRowMapper());
	}

	@Override
	public List<ItemSold> findByCategory(int idCategory) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idCategory", idCategory);
		
		
		return namedParameterJdbcTemplate.query(FIND_BY_CATEGORIE,nameParameters, new ArticleVenduRowMapper());
	}

	@Override
	public ItemSold findByNum(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", idArticle);
		
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_NO, nameParameters, new ArticleVenduRowMapper());
	}



	@Override
	public void updatePriceSale(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", auctions.getItemSold().getIdArticle());
		nameParameters.addValue("priceSale", auctions.getAmountAuctions());
		
		namedParameterJdbcTemplate.update(UPDATE_PRIX_VENTE, nameParameters);
		
		
	}
	
	

}
class ArticleVenduRowMapper implements RowMapper<ItemSold>{

	@Override
	public ItemSold mapRow(ResultSet rs, int rowNum) throws SQLException {
		ItemSold itemSold = new ItemSold();
		
		itemSold.setIdArticle(rs.getInt("idArticle"));
		itemSold.setName(rs.getString("nomArticle"));
		itemSold.setDescription(rs.getString("description"));
		itemSold.setStartDateAuctions(rs.getDate("startDateAuctions").toLocalDate());
		itemSold.setEndDateAuctions(rs.getDate("endDateAuctions").toLocalDate());
		itemSold.setInitialPrice(rs.getInt("initialPrice"));
		itemSold.setPriceSale(rs.getInt("priceSale"));
		
		User vendeur = new User();
		vendeur.setIdUser(rs.getInt("idUser"));
		itemSold.setSell(vendeur);
		
//		Utilisateur achete = new Utilisateur();
//		if (itemSold.getAchete() != null) {
//			achete.setidUser(rs.getInt("noAcheteur"));
//			itemSold.setAchete(achete);
//		}
//		
		itemSold.setBuy(null);
		
		Category category = new Category();
		category.setIdCategory(rs.getInt("idCategory"));
		itemSold.setCartegoryArticle(category);

		return itemSold;
	}
	
}




































