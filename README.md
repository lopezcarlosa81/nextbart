# Next BART application

http://nextbart.herokuapp.com/

This application shows departure times for BART stops.  It also finds your current location, shows you the nearest BART stop, and defaults to showing next departure times for that stop.
The UI is a single page web application that uses jQuery and Knockout.js to display the information and a Web Service to get its data. The backend is a Java Jetty web service that gets its data from a Repository which in turn gets data for BART stop informatoin from api.bart.gov and passes information through business model classes.

## Code
The three main java files for the backend are StopService.java (src/main/java/cargen/nextbart/webservices), BartStopRepository.java (src/main/java/cargen/nextbart/repositories), and Stop.java (src/main/java/cargen/nextbart/models).
StopService.java implements a Java Jetty web service to return BART stop information.  It includes a getAll method to return all stops, a getNearestStop method to get the stop closest to a location, and a getDepartureTimes method to get departure times for a given stop. It relies on BartStopRepository to get its information.
BartStopRepository.java returns BART stop information using data from http://api.bart.gov.  This class caches basic BART stop information with the assumption that this data changes very infrequently.  
Stops.java is the root model for the application and represents a public transportation stop.  It includes properties such as name, code, address, and geo location.

## Technical Notes
One thing to add in the future is other public transit systems like MUNI.  There could be a MuniStopRepository that returns MUNI specific information.  A Service layer can be added between the Repositories and the web service so that the service can perhaps join stop information from the different repositories and expose a unified list to the web service.
More exception and error handling can also be added and exposed to the user for times when the backend api may fail (e.g. bart.gov is down).
Another item that needs to be added is unit testing.

    
## Running the application locally
First build with: $mvn clean install then run it with: $ java -cp target/classes:target/dependency/* cargen.nextbar.main.Main

