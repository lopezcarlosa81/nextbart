package cargen.nextbart.webservices;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cargen.nextbart.repositories.IStopRepository;
import cargen.nextbart.repositories.BartStopRepository;
import cargen.nextbart.models.Departure;
import cargen.nextbart.models.GeoLocation;
import cargen.nextbart.models.Stop;

/**
 * WebService to get Transit Stop information
 * Returns all information in JSON
 * TODO change from using Repository directly to using a Service 
 * which will allow for extra layer for things like error handling, and possibly combining other Stop Repositories
 * @author lopezc
 *
 */
@Path("/stops")
@Produces(MediaType.APPLICATION_JSON)
public class StopService {
	
	//TODO: use Dependency Injection to inject repository
	private IStopRepository repository = new BartStopRepository();

	/**
	 * 'http://root/stops/all'
	 * @return all stops
	 */
    @GET
    @Path("all")
    public List<Stop> getAll() {
        return repository.getStops();
    }
    
    /**
     * 'http://root/stops/nearest?latitude=37.7&longitude=-122.39'
     * @param latitude
     * @param longitude
     * @return Stop nearest given coordinates
     */
    @GET
    @Path("nearest")
    public Stop getNearestStop(
    		@DefaultValue("0") @QueryParam("latitude") double latitude,
            @DefaultValue("0") @QueryParam("longitude") double longitude){
    	// 0,0 are invalid coordinates (somewhere in Atlantic Ocean)
    	if(latitude==0 && longitude==0)
    		return null;
    	
    	return repository.nearestStop(new GeoLocation(latitude, longitude));
    }
    
    /**
     * 'http://root/stops/departures?stopCode=MONT'
     * @param stopCode
     * @return next departure times for given stop
     */
    @GET
    @Path("departures")
    public List<Departure> getDepartureTimes(
    		@DefaultValue("") @QueryParam("stopCode") String stopCode){
    	// stopCode must have a value, otherwise return empty List
    	if(stopCode == null || stopCode.isEmpty())
    		return new ArrayList<Departure>();
    	
    	return repository.departureTimesForStop(stopCode);
    }

}