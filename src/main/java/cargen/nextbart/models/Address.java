package cargen.nextbart.models;

/**
 * Represents a stree address
 * @author lopezc
 *
 */
public class Address {
	private String street;
	private String city;
	private String state;
	
	public Address(String street, String city, String state){
		this.street = street;
		this.city = city;
		this.state = state;
	}
	
	public String toString(){
		return String.format("%s %s, %s", street, city, state);
	}
	
	public String getStreet(){
		return street;
	}
	
	public String getCity(){
		return city;
	}
	
	public String getState(){
		return state;
	}
}
