package cargen.nextbart.models;

import cargen.nextbart.models.Address;
import cargen.nextbart.models.GeoLocation;

/**
 * Represents a public transportation Stop.
 * Examples include a stop for the San Francisco train system (BART)
 * @author lopezc
 *
 */
public class Stop {
	private String name;
	private String code;
	private GeoLocation geoLocation;
	private Address address;

	public Stop(String name, String code, GeoLocation geoLocation, Address address){
		this.name = name;
		this.code = code;
		this.geoLocation = geoLocation;
		this.address = address;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCode(){
		return code;
	}
	
	public GeoLocation getGeoLocation(){
		return geoLocation;
	}
	
	public Address getAddress(){
		return address;
	}
}