package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.Category;
import fr.eni.enchere.bo.SoldArticles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SoldArticlesDAOImpl implements SoldArticlesDAO {

	private final String INSERT =
					"""
					INSERT INTO SOLD_ARTICLES (nameArticle, description, startDateAuctions, endDateAuctions, initialPrice, idUser, idCategory)
					VALUES (:nameArticle, :description, :startDateAuctions, :endDateAuctions, :initialPrice, :idUser, :idCategory)
					""";
	private final String FIND_All = "select * from SOLD_ARTICLES";
	private final String FIND_BY_CATEGORIE = """
					SELECT * FROM SOLD_ARTICLES WHERE idCategory = :idCategory
					""";
	private final String FIND_BY_NO = "SELECT * FROM SOLD_ARTICLES WHERE idArticle = :idArticle";
	private final String UPDATE_PRIX_VENTE = "UPDATE SOLD_ARTICLES SET prixVente = :prixVente where idArticle = :idArticle";
	private static final String SEARCH_BY_NAME = """
					SELECT * FROM SOLD_ARTICLES WHERE lower(nameArticle) LIKE :searchArticleName;
					""";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



	@Override
	public SoldArticles create(SoldArticles soldArticles) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();


		KeyHolder keyHolder = new GeneratedKeyHolder();

		nameParameters.addValue("nameArticle", soldArticles.getNameArticle());
		nameParameters.addValue("description", soldArticles.getDescription());
		nameParameters.addValue("startDateAuctions", soldArticles.getStartDateAuctions());
		nameParameters.addValue("endDateAuctions", soldArticles.getEndDateAuctions());
		nameParameters.addValue("initialPrice", soldArticles.getInitialPrice());
		nameParameters.addValue("idUser", soldArticles.getIdUser());
		nameParameters.addValue("idCategory", soldArticles.getIdCategory());

		namedParameterJdbcTemplate.update(INSERT, nameParameters, keyHolder);

		return this.findByNum(keyHolder.getKey().intValue());
	}



	@Override
	public List<SoldArticles> findAll() {
		return namedParameterJdbcTemplate.query(FIND_All, new SoldArticlesRowMapper());
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
		nameParameters.addValue("idArticle", auctions.getSoldArticle().getIdArticle());
		nameParameters.addValue("priceSale", auctions.getAmountAuctions());

		namedParameterJdbcTemplate.update(UPDATE_PRIX_VENTE, nameParameters);


	}

	@Override
	public List<SoldArticles> searchByName(String searchArticleName) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("searchArticleName", "%" + searchArticleName.toLowerCase() + "%");

		return namedParameterJdbcTemplate.query(SEARCH_BY_NAME, nameParameters, new SoldArticlesRowMapper());
	}


	static class SoldArticlesRowMapper implements RowMapper<SoldArticles> {

		@Override
		public SoldArticles mapRow(ResultSet rs, int rowNum) throws SQLException {
			SoldArticles soldArticles = new SoldArticles();

			soldArticles.setIdArticle(rs.getInt("idArticle"));
			soldArticles.setNameArticle(rs.getString("nameArticle"));
			soldArticles.setDescription(rs.getString("description"));
			soldArticles.setStartDateAuctions(rs.getDate("startDateAuctions").toLocalDate());
			soldArticles.setEndDateAuctions(rs.getDate("endDateAuctions").toLocalDate());
			soldArticles.setInitialPrice(rs.getInt("initialPrice"));
			soldArticles.setPriceSale(rs.getInt("priceSale"));
			soldArticles.setSaleStatus(rs.getBoolean("saleStatus"));
			soldArticles.setIdUser(rs.getInt("idUser"));
            soldArticles.setIdCategory(rs.getInt("idCategory"));

            // TODO : il faut un objet "métier" qui permet d'avoir toutes les données vendeur/acheteur
			// User vendeur = new User();
			// vendeur.setIdUser(rs.getInt("idUser"));
			// soldArticles.setSell(vendeur);
			// soldArticles.setBuy(null);

			return soldArticles;
		}

	}

}