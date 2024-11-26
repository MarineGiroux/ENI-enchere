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
	public Auctions findBynoUtilisateur(int noArticle, int noUtilisateur) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		nameParameters.addValue("noUtilisateur", noUtilisateur);
		nameParameters.addValue("noArticle", noArticle);
		return namedParameterJdbcTemplate.queryForObject(FIND_BY_noArticle_noUtilisateur, nameParameters, new BeanPropertyRowMapper<Auctions>(Auctions.class));
	}

	@Override
	public void create(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();	
		
		nameParameters.addValue("noUtilisateur", auctions.getUtilisateur().getIdUser());
		nameParameters.addValue("noArticle", auctions.getItemSold().getIdArticle());
		nameParameters.addValue("dateEnchere", auctions.getDateAuctions());
		nameParameters.addValue("montantEnchere", auctions.getAmountAuctions());
		
		namedParameterJdbcTemplate.update(INSERT, nameParameters);
	}

	@Override
	public List<Auctions> findAll() {
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
	public void surencherir(Auctions auctions) {
		MapSqlParameterSource nameParameters = new MapSqlParameterSource();
		nameParameters.addValue("noUtilisateur", auctions.getUtilisateur().getIdUser());
		nameParameters.addValue("montantEnchere", auctions.getAmountAuctions());
		nameParameters.addValue("dateEnchere", auctions.getDateAuctions());
		nameParameters.addValue("noArticle", auctions.getItemSold().getIdArticle());
		
		namedParameterJdbcTemplate.update(UPDATE, nameParameters);
		
	}

	@Override
	public List<Auctions> findByArticle(int noArticle) {
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
class EnchereRowMapper implements org.springframework.jdbc.core.RowMapper<Auctions>{

	@Override
	public Auctions mapRow(ResultSet rs, int rowNum) throws SQLException {
		Auctions auctions = new Auctions();
		auctions.setAmountAuctions(rs.getInt("montantEnchere"));
		auctions.setDateAuctions(rs.getDate("dateEnchere").toLocalDate());
		
		User user = new User();
		user.setNoUtilisateur(rs.getInt("noUtilisateur"));
		auctions.setUtilisateur(user);
		
		ItemSold itemSold = new ItemSold();
		itemSold.setIdArticle(rs.getInt("noArticle"));
		auctions.setArticleVendu(itemSold);
		
		return auctions;
	}
	
	
}
































