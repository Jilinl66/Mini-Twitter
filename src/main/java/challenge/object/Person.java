/**
 * Class Person
 * 
 * @author Jilin Liu
 */
package challenge.object;

public class Person {
	Integer id;
	String name;
	
	public Person(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
