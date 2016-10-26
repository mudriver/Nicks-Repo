angular.module('sub-elements')
//service to retrieve data for select2 elements
.service('select2Service', function select2Service($http, $q) {
  
  var s2Service = this;


  s2Service.getData = function(url, params){
	  	var defer = $q.defer();

	  	if(params !== undefined){
	  		console.log(JSON.stringify(params));
	  	}

	  	
	  	$http.get(url, {params : params})
	  	.success(function(res){
	  		s2Service.data = res;
	  		//console.log(JSON.stringify(res));
	  		defer.resolve(res);
	  		
	  	})
	  	.error(function(err, status){
	  		defer.reject(err);
	  	});

	  	return defer.promise;
  };
  
  
  return s2Service;

})
.service('jsonSubElementService', function jsonSubElementService($http, $q) {
  
  var jsonService = this;


  jsonService.saveData = function(saveUrl, objectToSave){
	  	var defer = $q.defer();


	  	
	  	$http.post(saveUrl, objectToSave)
	  	.success(function(res){
	  		
	  		console.log(JSON.stringify(res));
	  		defer.resolve(res);
	  		
	  	})
	  	.error(function(err, status){
	  		defer.reject(err);
	  	});

	  	return defer.promise;
	  };
  
  
  return jsonService;

})