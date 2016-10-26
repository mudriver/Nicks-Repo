angular.module('loadingModule').
  service('LoadingInterceptor', 
		    ['$q', '$rootScope', '$log', '$anchorScroll',
		    function($q, $rootScope, $log, $anchorScroll) {
		        'use strict';
		 
		        var xhrCreations = 0;
		        var xhrResolutions = 0;
		     
		        function isLoading() {
		           
		        	if(xhrResolutions < xhrCreations){
		        		$anchorScroll();
		        	}
		            return xhrResolutions < xhrCreations;
		        }
		     
		        function updateStatus() {
		            $rootScope.loading = isLoading();
		        }
		     
		        return {
		            request: function (config) {
		            	//console.log(JSON.stringify(config));
		            	if(config.url.indexOf("/select/") > -1){
		                	//console.log(JSON.stringify(config.url));
		                }
		            	else if(config.url.toLowerCase().indexOf("cache") > -1){
		            		
		            	}
		                else{
		                	//console.log("loading:" + JSON.stringify(config.url));
			            	xhrCreations++;
			                updateStatus();
		                }
		            	
		                return config;
		            },
		            requestError: function (rejection) {
		            	if(rejection.config.url.indexOf("/select/") > -1){
		                	//console.log(JSON.stringify(config.url));
		                }
else if(rejection.config.url.toLowerCase().indexOf("cache") > -1){
		            		
		            	}
		                else{
		                	//console.log("loading:" + JSON.stringify(config.url));
		                	xhrResolutions++;
			                updateStatus();
		                }
		                $log.error('Request error:', rejection);
		                return $q.reject(rejection);
		            },
		            response: function (response) {
		            	//console.log(JSON.stringify(response.config.url));
		            	if(response.config.url.indexOf("/select/") > -1){
		                	//console.log(JSON.stringify(config.url));
		                }
else if(response.config.url.toLowerCase().indexOf("cache") > -1){
		            		
		            	}
		                else{
		                	//console.log("loading:" + JSON.stringify(config.url));
		                	xhrResolutions++;
			                updateStatus();
		                }
		            	
		            	
		                return response;
		            },
		            responseError: function (rejection) {
		            	//console.log("responseError");
		            	//console.log(rejection.status);
		            	
		            	if(rejection.config !== undefined){
		            		if(rejection.config.url.indexOf("/select/") > -1){
			                	console.log("error:" + JSON.stringify(rejection.config.url));
			                }
			            	else if(rejection.config.url.toLowerCase().indexOf("cache") > -1){
			            		
			            	}

			                else{
			                	//console.log("error:" + JSON.stringify(rejection.config.url));
			                	xhrResolutions++;
				                updateStatus();
			                }
		            	}
		            	else{
		            		xhrResolutions = xhrCreations;
		            		updateStatus();
		            	}
		            	
		            	
		                //$log.error('Response error:', rejection);
		            	return $q.reject(rejection);
		            }
		        };
		    }]);