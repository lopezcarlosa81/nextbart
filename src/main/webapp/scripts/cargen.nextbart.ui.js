cargen.nextbart.ui = (function(model, service){
	//TODO place messages in resource file
	var _technialErrorMessage = "Technical issue.  Please reload your page.";
	var _geolocationMessageError = "Could not get your current location."; 
	var _geolocationMessageGettingLocation = "Getting your current location...";
	var _geolocationMessageGettingNearestStop = "Getting nearest BART stop...";
	var _loadingMessage = "Loading...";

	function _stopsOnChange(e){
		// show loading message and hide existing table
		model.ChosenStopDepartures([]);
		model.StopsMessage(_loadingMessage);
		
		var selectedStop = $(this).find('option:selected').val();
		service.getDepartures(selectedStop)
		.then(function (departures) {
			// got departures
			// make times cleaner by adding min,
			for(var i=0; i<departures.length; i++){
				for(var j=0; j<departures[i].departureTimes.length; j++)
				{
					if(departures[i].departureTimes[j] && departures[i].departureTimes[j] != 'Leaving'){
						departures[i].departureTimes[j] += " min";
					}
					departures[i].departureTimesString = departures[i].departureTimes.join(', ');
				}
			}
			model.StopsMessage('');
			model.ChosenStopDepartures(departures);
			},function(){
			// service error
			model.Message(_technialErrorMessage);
			});
	}
	
	// jquery DOM Ready
	$(document).ready(function(){
	
		// apply ko bindings
		ko.applyBindings(model);
		
		// bind stop onclick
		$("#stops").on('change', _stopsOnChange);
	
		// get all stops and return promise that stops are loaded
		var _stopsLoaded = service.getStops()
			.then(function (data) {
				// success, load stops
	            model.Stops(data);
	            },function(){
	            //service error
	            model.Message(_technialErrorMessage);
	            });	
		
	
		// get users' location
		// and if available get closest BART stop and departure times
		if (navigator.geolocation){
			
			var _geolocationSuccess = function(position){
				var latitude = position.coords.latitude;
				var longitude = position.coords.longitude;
				if(latitude && longitude){
					model.Message(_geolocationMessageGettingNearestStop);
					
					// gets nearest stop and returns promise that we have nearest stop
					var _gotNearestStop = service.nearestStop(latitude, longitude)
						.then(function (nearestStop) {
							// success getting nearest stop
							if(nearestStop){
								model.NearestStopName(nearestStop.name);
								model.NearestStopAddress(nearestStop.address.street + " " + nearestStop.address.city + ", " + nearestStop.address.state);
								return nearestStop.code;					
							}				
						},function(){
							  //service error
							  model.Message(_technialErrorMessage);
							  return undefined;
						  });
					
					// clear message
					model.Message('');
				
					// once we have both all stops loaded in drop down and nearest stop, 
					// pick nearest stop from drop down as default
					$.when( _stopsLoaded, _gotNearestStop).done(function(a1, a2) {
						if(a2){
							$("#stops").val(a2).trigger('change');
						}
					});
				}
				else{
					// could not get latitude longitude
					model.Message(_geolocationMessageError);
				}
			}
			
			var _geolocationError = function(err){
				// error getting current location
				model.Message(_geolocationMessageError);
			}
			
			navigator.geolocation.getCurrentPosition(_geolocationSuccess, _geolocationError);
			model.Message(_geolocationMessageGettingLocation);
	    }
		else{
			// browser does not support geo location
			model.Message(_geolocationMessageError)
		}
	});
	
})(cargen.nextbart.ko.getInstance(), cargen.nextbart.service);