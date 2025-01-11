package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.SoldArticles;
import fr.eni.enchere.bo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuctionsDAOImpl implements AuctionsDAO {
	
	private final String INSERT = "INSERT INTO AUCTIONS (idUser,idArticle,dateAuctions,amountAuctions)VALUES (:idUser,:idArticle,:dateAuctions,:amountAuctions)";
	private final String FIND_ALL = "Select * FROM AUCTIONS";
	private final String FIND_BY_idArticle_idUser = "Select * FROM AUCTIONS WHERE idArticle = :idArticle AND idUser = :idUser";
	private final String COUNT_idArticle_idUser = "SELECT COUNT(idArticle) FROM AUCTIONS WHERE idArticle = :idArticle AND idUser = :idUser";
	private final String UPDATE_AMOUNT_AUCTION = """
			update AUCTIONS SET amountAuctions = :amountAuctions, dateAuctions = :dateAuctions
			             WHERE idArticle = :idArticle and idUser = :idUser
			""";
	private final String FIND_BY_idArticle = "Select * FROM AUCTIONS WHERE idArticle = :idArticle";

	private final String FIND_BIGGER_AUCTION_OF_ARTICLE = """
				SELECT top 1 *
				FROM AUCTIONS WHERE idArticle = :idArticle
				ORDER BY amountAuctions desc;
			""";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public Auctions findByidUser(int idArticle, int idUser) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		nameParameters.addValue("idUser", idUser);
		nameParameters.addValue("noArticle", idArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_idArticle_idUser, nameParameters, new BeanPropertyRowMapper<Auctions>(Auctions.class));
	}

	@Override
	public void create(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		
		nameParameters.addValue("idUser", auctions.getUser().getIdUser());
		nameParameters.addValue("idArticle", auctions.getSoldArticles().getIdArticle());
		nameParameters.addValue("dateAuctions", auctions.getDateAuctions());
		nameParameters.addValue("amountAuctions", auctions.getAmountAuctions());
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters);
	}

	@Override
	public List<Auctions> findAll() {
		return namedParameterJdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
	}

	@Override
	public int countAuctionsUser(int idArticle, int idUser) {

		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idUser", idUser);
		nameParameters.addValue("idArticle" , idArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_idArticle_idUser, nameParameters, Integer.class);
	}

	@Override
	public void outbid(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("amountAuctions", auctions.getAmountAuctions());
		nameParameters.addValue("dateAuctions", auctions.getDateAuctions());
		nameParameters.addValue("idUser", auctions.getUser().getIdUser());
		nameParameters.addValue("idArticle", auctions.getSoldArticles().getIdArticle());
		
		namedParameterJdbcTemplate.update(UPDATE_AMOUNT_AUCTION, nameParameters);
		
	}

	@Override
	public List<Auctions> findByArticle(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", idArticle);
		
		return namedParameterJdbcTemplate.query(FIND_BY_idArticle, nameParameters,new EnchereRowMapper());
	}

	@Override
	public Auctions findBiggerAuction(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("idArticle", idArticle);

		List<Auctions> results = namedParameterJdbcTemplate.query(FIND_BIGGER_AUCTION_OF_ARTICLE, nameParameters, new EnchereRowMapper());
		if (results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

}

class EnchereRowMapper implements org.springframework.jdbc.core.RowMapper<Auctions>{

	@Override
	public Auctions mapRow(ResultSet rs, int rowNum) throws SQLException {
		Auctions auctions = new Auctions();
		auctions.setAmountAuctions(rs.getInt("amountAuctions"));
		auctions.setDateAuctions(rs.getDate("dateAuctions").toLocalDate());
		int idUser = rs.getInt("idUser");
		auctions.setIdUser(idUser);
		int idArticle = rs.getInt("idArticle");
		auctions.setIdArticle(idArticle);
		return auctions;
	}
	
	
}