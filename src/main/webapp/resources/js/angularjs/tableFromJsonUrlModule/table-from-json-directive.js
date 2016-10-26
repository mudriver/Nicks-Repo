angular
		.module('tableFromJson')

		// creates a table structure based on the configuration retrieved from
		// the url by $http post request
		.directive(
				'jsonTable',
				function() {

					return {
						restrict : 'E',
						scope : {
							configUrl : '@'
							
						},
						controller : "jsonTableController",
						controllerAs : "jsonTableCtrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/tableFromJsonUrlModule/templates/json-table-view.html',
						link :{
							pre: function(scope,elem,attr, jsonTableCtrl){
						
			        		 
			        		 
			              }
						}
					};
				})

		

		.directive(
				'jsonTableSearches',
				function() {
					return {
						restrict : 'A',
						replace : true,
						scope : {
							config : '=',
							data : "=",
							refresh: "&"	
						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/tableFromJsonUrlModule/templates/json-table-searches-view.html',
						
					};
				})
		.directive(
				'jsonTableHeaders',
				function($compile) {
					return {
						restrict : 'A',
						replace : true,
						scope : {
							config : '=',
							search: "=",
							refresh: "&",

						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/tableFromJsonUrlModule/templates/json-table-headers-view.html',

					};
				})


