cargen.nextbart.ko = (function(){
	
	var instance;
	function singleton(){
		// setup instance properties as knockout observables
		this.Stops = ko.observableArray([]);
		this.NearestStopName = ko.observable();
		this.NearestStopAddress = ko.observable();	
		this.ChosenStopDepartures = ko.observableArray([]);
		
		// show/hide
		this.ShowNearestStop = ko.computed(function () {
            return this.NearestStopName();
        }, this);

		this.ShowChosenStopDepartures = ko.computed(function () {
            return this.ChosenStopDepartures().length > 0;
        }, this);
		
		// message
		this.Message = ko.observable('');
		this.ShowMessage = ko.computed(function () {
            return this.Message();
        }, this);
	};
	
	// returns object with getInstance method
	// to retrieve singleton instance
	return {
		getInstance : function(){
			if(instance === undefined)
				instance = new singleton();
			return instance;
		}
	};
})();