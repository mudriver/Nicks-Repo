angular.module("trainersApp")

.controller('approveButtonController', function ($scope,  jsonService,  $routeParams){
	
	$scope.modelValue = false;
	$scope.approve = function(){
		console.log("Click");
		
		$scope.modelValue = !$scope.modelValue;
		jsonService.getData("/turfclubPrograms/trainers/service/update/trainerVerified", $scope.modelValue, $routeParams.id)
  		.then(function(res){
  			
  			$scope.modelValue = res;
  				

  		}, function(err){
  			
  			alert("Server Error");
  		})
	}
	jsonService.getData("/turfclubPrograms/trainers/service/update/trainerVerified", null, $routeParams.id)
		.then(function(res){
			console.log(res);
			$scope.modelValue =res;
				

		}, function(err){
			
			alert("Server Error");
		})
	

	
	
});