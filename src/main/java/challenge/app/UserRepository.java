/**
 * Create user repository to access database 
 * 
 * @author Jilin Liu
 */
package challenge.app;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import challenge.object.Followers;
import challenge.object.Person;
import challenge.object.Tweet;
@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/*1. read the message list for the current user*/
	@Transactional(readOnly = true)
	public List<Tweet> readUserMessageByKeyword(int person_id, String keyword) {
		String sql = "";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		if(keyword == null) {
			sql = "SELECT id, t2.person_id, content FROM tweet AS t2 JOIN "
					+ "(SELECT DISTINCT person_id FROM followers WHERE person_id=:person_id OR follower_person_id=:person_id) AS t1 "
					+ "ON t1.person_id = t2.person_id";
		}
		else {
			sql = "SELECT * FROM ("
				+ "SELECT id, t2.person_id, content FROM tweet AS t2 JOIN "
				+ "(SELECT DISTINCT person_id FROM followers WHERE person_id=:person_id OR follower_person_id=:person_id) AS t1 "
				+ "ON t1.person_id = t2.person_id )"
				+ "WHERE content LIKE :keyword";
			namedParameters.put("keyword", "%" + keyword + "%");
		}	
		namedParameters.put("person_id", person_id);
	    return namedParameterJdbcTemplate.query(sql, namedParameters, new TweetRowMapper());
	}
	
	/*2. Endpoints to get the list of people the user is following as well as the followers of the user*/
	@Transactional(readOnly = true)
	public List<Followers> findFollowers(int person_id) {
		String sql = "SELECT * FROM followers WHERE person_id = :person_id OR follower_person_id = :person_id";
		SqlParameterSource namedParameters = new MapSqlParameterSource("person_id", person_id);
		return namedParameterJdbcTemplate.query(sql, namedParameters, new FollowersRowMapper());
	}
	
	/*3. An endpoint to start following another user*/
	public int insertFollower(Followers following) {
		String sqlCheckExist = "SELECT * FROM followers WHERE person_id = :person_id AND follower_person_id = :follower_person_id";
		String sqlInsert = "INSERT INTO followers (person_id, follower_person_id) VALUES (:person_id, :follower_person_id)";
		Map<String, Integer> namedParameters = new HashMap<String, Integer>();
		namedParameters.put("person_id", following.getPerson_id());
		namedParameters.put("follower_person_id", following.getFollower_person_id());
		if (namedParameterJdbcTemplate.query(sqlCheckExist, namedParameters, new FollowersRowMapper()).size() != 0) {
			return 0;
		}
		else {
			return namedParameterJdbcTemplate.update(sqlInsert, namedParameters);
		}
	}
	
	/*4. An endpoint to unfollow another user*/
	public int deleteFollower(Followers following) {
		String sqlCheckExist = "SELECT * FROM followers WHERE person_id = :person_id AND follower_person_id = :follower_person_id";
		String sqlDel = "DELETE FROM followers WHERE person_id = :person_id AND follower_person_id = :follower_person_id";
		Map<String, Integer> namedParameters = new HashMap<String, Integer>();
		namedParameters.put("person_id", following.getPerson_id());
		namedParameters.put("follower_person_id", following.getFollower_person_id());
		if (namedParameterJdbcTemplate.query(sqlCheckExist, namedParameters, new FollowersRowMapper()).size() == 0) {
			return 0;
		}
		else {
			return namedParameterJdbcTemplate.update(sqlDel, namedParameters);
		}
	}
	
	/*5. Shortest path between people*/
	public int findShortestPath(int person_id1, int person_id2) {
		String sqlSelect = "SELECT * FROM followers";
		List<Followers> list = namedParameterJdbcTemplate.query(sqlSelect, new FollowersRowMapper());
		helperFunction helper = new helperFunction();
		return helper.shorestDist(list, person_id1, person_id2);
	}
	
	/*6. Find a list of all users, paired with their most "popular" follower*/
	public List<Followers> findMostPopularFollower() {
		String sql = "SELECT p.id, p.person_id, p.follower_person_id, p.cnt FROM "
				+ "(SELECT t1.id, t1.person_id, t1.follower_person_id, t2.cnt FROM "
				+ "followers AS t1 JOIN (SELECT person_id, COUNT(*) AS cnt FROM followers GROUP BY person_id) AS t2 "
				+ "ON t1.follower_person_id = t2.person_id) p INNER JOIN (SELECT person_id, MAX(cnt) AS maxcnt "
				+ "FROM (SELECT t1.person_id, t1.follower_person_id, t2.cnt FROM followers AS t1 "
				+ "JOIN (SELECT person_id, COUNT(*) AS cnt FROM followers GROUP BY person_id ) AS t2 "
				+ "ON t1.follower_person_id = t2.person_id) GROUP BY person_id) AS q "
				+ "ON p.person_id = q.person_id AND p.cnt = q.maxcnt";
		return namedParameterJdbcTemplate.query(sql, new FollowersRowMapper());
	}
	
	/*7.Check person exist*/
	public boolean personExist(int id) {
		String sqlSelectAll = "SELECT * FROM person WHERE id = :id";
		SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);
		if(namedParameterJdbcTemplate.query(sqlSelectAll, namedParameters, new PersonRowMapper()).size() == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/*8.Retrieve user id*/
	public int retrieveUserId(String username) {
		String sqlSelectUser = "SELECT * FROM person WHERE name = :name";
		SqlParameterSource namedParameters = new MapSqlParameterSource("name", username);
		List<Person> person = namedParameterJdbcTemplate.query(sqlSelectUser, namedParameters, new PersonRowMapper());
		return person.get(0).getId(); 
	}
		
	public List<Person> getAllPerson() {
		String sqlSelectUser = "SELECT * FROM person";
		return namedParameterJdbcTemplate.query(sqlSelectUser, new PersonRowMapper());
	}
	class TweetRowMapper implements RowMapper<Tweet> {
		
		@Override
		public Tweet mapRow(ResultSet rs, int rowNum) throws SQLException {
			Tweet tweet = new Tweet(rs.getInt("id"), rs.getInt("person_id"), rs.getString("content"));
			return tweet;
		}
	}
	
	class FollowersRowMapper implements RowMapper<Followers> {

		@Override
		public Followers mapRow(ResultSet rs, int rowNum) throws SQLException {
			Followers followers = new Followers(rs.getInt("id"), rs.getInt("person_id"), rs.getInt("follower_person_id"));
			return followers;
		}
	}
	
	class PersonRowMapper implements RowMapper<Person> {

		@Override
		public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person person = new Person(rs.getInt("id"), rs.getString("name"));
			return person;
		}	
	}
}
