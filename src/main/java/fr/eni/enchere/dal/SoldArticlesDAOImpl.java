package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
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

	private final String INSERT =
					"""
					INSERT INTO SOLD_ARTICLES (nameArticle, description, startDateAuctions, endDateAuctions, initialPrice, picture, idUser, idCategory)
					VALUES (:nameArticle, :description, :startDateAuctions, :endDateAuctions, :initialPrice, :picture, :idUser, :idCategory)
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
	private static final String UPDATE_ARTICLES = """
						UPDATE SOLD_ARTICLES
						SET nameArticle = :nameArticle,
							description = :description,
							startDateAuctions = :startDateAuctions,
							endDateAuctions = :endDateAuctions,
							initialPrice = :initialPrice,
							picture = :picture
						WHERE idArticle = :idArticle
					""";
	private static final String DELETE_ARTICLES = "  DELETE from SOLD_ARTICLES where idArticle = :idArticle";
	private static final String DELETE_EXPIRED_ARTICLES = """
						DELETE FROM SOLD_ARTICLES WHERE endDateAuctions < CURRENT_DATE
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
		nameParameters.addValue("picture", soldArticles.getPicture());
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
	public void update(SoldArticles soldArticles) {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("nameArticle", soldArticles.getNameArticle());
		params.addValue("description", soldArticles.getDescription());
		params.addValue("startDateAuctions", soldArticles.getStartDateAuctions());
		params.addValue("endDateAuctions", soldArticles.getEndDateAuctions());
		params.addValue("initialPrice", soldArticles.getInitialPrice());
		params.addValue("picture", soldArticles.getPicture());
		params.addValue("idArticle", soldArticles.getIdArticle());
		namedParameterJdbcTemplate.update(UPDATE_ARTICLES, params);
	}


	@Override
	public List<SoldArticles> searchByName(String searchArticleName) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("searchArticleName", "%" + searchArticleName.toLowerCase() + "%");

		return namedParameterJdbcTemplate.query(SEARCH_BY_NAME, nameParameters, new SoldArticlesRowMapper());
	}

	@Override
	public void deleteArticleById(String idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", idArticle);
		namedParameterJdbcTemplate.update(DELETE_ARTICLES, nameParameters);
	}

	@Override
	public void deleteExpiredArticles() {
		namedParameterJdbcTemplate.update(DELETE_EXPIRED_ARTICLES, new MapSqlParameterSource());
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
			soldArticles.setPicture(rs.getString("picture"));
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