package cargen.nextbart.models;

/**
 * Represents a specific poing on earth
 * @author lopezc
 *
 */
public class GeoLocation {
	
	private double latitude;
	private double longitude;
	
	public GeoLocation(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}

}
