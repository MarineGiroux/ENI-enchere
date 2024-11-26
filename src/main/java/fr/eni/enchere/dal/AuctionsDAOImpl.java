package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import fr.eni.enchere.bo.Auctions;
import fr.eni.enchere.bo.ItemSold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.User;
@Repository
public class AuctionsDAOImpl implements AuctionsDAO {
	
	private final String INSERT = "INSERT INTO AUCTIONS (idUser,idArticle,dateAuctions,amountAuctions)VALUES (:idUser,:idArticle,:dateAuctions,:amountAuctions)";
	private final String FIND_ALL = "Select * FROM AUCTIONS";
	private final String FIND_BY_idArticle_idUser = "Select * FROM AUCTIONS WHERE idArticle = :idArticle AND idUser = :idUser";
	private final String COUNT_idArticle_idUser = "SELECT COUNT(idArticle) FROM AUCTIONS WHERE idArticle = :idArticle AND idUser = :idUser";
	private final String COUNT_idArticle = "SELECT COUNT(idArticle) FROM AUCTIONS WHERE idArticle = :idArticle";
	private final String UPDATE = "update AUCTIONS SET idUser = :idUser, amountAuctions = :amountAuctions, dateAuctions = :dateAuctions WHERE idArticle = :idArticle";
	private final String FIND_BY_idArticle = "Select * FROM AUCTIONS WHERE idArticle = :idArticle";
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
		nameParameters.addValue("idArticle", auctions.getItemSold().getIdArticle());
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
		nameParameters.addValue("idUser", auctions.getUser().getIdUser());
		nameParameters.addValue("amountAuctions", auctions.getAmountAuctions());
		nameParameters.addValue("dateAuctions", auctions.getDateAuctions());
		nameParameters.addValue("idArticle", auctions.getItemSold().getIdArticle());
		
		namedParameterJdbcTemplate.update(UPDATE, nameParameters);
		
	}

	@Override
	public List<Auctions> findByArticle(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle", idArticle);
		
		return namedParameterJdbcTemplate.query(FIND_BY_idArticle, nameParameters,new EnchereRowMapper());
	}

	@Override
	public int countAuction(int idArticle) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noArticle" , idArticle);
		return namedParameterJdbcTemplate.queryForObject(COUNT_idArticle, nameParameters, Integer.class);
	}



}
class EnchereRowMapper implements org.springframework.jdbc.core.RowMapper<Auctions>{

	@Override
	public Auctions mapRow(ResultSet rs, int rowNum) throws SQLException {
		Auctions auctions = new Auctions();
		auctions.setAmountAuctions(rs.getInt("amountAuctions"));
		auctions.setDateAuctions(rs.getDate("dateAuctions").toLocalDate());
		
		User user = new User();
		user.setIdUser(rs.getInt("idUser"));
		auctions.setUser(user);
		
		ItemSold itemSold = new ItemSold();
		itemSold.setIdArticle(rs.getInt("idArticle"));
		auctions.setItemSold(itemSold);
		
		return auctions;
	}
	
	
}