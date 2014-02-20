package cargen.nextbart.repositories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cargen.nextbart.models.Address;
import cargen.nextbart.models.Departure;
import cargen.nextbart.models.GeoLocation;
import cargen.nextbart.models.Stop;

/**
 * Returns information about BART Stops (SF train system).
 * This repository caches basic information about BART Stops in a static field.
 * It assumes Stops change very infrequently. 
 * gets BART information from api.art.gov
 * http://api.bart.gov/api/
 * @author lopezc
 *
 */
public class BartStopRepository implements IStopRepository {

	// in memory cache of BART stops
	// Assuming BART stop change very infrequently
	private static List<Stop> bartStops;
	
	// api URLs
	// TODO place in web.xml or settings file
	private final String API_BASE_URL="http://api.bart.gov/api/";
	private final String API_KEY = "&key=MW9S-E7SL-26DU-VV8V";
	
	public BartStopRepository(){
	}

	/**
	 * Returns list of all BART stops
	 */
	public List<Stop> getStops() {
		// load basic BART stop information into static field for faster retrieval
		if(bartStops == null || bartStops.isEmpty())
			this.loadBartStops();
		return bartStops;
	}

	/**
	 * Returns list of next departure times for stop within the next 90 minutes
	 * Gets data from http://api.bart.gov/api/etd.aspx
	 */
	public List<Departure> departureTimesForStop(String stopCode) {
		List<Departure> departures = new ArrayList<Departure>();
		
		// if stopCode is null or empty return empty list
		if(stopCode == null || stopCode.isEmpty())
			return new ArrayList<Departure>();
		
		try {
			// call api to get next departures for a given stop and create Departure model
			// TODO cleanup XML parsing
			String departuresURL = API_BASE_URL + "etd.aspx?cmd=etd" + API_KEY + "&orig=" + stopCode;
			NodeList estimatedDepartures = XMLParseURL(departuresURL, "etd");
	        for( int i=0; i<estimatedDepartures.getLength(); i++ ) {
	        	Element estimatedDeparture = (Element)estimatedDepartures.item( i );
	        	String destination = XMLChildValue(estimatedDeparture, "destination");
	        	List<String> departureTimes = new ArrayList<String>();
	        	// create Departure
	        	Departure departure = new Departure(destination, departureTimes);
	        	NodeList estimates = estimatedDeparture.getElementsByTagName( "estimate" );
	        	// add departureTimes to Departure
	        	for( int j=0; j<estimates.getLength(); j++ ) {
	        		Element estimate = (Element)estimates.item(j);
	        		String minutes = XMLChildValue(estimate,"minutes" );
	        		departureTimes.add(minutes);
	        	}
	        	departures.add(departure);
	        }
		} catch (Exception e) {
			// TODO log exception
		}
		
		return departures;
	}

	/**
	 * Returns the nearest BART stop to location
	 */
	public Stop nearestStop(GeoLocation location) {
		// iterate through all stops and return the one with least distance;
		Stop nearest = null;
		double minDistance = Double.MAX_VALUE;
		for(Stop stop : getStops()){
			// distance between 2 points:
			// sqt( (x2-x1)^2 + (y2-y1)^2 )
			double distance = Math.sqrt( 
					Math.pow(location.getLatitude() - stop.getGeoLocation().getLatitude(),2.0) +
					Math.pow(location.getLongitude() - stop.getGeoLocation().getLongitude(), 2.0));
			if(distance < minDistance)
			{
				nearest = stop;
				minDistance = distance;
			}
		}
		return nearest;
	}
	
	
	/**
	 * Loads basic BART stop information into bartStops static field.
	 * Assuming that BART stop basic information changes very infrequently.
	 * This allows for faster retrieval of stop information
	 * Data from http://api.bart.gov/api/
	 */
	private void loadBartStops(){
		bartStops = new ArrayList<Stop>();
		try {
			// calls api and loads information into Stop models and into bartStop static List
			// TODO cleanup XML parsing
			String stationsURL = API_BASE_URL + "stn.aspx?cmd=stns" + API_KEY;
			NodeList stops = XMLParseURL(stationsURL,"station");
            for( int i=0; i<stops.getLength(); i++ ) {
                Element stopRoot = (Element)stops.item( i );
                String name = XMLChildValue(stopRoot, "name");
                String code = XMLChildValue(stopRoot, "abbr");
                String latitude = XMLChildValue(stopRoot, "gtfs_latitude");
                String longitude = XMLChildValue(stopRoot, "gtfs_longitude");
                String street = XMLChildValue(stopRoot, "address");
                String city = XMLChildValue(stopRoot, "city");
                String state = XMLChildValue(stopRoot, "state");
                
                Stop stop = new Stop(name, code, new GeoLocation(Double.parseDouble(latitude),Double.parseDouble(longitude)), new Address(street, city,state));
                bartStops.add(stop);
            }
		}
		catch (Exception e) {
			//TODO log exception
		}
	}
	
	/**
	 * Returns the value of the first child element with childName under root
	 * <root><child>value</child></roo>
	 * @param root
	 * @param childName
	 * @return value of first childName element
	 */
	private String XMLChildValue(Element root, String childName){
		if(root == null || childName == null || root.getElementsByTagName(childName) == null)
			return "";
		
		return root.getElementsByTagName(childName).item( 0 ).getTextContent();
	}
	
	/**
	 * Parses an XML document from a URL and returns list of Nodes that match tag
	 * @param URL
	 * @param tag
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private NodeList XMLParseURL(String URL, String tag) throws ParserConfigurationException, SAXException, IOException{
		if(URL == null || URL.isEmpty() || tag == null || tag.isEmpty())
			return null;
		
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(URL);
		return document.getElementsByTagName(tag);
	}
}
