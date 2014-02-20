package cargen.nextbart.repositories;

import cargen.nextbart.models.Departure;
import cargen.nextbart.models.GeoLocation;
import cargen.nextbart.models.Stop;

import java.util.List;


/**
 * Holds methods for retrieving information about public transportation Stops
 * @author lopezc
 *
 */
public interface IStopRepository {
	/**
	 * Returns all stops for public transportation agency
	 * @return list of all Stops
	 */
	public List<Stop> getStops();
	
	/**
	 * List of departure times for Stop within the next 90 minutes
	 * @param stopCode - code of Stop
	 * @return list of departure times.
	 */
	public List<Departure> departureTimesForStop(String stopCode);
	
	/**
	 * Returns nearest stop to a location
	 * @param location - geo location with latitude and longitude
	 * @return nearest stop to location
	 */
	public Stop nearestStop(GeoLocation location);

}
