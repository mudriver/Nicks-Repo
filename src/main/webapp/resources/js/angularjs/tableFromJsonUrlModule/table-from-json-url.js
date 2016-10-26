angular.module('tableFromJson',['spring-security-csrf-token-interceptor','loadingModule', 'ui.bootstrap']).filter('myDateFormat', function myDateFormat($filter){
	  return function(text){
		    var  tempdate= new Date(text);
		    return $filter('date')(tempdate, "dd/MM/yyyy");
		  }
		})
		.config(['$httpProvider', function($httpProvider) {
		    $httpProvider.interceptors.push('LoadingInterceptor');
		   
		}]);