/**
 * userRepository test
 * 
 * @author Jilin Liu
 */
package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import challenge.app.ChallengeApplication;
import challenge.app.UserRepository;
import challenge.object.*;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ChallengeApplication.class)
public class ChallengeApplicationTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void readUserMessageTest() {
		List<Tweet> tweets = userRepository.readUserMessageByKeyword(1, "aliquet");
		assertNotNull(tweets);
		assertTrue(!tweets.isEmpty());
		assertTrue(tweets.size() == 9);
	}
	
	@Test
	public void findFollowersteTest() {
		List<Followers> followers = userRepository.findFollowers(1);
		assertNotNull(followers);
		assertTrue(followers.size() == 11);
	}
	
	@Test
	public void insertFollowerTest() {
		Followers follow = new Followers(1, 2);
		int affectRows = userRepository.insertFollower(follow);
		assertNotNull(affectRows);
		assertTrue(affectRows == 1);
	}
	
	@Test
	public void deleleFollowerTest() {
		Followers follow = new Followers(1, 2);
		int affectRows = userRepository.deleteFollower(follow);
		System.err.println(affectRows);
		assertNotNull(affectRows);
		assertTrue(affectRows == 0);
	}
	
	@Test
	public void findShortestPathTest() {
		int dis = userRepository.findShortestPath(1, 3);
		assertNotNull(dis);
		assertTrue(dis == 2);
		dis = userRepository.findShortestPath(1, 2);
		assertNotNull(dis);
		assertTrue(dis == 1);
	}
	
	@Test
	public void findMostPopularFollowerTest() {
		List<Followers> followList = userRepository.findMostPopularFollower();
		assertNotNull(followList);
		assertTrue(followList.size() == 18);
	}
	
	@Test
	public void retrieveUserIdTest() {
		int id = userRepository.retrieveUserId("");
		assertTrue(id == 1);
	}
}
