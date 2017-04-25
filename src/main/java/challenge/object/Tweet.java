/**
 * Class Tweet
 * 
 * @author Jilin Liu
 */
package challenge.object;

public class Tweet {
	private Integer id;
	private Integer person_id;
	private String content;
	
	public Tweet(Integer id, Integer person_id, String content) {
		super();
		this.id = id;
		this.person_id = person_id;
		this.content = content;
	}
	public Integer getId() {
		return id;
	}
	public Integer getPerson_id() {
		return person_id;
	}
	public String getContent() {
		return content;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPerson_id(Integer person_id) {
		this.person_id = person_id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}