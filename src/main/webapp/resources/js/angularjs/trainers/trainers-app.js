var app = angular.module("trainersApp", ['ngRoute','spring-security-csrf-token-interceptor','sub-elements','tableFromJson','editFromJson','LocalStorageModule', 'ngSanitize', 'ui.select','blueimp.fileupload']);

app.config(function($routeProvider) {
    
	$routeProvider
        .when('/edit/:id', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/trainers-edit.html',
        })
        .when('/edit', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/trainers-edit.html',
        })
        .when('/pensionTrainerlist', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/pension-trainers-list.html',

        })
        .when('/trainerlist', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/trainers-list.html',

        })
        .when('/employeelist', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/employee-list.html',

        })
        .when('/employeelist/:id', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/employee-list.html',

        })
        .when('/employeePensionList/:id', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/pension-employee-list.html',

        })    
        .when('/authreplist', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/auth-rep-list.html',

        })
        .when('/authreplist/:id', {
            templateUrl: '/turfclubPrograms/resources/js/angularjs/trainers/templates/auth-rep-list.html',

        }) 
        .otherwise({
            redirectTo: '/trainerlist'
        });
}).config(function (localStorageServiceProvider) {
	  localStorageServiceProvider
	    .setPrefix('trainersApp');
	}).config([
	            '$httpProvider', 'fileUploadProvider',
	            function ($httpProvider, fileUploadProvider) {
	                delete $httpProvider.defaults.headers.common['X-Requested-With'];
	                fileUploadProvider.defaults.redirect = window.location.href.replace(
	                    /\/[^\/]*$/,
	                    '/cors/result.html?%s'
	                );

	                    // Demo settings:
	                    angular.extend(fileUploadProvider.defaults, {
	                        // Enable image resizing, except for Android and Opera,
	                        // which actually support image resizing, but fail to
	                        // send Blob objects via XHR requests:
	                        disableImageResize: /Android(?!.*Chrome)|Opera/
	                            .test(window.navigator.userAgent),
	                        maxFileSize: 9999000,
	                        acceptFileTypes: /(\.|\/)(jpe?g|png|pdf)$/i
	                    });
	 
	            }
	        ]);