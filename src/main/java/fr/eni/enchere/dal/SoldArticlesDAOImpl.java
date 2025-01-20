package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class SoldArticlesDAOImpl implements SoldArticlesDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(SoldArticlesDAOImpl.class);

	private final Clock clock;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public SoldArticlesDAOImpl(Clock clock, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.clock = clock;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	private final String INSERT =
					"""
					INSERT INTO SOLD_ARTICLES (nameArticle, description, startDateAuctions, endDateAuctions, initialPrice, saleStatus, picture, idUser, idCategory)
					VALUES (:nameArticle, :description, :startDateAuctions, :endDateAuctions, :initialPrice, 0, :picture, :idUser, :idCategory)
					""";
	private final String FIND_All = "select * from SOLD_ARTICLES";
	private final String FIND_BY_CATEGORIE = """
					SELECT * FROM SOLD_ARTICLES WHERE idCategory = :idCategory
					""";
	private final String FIND_BY_NO = "SELECT * FROM SOLD_ARTICLES WHERE idArticle = :idArticle";
	private final String UPDATE_PRICE_SALE = "UPDATE SOLD_ARTICLES SET priceSale = :priceSale where idArticle = :idArticle";
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

	private static final String FIND_OUTDATED_SOLD_ARTICLES_FOR_USER = """
				SELECT * FROM SOLD_ARTICLES
				WHERE saleStatus = 0
				AND idUser = :idUser
				AND endDateAuctions < :outdated
			""";

	private static final String UPDATE_CLOSE_SALE_STATUS = """
				UPDATE SOLD_ARTICLES
				SET saleStatus = 1
				WHERE idArticle = :idArticle
			""";

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
		nameParameters.addValue("idArticle", auctions.getSoldArticles().getIdArticle());
		nameParameters.addValue("priceSale", auctions.getAmountAuctions());

		namedParameterJdbcTemplate.update(UPDATE_PRICE_SALE, nameParameters);
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
	public int closeOutdatedAuctionsAndGetCreditAmount(int idUser) {
		final MapSqlParameterSource nameParametersFind = new MapSqlParameterSource();
		nameParametersFind.addValue("idUser", idUser);
		nameParametersFind.addValue("outdated", LocalDate.now(clock));
		final List<SoldArticles> query = namedParameterJdbcTemplate.query(FIND_OUTDATED_SOLD_ARTICLES_FOR_USER, nameParametersFind, new SoldArticlesRowMapper());

		int creditAmount = 0;

		if (!query.isEmpty()) {
			for (SoldArticles soldArticles : query) {
				creditAmount += soldArticles.getPriceSale();
				MapSqlParameterSource nameParametersUpdate = new MapSqlParameterSource();
				nameParametersUpdate.addValue("idArticle", soldArticles.getIdArticle());
				namedParameterJdbcTemplate.update(UPDATE_CLOSE_SALE_STATUS, nameParametersUpdate);
			}
		}

		return creditAmount;
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