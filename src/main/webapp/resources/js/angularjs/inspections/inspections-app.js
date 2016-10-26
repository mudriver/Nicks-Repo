var app = angular.module("inspectionsApp", ['ngRoute','spring-security-csrf-token-interceptor','sub-elements','tableFromJson','editFromJson', 'LocalStorageModule', 'ngSanitize', 'ui.select', 'blueimp.fileupload']);

app.config(function($routeProvider) {
    

	
	$routeProvider
        .when('/edit/:id', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/inspections/templates/inspections-edit.html',

        })
        .when('/edit', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/inspections/templates/inspections-edit.html',

        })
        .when('/list', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/inspections/templates/inspections-list.html',

        })
        .otherwise({
            redirectTo: '/list'
        });
}).config(function (localStorageServiceProvider) {
	  localStorageServiceProvider
	    .setPrefix('inspectionsApp');
	});


			  
