/**
 * Class Followers
 * 
 * @author Jilin Liu
 */
package challenge.object;

public class Followers {
	Integer id;
	Integer person_id;
	Integer follower_person_id;
	
	public Followers(int person_id, int follower_person_id) {
		this.person_id = person_id;
		this.follower_person_id = follower_person_id;
	}
	
	public Followers(int id, int person_id, int follower_person_id) {
		super();
		this.id = id;
		this.person_id = person_id;
		this.follower_person_id = follower_person_id;
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getPerson_id() {
		return person_id;
	}
	public Integer getFollower_person_id() {
		return follower_person_id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPerson_id(Integer person_id) {
		this.person_id = person_id;
	}
	public void setFollower_person_id(Integer follower_person_id) {
		this.follower_person_id = follower_person_id;
	}
	
}
