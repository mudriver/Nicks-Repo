angular.module('editFromJson')
  .service('jsonEditService', function jsonService($http, $q) {
  
  var jsonService = this;


  jsonService.config = {};
  
  jsonService.data = {};
  
  
  jsonService.getConfig = function(configUrl, id){
  	var defer = $q.defer();

  	
  	var params = {id : id};
  	$http.get(configUrl, {params: params})
  	.success(function(res){
  		jsonService.config = res;
  		console.log(JSON.stringify(res));
  		defer.resolve(res);
  		
  	})
  	.error(function(err, status){
  		defer.reject(err);
  	});

  	return defer.promise;
  };
  
  jsonService.postSaveData = function(saveUrl, objectToSave){
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
  
});