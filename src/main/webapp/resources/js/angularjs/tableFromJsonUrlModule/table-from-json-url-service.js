angular.module('tableFromJson')
  .service('jsonService', function jsonService($http, $q, $window) {
  
  var jsonService = this;


  jsonService.config = {};
  
 
  
  
  jsonService.getConfig = function(configUrl){
  	var defer = $q.defer();


  	
  	$http.post(configUrl)
  	.success(function(res){
  		//console.log("Result:" + JSON.stringify(res) + JSON.stringify(res).indexOf("<html>"));
  		if(JSON.stringify(res).indexOf("html>") > -1 && JSON.stringify(res).indexOf("Please Enter User Name") > -1){
  			$location.path("/");
  		}
  		else if(JSON.stringify(res).indexOf("html>") > -1){
  			//console.log("null");
  			defer.resolve(null);
  		}
  		else{
  			defer.resolve(res);
  		}
  		
  	})
  	.error(function(err, status){
  		defer.reject(err);
  	});

  	return defer.promise;
  };
  
  jsonService.getData = function(dataUrl, savedSearch, id){
	  	var defer = $q.defer();
	  	var params = {};
	  	params = savedSearch;
	  	if(id !== undefined){
	  		dataUrl += "/" + id;
	  	}

	  	//console.log("Get Data: " + dataUrl + " - " + JSON.stringify(params));
	  	$http.post(dataUrl, params)
	  	.success(function(res){
	  		//console.log("Result:" + JSON.stringify(res) + JSON.stringify(res).indexOf("<html>"));
	  		if(JSON.stringify(res).indexOf("html>") > -1 && JSON.stringify(res).indexOf("Please Enter User Name") > -1){
	  			
	  			$window.location='/turfclubPrograms/home';
	  		}
	  		else if(JSON.stringify(res).indexOf("html>") > -1){
	  			console.log("null");
	  			defer.resolve(null);
	  		}
	  		else{
	  			defer.resolve(res);
	  		}
	  		
	  		
	  	})
	  	.error(function(err, status){
	  		console.log("Error:" + JSON.stringify(err));
	  		defer.reject(err);
	  	});

	  	return defer.promise;
  };
  
  
  jsonService.saveSearch = function(saveUrl, saveSearch){
	  	var defer = $q.defer();


	  	var params = {savedSearch: saveSearch};
	  	//console.log(JSON.stringify(saveSearch));
	  	$http.post(saveUrl, saveSearch)
	  	.success(function(res){
	  		
	  		defer.resolve(res);
	  		
	  	})
	  	.error(function(err, status){
	  		defer.reject(err);
	  	});

	  	return defer.promise;
	  };
  

  return jsonService;

  });