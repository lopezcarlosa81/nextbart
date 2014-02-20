package cargen.nextbart.models;

import java.util.List;

/**
 * Represents a departure time for a transit system at a stop to a destination
 * e.g. train departs in 5 minutes from platform 2
 * @author lopezc
 *
 */
public class Departure {
	
	private String destination;
	private List<String> departureTimes;
	
	public Departure(String destination, List<String> departureTimes){
		this.destination = destination;
		this.departureTimes = departureTimes;
	}
	
	public String getDestination(){
		return destination;
	}
	
	public List<String> getDepartureTimes(){
		return departureTimes;
	}


}
