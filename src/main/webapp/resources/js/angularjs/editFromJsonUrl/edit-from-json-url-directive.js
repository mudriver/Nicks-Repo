angular
		.module('editFromJson')

		// creates a table structure based on the configuration retrieved from
		// the url by $http post request
		.directive(
				'jsonEdit',
				function($compile) {

					return {
						restrict : 'E',
						scope : {
							configUrl : '@',
							id : "="
						},
						controller : "jsonEditController",
						controllerAs : "jsonEditCtrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/editFromJsonUrl/templates/edit-from-json-url-view.html',
						
					};
				});
				