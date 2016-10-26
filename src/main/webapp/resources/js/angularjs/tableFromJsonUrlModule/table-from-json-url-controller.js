angular.module('tableFromJson').controller('jsonTableController', function ($scope, localStorageService, jsonService, $interval, $routeParams) {

	var ctrl = this;

	ctrl.initializing = true;
	ctrl.offline = false;
	
	
	
	//ctrl.cacheUpdated = localStorageService.get('cacheUpdated');
	ctrl.config = {};
	ctrl.data= {};
	ctrl.init = function(){

		ctrl.getConfig();
		
  	};

  	


  	

 
  	
  	
  	ctrl.refresh = function(){
  		//console.log(JSON.stringify(ctrl.config.savedSearch));
  		console.log("refresh " + ctrl.initializing);
  		if(!ctrl.initializing){
  			if(ctrl.config.savedSearch.currentRecordStart == null){
  	  			ctrl.config.savedSearch.currentRecordStart = 0;
  	  		}
  	  		console.log("cache search");
  	  		
  	  		
  	  		ctrl.getData(ctrl.config.dataUrl, ctrl.config.savedSearch);
  		}
  		
  		
  		
  	}

  	ctrl.loadSearch = function(){
  		console.log("load search");
  		ctrl.config.savedSearch = ctrl.searchName;
  		ctrl.refresh();
  	}
  
/*
  	if(localStorageService.get('updateCache') == null){
  		console.log("Setting update cache")
  		localStorageService.set('updateCache', true);
  	}
  	
  	var cacheTimer = $interval(function(){
  		console.log("CACHE TRUE");
  		localStorageService.set('updateCache', true);
  		},10000);
  	
  	ctrl.getCachedData = function(){
  		console.log(ctrl.config.cachedDataUrl);
  		jsonService.getData(ctrl.config.cachedDataUrl)
	  		.then(function(res){
	  			// success
	  			console.log("New Data:");
	  			var date = new Date();
	  			var newDate = new Date(date.getTime()+date.getTimezoneOffset()*60*1000);

	  		    var offset = date.getTimezoneOffset() / 60;
	  		    var hours = date.getHours();

	  		    newDate.setHours(hours - offset);

	  		    
	  			localStorageService.set('cacheUpdated', newDate.toString());
	  			ctrl.cacheUpdated = localStorageService.get('cacheUpdated');
	  			localStorageService.set('cacheData', res.data);
	  			//console.log(JSON.stringify(ctrl.data));
	  			//console.log(JSON.stringify(ctrl.config));
	  			
	  		}, function(err){
	  			alert("Server Error");
	  		})
  	}*/
  	
  	ctrl.getData = function(dataUrl, savedSearch, cachedDataUrl){

  		//console.log("get data from:" + dataUrl);
  		console.log(JSON.stringify(savedSearch));
  		
  		jsonService.getData(dataUrl, savedSearch, $routeParams.id)
  		.then(function(res){
  			// success
  			//console.log("DATA:" + JSON.stringify(res));
  			if(res == null){
  				//console.log("Use cache");
  				//ctrl.offline = true;
  				ctrl.clearSearch();
  				/*
  				ctrl.data = localStorageService.get('cacheData');
  	  			ctrl.max = ctrl.data.length;
  	  			*/
  			}
  			else{
  				ctrl.offline = false;
  				ctrl.data = res.data;
  	  			ctrl.max = res.max;
  	  		ctrl.initializing = false;
  	  			console.log("Initializing "  + ctrl.initializing)
  	  			console.log("SET search " + ctrl.config.lastSearchPrefix);
  	  			localStorageService.set(ctrl.config.lastSearchPrefix, savedSearch);
  	  		//console.log("Update Cache" + localStorageService.get('updateCache') + (localStorageService.get('updateCache') == true));
  	  		/*
  	  			if(ctrl.config.cachedDataUrl != null && localStorageService.get('updateCache') == true && res != null){
  	  			ctrl.getCachedData();
  	  			localStorageService.set('updateCache', false);
  	  		}*/
  			}
  			
  			//console.log(JSON.stringify(ctrl.data));
  			//console.log(JSON.stringify(ctrl.config));
  			
  		}, function(err){
  			alert("Server Error");
  		})
  		
  		
  		
  	};
  	

  
  	
  	ctrl.saveSearch = function(){
//console.log(ctrl.saveAs.length + ctrl.saveAs)
  		if(ctrl.saveAs.length > 4){
  			var nameExists = false;
  	  		angular.forEach(ctrl.config.savedSearchConfig.savedSearches, function(key, value){
  	  			if(ctrl.saveAs == key.savedSearchName){
  	  				nameExists = true;
  	  			}
  	  			
  	  		})
  	  		if(nameExists){
  	  			alert("There is already a search called:" + ctrl.saveAs);
  	  		}
  	  		else{
  	  			ctrl.config.savedSearch.savedSearchName = ctrl.saveAs;
  	  			jsonService.getData(ctrl.config.savedSearchConfig.saveUrl, ctrl.config.savedSearch)
  	  	  		.then(function(res){
  	  	  			console.log(JSON.stringify(res));
  	  	  			ctrl.config.savedSearchConfig.savedSearches = res;
  	  	  			ctrl.searchName = ctrl.config.savedSearch;
  	  	  		}, function(err){
  	  	  			alert("Server Error");
  	  	  		})
  	  		}
  	  		ctrl.saveAs = "";
  		}
  		else{
  			alert("The save name is too short!");
  		}
  		
  		
  		
  		
  		
  	}
  	
  	ctrl.clearSearch = function(){
  		console.log(JSON.stringify(ctrl.config.savedSearch));
  		ctrl.searchName = null;
  		ctrl.config.savedSearch = angular.copy(ctrl.blankSearch);
  		ctrl.refresh();
  		console.log(JSON.stringify(ctrl.config.savedSearch));
  	}
  	
  	
  	ctrl.getConfig = function(){

 

  		jsonService.getConfig(ctrl.configUrl)
  		.then(function(res){
  			// success
  			
  			
  			//console.log(JSON.stringify(res));
  			if(res == null){
  				ctrl.offline = true;
  				/*
  				ctrl.config = localStorageService.get('config');
  				*/
  				
  			}
  			else{
  				ctrl.config = res;
  				ctrl.offline = false;
  				ctrl.blankSearch = angular.copy(ctrl.config.savedSearch);
  				//console.log(JSON.stringify(res));
  				/*
  				localStorageService.set('config',res);
  				*/
  				
  				console.log(ctrl.config.lastSearchPrefix);
  				if(ctrl.config.lastSearchPrefix !== null &&   localStorageService.get(ctrl.config.lastSearchPrefix) !== null){
  					console.log("using stored search");
  					ctrl.config.savedSearch = localStorageService.get(ctrl.config.lastSearchPrefix);
  					
  				}
  				
  				ctrl.getData(ctrl.config.dataUrl, ctrl.config.savedSearch, ctrl.config.cachedDataUrl);
  				
  			}
  			
  			
  			/*
  		  	if(localStorageService.get('savedSearch') !== null){
  		  	console.log(JSON.stringify(localStorageService.get('savedSearch')));
  		  		ctrl.hasLastSearch = true;
  		  		//ctrl.searchName = localStorageService.get('savedSearch');
  		  		ctrl.config.savedSearch = localStorageService.get('savedSearch');
  		  	}*/
  		  	
  			
  		}, function(err){
  			alert("Server Error");
  		})
  	};
   
  	ctrl.showNext = function(){
  		ctrl.config.savedSearch.currentRecordStart += (ctrl.config.savedSearch.maxToShow);
  		ctrl.refresh();
  	}
  	ctrl.showPrev = function(){
  		ctrl.config.savedSearch.currentRecordStart -= (ctrl.config.savedSearch.maxToShow);
  		ctrl.refresh();
  	}
  	

  	ctrl.init();
 
  	return ctrl;
  	
})












