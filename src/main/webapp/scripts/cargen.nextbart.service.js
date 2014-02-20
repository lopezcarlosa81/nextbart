cargen.nextbart.service = (function(){
	// TODO put urls in config or resource file
	var serviceUrl = '/services/stops';
	var getStopsUrl = serviceUrl + "/all";
	var nearestStopBaseUrl = serviceUrl + "/nearest";
	var departuresBaseUrl = serviceUrl + "/departures?stopCode=";
	

	// ajax call to get all stops
	// returns promise with data
    var _getStops = function () {
        return _ajaxGet(getStopsUrl);
    };
    
    // ajax call to get nearest BART stop
    // returns promise with data
    var _nearestStop = function(latitude, longitude){
    	var url = nearestStopBaseUrl + "?latitude=" + latitude +"&longitude=" + longitude;
        return _ajaxGet(url);
    }
    
    
    var _getDepartures = function(stopCode){
    	var url = departuresBaseUrl + stopCode;
    	return _ajaxGet(url);
    }
    
    // does an ajax GET
    // returns a promise
    var _ajaxGet = function(url){
        return $.ajax({
        	url: url,
        	type: 'GET',
        	dataType: 'json'
        		});
    }
    
    return {
    	getStops: _getStops,
    	nearestStop: _nearestStop,
    	getDepartures: _getDepartures
    }
})();