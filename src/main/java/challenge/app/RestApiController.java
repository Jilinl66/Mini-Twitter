/**
 * RESTful API controller
 * 
 * @author Jilin Liu
 */
package challenge.app;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import challenge.object.Followers;
import challenge.object.Tweet;

@RestController
@RequestMapping("/api")
public class RestApiController {
	
    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class); 
    		
	@Autowired
	private UserRepository userRepository;
	
	/* 1. read the message list for the current user */
	@RequestMapping(value = "/tweets", method = RequestMethod.GET)
	public ResponseEntity<List<Tweet>> readUserMessage(@RequestParam(value = "keyword", required = false) String keyword) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int person_id = userRepository.retrieveUserId(authUser.getUsername());
		logger.info("fetching tweet messages with person_id {}, keyword {}", person_id, keyword);
	    return new ResponseEntity<List<Tweet>>(userRepository.readUserMessageByKeyword(person_id, keyword), HttpStatus.OK);
	}

	/* 2. Endpoints to get the list of people the user is following as well as the followers of the user */
	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ResponseEntity<List<Followers>> findFollowers() {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int person_id = userRepository.retrieveUserId(authUser.getUsername());
		logger.info("fetching followers associate with id {}", person_id);
		return new ResponseEntity<List<Followers>>(userRepository.findFollowers(person_id), HttpStatus.OK);
	}
	
	/* 3. An endpoint to start following another user */
	@RequestMapping(value = "/followers", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void insertFollower(@RequestBody int followed_person_id) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int person_id = userRepository.retrieveUserId(authUser.getUsername());
		if(person_id == followed_person_id) {
			logger.info("Person id and follower id should be different");
			return;
		}
		if (userRepository.insertFollower(new Followers(followed_person_id, person_id)) == 0) {
			logger.info("Already exist following pair of person {} : follower {}", followed_person_id, person_id);
		}
		else {
			logger.info("Person {} started following person {}", person_id, followed_person_id);
		}
	}

	 /* 4. An endpoint to unfollow another user */
	@RequestMapping(value = "/unfollow", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFollower(@RequestBody int followed_person_id) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int person_id = userRepository.retrieveUserId(authUser.getUsername());
		if(person_id == followed_person_id) {
			logger.info("Follower person id should be different from user id");
			return;
		}
		if (userRepository.deleteFollower(new Followers(followed_person_id, person_id)) == 0) {
			logger.info("No such pair person and follower pair exist");
		}
		else {
			logger.info("Delete person {} following person {}", person_id, followed_person_id);
		}
	}
	
	/* 5. Shortest path */
	@RequestMapping(value = "/distance", method = RequestMethod.GET)
	public ResponseEntity<Integer> findShortestPath(@RequestParam(value = "person2") int person_id2) {
		User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int person_id = userRepository.retrieveUserId(authUser.getUsername());
		if(!userRepository.personExist(person_id) || !userRepository.personExist(person_id2)) {
			logger.info("Person does not exist");
			return new ResponseEntity<Integer>(0, HttpStatus.OK);
		}
		else if(person_id == person_id2) {
			logger.info("The Same person, distance is 0");
			return new ResponseEntity<Integer>(0, HttpStatus.OK);
		}
		else {
			logger.info("Compute shortest distance from person {} to person {}", person_id, person_id2);
			return new ResponseEntity<Integer>(userRepository.findShortestPath(person_id, person_id2), HttpStatus.OK);
		}
	}
	
	/* 6. Find a list of all users, paired with their most "popular" follower */
	@RequestMapping(value = "/popular", method = RequestMethod.GET)
	public ResponseEntity<List<Followers>> findMostPopularFollower() {
		logger.info("Fetching the most popular followers pairs");
		return new ResponseEntity<List<Followers>>(userRepository.findMostPopularFollower(), HttpStatus.OK);	
	}
}
