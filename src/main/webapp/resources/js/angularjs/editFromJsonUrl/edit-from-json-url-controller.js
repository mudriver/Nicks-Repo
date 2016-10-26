angular.module('editFromJson')
.controller('jsonEditController', function ($scope, $routeParams, $modal,$location, jsonEditService) {
	
	$scope.submitted = false;
	var ctrl = this;
	
	
	ctrl.init = function(){

		ctrl.getConfig();
  	};
	
  	ctrl.getConfig = function(){

  		 

  		jsonEditService.getConfig(ctrl.configUrl, $routeParams.id)
  		.then(function(res){
  			// success
  			
  			
  			
  			ctrl.config = jsonEditService.config;
  			
  			
  		}, function(err){
  			alert("Server Error");
  		})
  	};
   
  	
  	
  	
  	ctrl.saveData = function(form){
  		 
  		console.log("click");
  
  		
  		console.log(JSON.stringify(ctrl.config.data));
  		angular.forEach(form.$error, function(type) {
  	        angular.forEach(type, function(field) {
  	            
  	        	field.$setDirty();
  	            
  	        });
  	    });
  		if(!form.$invalid){
  			console.log("Submit");
  			jsonEditService.postSaveData(ctrl.config.saveUrl ,ctrl.config.data)
  			.then(function(res){
  			
  			console.log(JSON.stringify(res));
  			var modalInstance = $modal.open({
    		      keyboard : false,
    		      backdrop : 'static',
    		      controller: 'saveModalController',
    		      templateUrl: '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/save-complete-modal.html',
    		    	resolve: {
    		          message: function () {
    		              return res.message;
    		          }
    		        }
    			});

    		    modalInstance.result.then(function () {
    		      console.log("OK")
    		      $location.path(ctrl.config.afterSaveUrl);
    		    }, function () {
    		      $log.info('Modal dismissed at: ' + new Date());
    		    });
  		}, function(err){
  			alert("Server Error");
  		})
  			
  		}

  			

  		
  	};

  	ctrl.cancel = function(){
  		$location.path(ctrl.config.afterSaveUrl);
  	};
  	
  	ctrl.init();
 
  	return ctrl;
});

