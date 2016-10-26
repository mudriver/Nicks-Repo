angular
		.module('sub-elements')

		// controller for the bootstrap datepicker directive
		.controller('datePickerController', function($scope) {
			var ctrl = this;

			// console.log(ctrl.data);
			ctrl.date = "";
			ctrl.opened = false;

			ctrl.dateOptions = {

				showWeeks : false,

			};
			// open the date picker if the button is clicked firing this event
			ctrl.open = function(event) {

				event.preventDefault();
				event.stopPropagation();
				ctrl.opened = true;
			};

			return ctrl;

		})
		// controller for linked select2 directive
		.controller(
				'linkedSelect2Controller',
				function($http, $filter, select2Service) {

					var ctrl = this;

					// initialise the list of items to empty and the selection
					// to empty
					ctrl.items = [];
					ctrl.disabled = false;
					ctrl.out = [];
					if(ctrl.modelId !== undefined
							&& ctrl.modelValue != undefined) {
						ctrl.selection = {
							id : ctrl.modelId,
							value : ctrl.modelValue
						};
					}
					else {
						ctrl.selection = null;
					}
					console.log("Model" + ctrl.modelId);
					ctrl.hide = true;

					// function to initialise the item list using the provided
					// url and no parameters
					ctrl.initItems = function() {

						ctrl.items = [];

						select2Service.getData(ctrl.directive.searchUrl).then(
								function(res) {
									// success
									// assign the data to the select2 model
									ctrl.items = select2Service.data;
									ctrl.refreshItems();
									// console.log(JSON.stringify(ctrl.items));
								}, function(err) {
									alert("Server Error");
								});

					};

					ctrl.refreshItems = function() {

						ctrl.out = $filter('linkedFilter')(ctrl.items, {
							linkId : ctrl.linkedFormFieldModel,
							modelId : ctrl.modelId
						});
						console.log(ctrl.out.length);
						if(ctrl.out.length <= 0 && ctrl.modelId == null) {
							ctrl.disabled = true;
						}
						else {
							ctrl.disabled = false;
						}
						console.log("ModelID:" + ctrl.modelId);
						if(ctrl.modelId != null && ctrl.out.length > 0) {
							var onList = false;
							console.log("check selected on list");
							ctrl.out.forEach(function(item) {

								console.log(ctrl.modelId + " " + item.id);
								if(ctrl.modelId == item.id) {
									onList = true;
								}

							});

							// reset selection if new item not on list
							if(!onList) {
								console.log("clear" + onList);
								ctrl.modelId = null;
								ctrl.modelValue = null;
								ctrl.selection = null;
							}
						}

					};

					// if the config is set for load on init only then run init
					// function to load items only once
					if(ctrl.directive.loadOnInitOnly) {

						ctrl.initItems();
					}

					return ctrl;
				})
		// controller for multi-select2 directive
		.controller(
				'select2Controller',
				function($http, select2Service) {

					var ctrl = this;

					// initialise the list of items to empty and the selection
					// to empty
					ctrl.items = [];

					console.log("INIT: "
							+ JSON.stringify(ctrl.modelId + " "
									+ ctrl.modelValue));
					if(ctrl.modelId !== undefined
							&& ctrl.modelValue != undefined) {
						ctrl.selection = {
							id : ctrl.modelId,
							value : ctrl.modelValue
						};
					}
					else {
						ctrl.selection = null;
					}

					console.log("" + ctrl.modelId);
					ctrl.hide = true;

					// function to initialise the item list using the provided
					// url and no parameters
					ctrl.initItems = function() {

						ctrl.items = [];

						select2Service.getData(ctrl.directive.searchUrl).then(
								function(res) {
									// success
									// assign the data to the select2 model
									ctrl.items = select2Service.data;
									console.log(JSON.stringify(ctrl.items));
								}, function(err) {
									alert("Server Error");
								});

					};

					// function to refresh the item list from the provided url
					// using a search parameter
					ctrl.refreshItems = function(search) {

						// if the load on init only flag is not set then requery
						// the url for new data with the search
						if(!ctrl.directive.loadOnInitOnly) {
							ctrl.items = [];
							if(search.length > 3) {

								// hide the message asking for 3 or more
								// characters
								ctrl.hide = true;
								var params = {
									chars : search
								};
								select2Service.getData(
										ctrl.directive.searchUrl, params).then(
										function(res) {
											// success
											// assign the data to the select2
											// model
											ctrl.items = select2Service.data;

										}, function(err) {
											alert("Server Error");
										});
								/*
								 * var params = { chars : search }; return
								 * $http.get(ctrl.searchConfig.searchUrl,{params :
								 * params} ).then(function(response) {
								 * console.log(JSON.stringify(response.data));
								 * 
								 * ctrl.items = response.data; });
								 */
							}
							else {
								// otherwise set the list to empty
								ctrl.items = [];
								if(search.length > 0) {
									ctrl.hide = false;
								}
								else {
									ctrl.hide = true;
								}

							}
						}
						// if the setup is initialsed from url only then search
						// then items are filtered only
						else {
							console.log("refresh items")
							ctrl.items = ctrl.items;
							if(search.length > 0 && search.length < 3) {
								ctrl.hide = false;
							}
							else {
								ctrl.hide = true;
							}
						}

					};

					// if the config is set for load on init only then run init
					// function to load items only once
					if(ctrl.directive.loadOnInitOnly) {

						ctrl.initItems();
					}

					return ctrl;
				})

		// controller for select boolean
		.controller('selectBooleanController', function($http, select2Service) {

			var ctrl = this;

			ctrl.selection = null;
			ctrl.items = [ {
				text : ctrl.directive.trueText,
				url : "/turfclubPrograms/resources/img/greentick.png",
				val : true
			}, {
				text : ctrl.directive.falseText,
				url : "/turfclubPrograms/resources/img/redx.png",
				val : false
			} ];

			return ctrl;
		})
		// controller for select boolean
		.controller('selectEnumImageController', function($http, select2Service) {

			var ctrl = this;

			ctrl.selection = null;
			ctrl.items = [];

			for (var key in ctrl.directive.imageUrls) {
			    var value = ctrl.directive.imageUrls[key];
			    ctrl.items.push({
					text : value.title,
					url : value.imageUrl,
					val : value.key
				})
			    
			}
			return ctrl;
		})
		
		// controller for menuController
		.controller('menuBarController', function($http, select2Service) {

			var ctrl = this;

			return ctrl;
		})
		// controller for multi-select2 directive
		.controller(
				'multiSelect2Controller',
				function($http, select2Service) {

					var ctrl = this;

					// initialise the list of items to empty and the selection
					// to empty
					ctrl.items = [];
					if(ctrl.modelId !== undefined
							&& ctrl.modelValue != undefined) {
						console.log("Selection Empty");
						ctrl.selection = [];
					}
					else {
						console.log("Selection Null");
						ctrl.selection = null;
					}
					ctrl.hide = true;

					if(ctrl.modelValue !== undefined
							&& ctrl.modelValue.length > 0) {
						var array = ctrl.modelValue.split(",");
						console.log(ctrl.subKey)
						console.log(JSON.stringify(ctrl.modelValue));
						console.log(isArray(ctrl.modelValue) + " "
								+ ctrl.modelValue);
						for (var i = 0; i < array.length; i++) {
							var selected = {
								id : ctrl.modelId[i][ctrl.subKey],
								value : array[i]
							};
							ctrl.selection.push(selected);
						}
					}
					else {

						console.log("No size" + ctrl.subKey + " | "
								+ ctrl.modelValue + " | " + ctrl.modelId + "|");
					}

					// function to initialise the item list using the provided
					// url and no parameters
					ctrl.initItems = function() {

						ctrl.items = [];

						console.log("SELECT MULTI INIT")
						select2Service.getData(ctrl.directive.searchUrl).then(
								function(res) {
									// success
									// assign the data to the select2
									// model

									ctrl.items = select2Service.data;
									// console.log(JSON.stringify(ctrl.items));
								}, function(err) {
									alert("Server Error");
								});

					};

					// function to refresh the item list from the provided url
					// using a search parameter
					ctrl.refreshItems = function(search) {

						// if the load on init only flag is not set then requery
						// the url for new data with the search
						if(!ctrl.directive.loadOnInitOnly) {
							ctrl.items = [];

							if(search.length > 2) {

								// hide the message asking for 3 or more
								// characters
								ctrl.hide = true;
								var params = {
									chars : search
								};
								select2Service
										.getData(ctrl.directive.searchUrl,
												params)
										.then(
												function(res) {
													// success
													// assign the data to the
													// select2
													// model
													console
															.log("Data:"
																	+ JSON
																			.stringify(select2Service.data));
													ctrl.items = select2Service.data;

												}, function(err) {
													alert("Server Error");
												});
								/*
								 * var params = { chars : search }; return
								 * $http.get(ctrl.searchConfig.searchUrl,{params :
								 * params} ).then(function(response) {
								 * console.log(JSON.stringify(response.data));
								 * 
								 * ctrl.items = response.data; });
								 */
							}
							else {
								// otherwise set the list to empty
								ctrl.items = [];
								if(search.length > 0) {
									ctrl.hide = false;
								}
								else {
									ctrl.hide = true;
								}

							}
						}
						// if the setup is initialsed from url only then search
						// then items are filtered only
						else {
							console.log(isArray(ctrl.modelValue) + " "
									+ ctrl.modelValue);
							ctrl.items = ctrl.items;
							if(search.length > 0 && search.length < 3) {
								ctrl.hide = false;
							}
							else {
								ctrl.hide = true;
							}
						}

					};

					// if the config is set for load on init only then run init
					// function to load items only once
					if(ctrl.directive.loadOnInitOnly) {

						ctrl.initItems();
					}

					return ctrl;
				})
		// controller for the fillFieldsButton directive
		.controller(
				'fillFieldsButtonController',
				function(select2Service) {
					var ctrl = this;

					ctrl.buttonClass = 'btn btn-info';
					ctrl.buttonText = ctrl.directive.buttonText;
					ctrl.fieldData = {};

					ctrl.getData = function() {
						var params = {};
						var count = 0;

						ctrl.directive.paramNames.forEach(function addParam(
								name) {
							var key = ctrl.directive.paramFields[count];
							var convertedKey = "";
							if(key.toLowerCase().indexOf(".") > -1) {

								var keys = key.split(".");
								// console.log(attrs.fieldKey);

								for (var i = 0; i < keys.length; i++) {

									convertedKey += "['" + keys[i] + "']";

								}

								// console.log(ctrl.data);

							}
							else {
								convertedKey = "['" + key + "']";

							}

							// console.log(JSON.stringify(ctrl.data));
							console.log(convertedKey)
							// console.log(eval("ctrl.data" +
							// convertedKey));
							params[name] = eval("ctrl.data" + convertedKey);
							count++;
						});
						console.log(JSON.stringify(params));
						console.log(ctrl.directive.dataUrl);

						// get the data from the url with the params built above
						select2Service
								.getData(ctrl.directive.dataUrl, params)
								.then(
										function(res) {
											// success
											// assign the data to the select2
											// model
											console.log(JSON.stringify(res));
											ctrl.fieldData = res;

											if(ctrl.fieldData["noData"] != null) {
												// alert(res["noData"]);
												ctrl.buttonClass = 'btn btn-danger';
												ctrl.buttonText = ctrl.fieldData["noData"];
												ctrl.disabled = true;
											}
											else {
												ctrl.buttonClass = 'btn btn-info';
												ctrl.buttonText = ctrl.directive.buttonText;
												ctrl.disabled = false;
											}
										}, function(err) {
											alert("Server Error");
										});
					};

					ctrl.fillFields = function() {

						var count = 0;
						ctrl.directive.fieldsToFill
								.forEach(function setField(key) {
									console.log(key + "    "
											+ ctrl.directive.dataKeys[count] + " " + ctrl.fieldData[ctrl.directive.dataKeys[count]])
									ctrl.data[key] = ctrl.fieldData[ctrl.directive.dataKeys[count]];
									count++;
								});

					};

					ctrl.buttonClass = 'btn btn-danger';
					ctrl.buttonText = "Not Available";
					ctrl.disabled = true;
					return ctrl;
				})

		.controller('inputController', function($scope) {
			var ctrl = this;

			// console.log(JSON.stringify(ctrl.data));
			// console.log(JSON.stringify(ctrl.inputDirective));
			return ctrl;
		})

		.controller(
				'commentBoxController',
				function(jsonSubElementService) {
					var ctrl = this;

					ctrl.dataChange = function() {
						ctrl.change = true;
						console.log(JSON.stringify(ctrl.data));

					}
					ctrl.saveComment = function() {
						jsonSubElementService
								.saveData(ctrl.directive.saveUrl, ctrl.data)
								.then(
										function(res) {
											ctrl.change = false;
										},
										function(err) {
											alert("Server Error This Comment Did Not Save Correctly");
										});
					}
					return ctrl;
				})
		.controller(
				'commentBoxEnumToggleController',
				function(jsonSubElementService) {
					var ctrl = this;

					//console.log("Enum Model Value" + ctrl.modelValue);
					ctrl.dataChange = function() {
						ctrl.change = true;
						console.log(JSON.stringify(ctrl.data));

					}
					
					ctrl.saveComment = function() {
						jsonSubElementService
								.saveData(ctrl.directive.saveUrl, ctrl.data)
								.then(
										function(res) {
											ctrl.change = false;
										},
										function(err) {
											alert("Server Error This Comment Did Not Save Correctly");
										});
					}
					
					if(ctrl.directive.toggleButtonUrl != null){
						ctrl.toggleChange = function(){
							console.log("Toggle Change");
							if(ctrl.toggleOn){
								ctrl.toggleOn = false;
								ctrl.modelValue = ctrl.directive.toggleButtonOffValue;
								ctrl.classType = "toggle-btn";
								
								ctrl.glyphicon = " glyphicon-question-sign";
								console.log(JSON.stringify(ctrl.data));
								jsonSubElementService
								.saveData(ctrl.directive.toggleButtonUrl, ctrl.data)
								.then(
										function(res) {
											
											ctrl.modelValue = res.trainerVerifiedStatus;
											console.log(JSON.stringify(ctrl.modelValue));
											console.log(JSON.stringify(ctrl.data));
										},
										function(err) {
											alert("Server Error This Pending Status Did Not Save Correctly");
										});
							}
							else{
								ctrl.toggleOn = true;
								ctrl.modelValue = ctrl.directive.toggleButtonOnValue;
								ctrl.classType = "toggle-btn-on";
								ctrl.glyphicon = " glyphicon-remove-circle";
								console.log(JSON.stringify(ctrl.data));
								jsonSubElementService
								.saveData(ctrl.directive.toggleButtonUrl, ctrl.data)
								.then(
										function(res) {
											
											ctrl.modelValue = res.trainerVerifiedStatus;
											//console.log(JSON.stringify(ctrl.modelValue));
											//console.log(JSON.stringify(ctrl.data));
										},
										function(err) {
											alert("Server Error This Pending Status Did Not Save Correctly");
										});
								
							}
							
						}
						//console.log(ctrl.data[ctrl.directive.toggleButtonKey]);
						
						if(ctrl.directive.toggleButtonOnValue == ctrl.modelValue){
							
							ctrl.toggleOn = true;
							ctrl.classType = "toggle-btn-on";
							
							ctrl.glyphicon = " glyphicon-remove-circle";
						}

						else{
							ctrl.toggleOn = false;
							ctrl.classType = "toggle-btn";
							ctrl.glyphicon = " glyphicon-question-sign";
						}
						
						
					}
					
					
					
					return ctrl;
				})

		.controller('dynamicSearchController', function() {
			var ctrl = this;

			ctrl.getSearch = function(savedSearch) {
				var queryItems = savedSearch.queryItems;
				var search = "";
				for (var i = 0; i < queryItems.length; i++) {
					search += queryItems[i] + " ";
				}
				return search;
			}

			ctrl.search = ctrl.getSearch(ctrl.searchConfig.savedSearch);
			return ctrl;
		})

		// Display Directives Controllers

		.controller('dateDisplayController', function() {
			var ctrl = this;

			// console.log(JSON.stringify(ctrl.dateDirective));
			var key = ctrl.directive.key;
			if(key.toLowerCase().indexOf(".") > -1) {

				var keys = key.split(".");
				// console.log(attrs.fieldKey);

				for (var i = 0; i < keys.length; i++) {

					convertedKey += "['" + keys[i] + "']";

				}

				// console.log(ctrl.data);

			}
			else {
				convertedKey = "['" + key + "']";

			}

			// console.log(JSON.stringify(ctrl.data));
			// console.log(eval("ctrl.data" + convertedKey))
			// console.log(eval("ctrl.data" +
			// convertedKey));
			ctrl.text = eval("ctrl.data" + convertedKey);

			return ctrl;

		})

		.controller('spanController', function() {
			var ctrl = this;

			// console.log(JSON.stringify(ctrl.spanDirective));
			var key = ctrl.directive.key;
			var values = getKeyValue(ctrl.data, key);
			// console.log(JSON.stringify(values));
			var text = "";
			for (var i = 0; i < values.length; i++) {
				if((i + 1) % ctrl.directive.splitAfter == 0 && i != 0) {
					if(values[i] != null && values[i] != "") {
						text += ctrl.directive.splitter + values[i];
					}

				}
				else if((i + 1) % ctrl.directive.splitAfter == 0) {
					if(values[i] != null && values[i] != "") {
						text += values[i];
					}
				}
				else {
					if(values[i] != null && values[i] != "") {
						text += values[i] + " ";
					}
				}

			}
			// console.log("'" + text + "'");

			if(text.trim() == "null") {
				ctrl.text = "";
			}
			else {
				ctrl.text = text;
			}

			return ctrl;

		})

		.controller('trueFalseTickController', function() {
			var ctrl = this;

			
		//console.log(ctrl.data);
			//console.log(ctrl.modelValue);

			return ctrl;

		})

		.controller('enumToImageController', function() {
			var ctrl = this;

			//console.log("ENUM CONTROL" + ctrl.modelValue);
			ctrl.update = function(){
				for ( var i in ctrl.directive.enumToImageMap) {
					if(ctrl.directive.enumToImageMap.hasOwnProperty(i)) {
						//console.log(i + " " + ctrl.text);
						if(i == ctrl.modelValue) {
							ctrl.imageUrl = ctrl.directive.enumToImageMap[i];
							//console.log(ctrl.imageUrl);
						}

					}
				}
			}
			ctrl.update();

			return ctrl;

		})

		.controller('contactDetailsController', function() {
			var ctrl = this;

			// console.log(JSON.stringify(ctrl.spanDirective));
			if(ctrl.directive.nameKey !== null) {
				var key = ctrl.directive.nameKey;
				var values = getKeyValue(ctrl.data, key);
				var text = values[0];

				if(text == null) {
					ctrl.name = "";
				}
				else {
					ctrl.name = text;
				}
			}
			if(ctrl.directive.phoneKey !== null) {
				var key = ctrl.directive.phoneKey;
				var values = getKeyValue(ctrl.data, key);
				var text = values[0];

				if(text == null) {
					ctrl.phone = "";
				}
				else {
					ctrl.phone = text;
				}
			}
			if(ctrl.directive.mobileKey !== null) {
				key = ctrl.directive.mobileKey;
				values = getKeyValue(ctrl.data, key);
				text = values[0];

				if(text == null) {
					ctrl.mobile = "";
				}
				else {
					ctrl.mobile = text;
				}
			}

			if(ctrl.directive.faxKey !== null) {
				key = ctrl.directive.faxKey;
				values = getKeyValue(ctrl.data, key);
				text = values[0];

				if(text == null) {
					ctrl.fax = "";
				}
				else {
					ctrl.fax = text;
				}
			}

			if(ctrl.directive.emailKey !== null) {
				key = ctrl.directive.emailKey;
				values = getKeyValue(ctrl.data, key);
				text = values[0];

				if(text == null) {
					ctrl.email = "";
				}
				else {
					ctrl.email = text;
				}
			}

			if(ctrl.directive.extraKey !== null) {
				//console.log(ctrl.directive.extraKey);
				key = ctrl.directive.extraKey;
				//console.log("getKeyValue");
				values = getKeyValue(ctrl.data, key);
				//console.log(JSON.stringify(values));
				var text = "";
				for (var i = 0; i < values.length; i++) {

					if((i + 1) == values.length) {
						text += values[i];

					}

					else {

						text += values[i] + ctrl.directive.extraKeySplitter;

					}

				}
				//console.log("'" + text + "'");

				if(text.trim() == "null" || text.indexOf("null") > -1) {
					ctrl.extra = "";
				}
				else {
					ctrl.extra = text;
				}
			}

			return ctrl;

		})

		.controller('saveModalController',
				function($scope, $modalInstance, message) {

					$scope.message = message;
					$scope.ok = function() {
						console.log("close")
						$modalInstance.close("Done");
					}

				})
		.controller(
				'sortByController',
				function() {
					var ctrl = this;

					ctrl.setSort = function() {

						console.log(JSON.stringify(ctrl.search));

						maxPriority = 1;
						for ( var key in ctrl.search.orderByFields) {
							if(ctrl.search.orderByFields[key].fieldPriority >= maxPriority) {
								maxPriority = ctrl.search.orderByFields[key].fieldPriority + 1;
							}
							// console.log(" " +
							// JSON.stringify(ctrl.search.orderByFields[key]) +
							// " is #" + key); // "User john is #234"
						}

						if(ctrl.search.orderByFields[ctrl.column.title]) {

							if(ctrl.search.orderByFields[ctrl.column.title].fieldOrder == "ASC") {
								ctrl.search.orderByFields[ctrl.column.title].fieldOrder = "DESC";
							}
							else {
								ctrl.search.orderByFields[ctrl.column.title].fieldOrder = "ASC";
							}

							console.log("Exists");
						}
						else {

							ctrl.search.orderByFields[ctrl.column.title] = {
								fieldTitle : ctrl.column.title,
								fieldOrder : "ASC",
								fieldPriority : maxPriority
							}
						}
						ctrl.refresh();
					}
					ctrl.removeSort = function() {

						var priorityRemoved = ctrl.search.orderByFields[ctrl.column.title].fieldPriority;
						for ( var key in ctrl.search.orderByFields) {
							if(ctrl.search.orderByFields[key].fieldPriority > priorityRemoved) {
								ctrl.search.orderByFields[key].fieldPriority = ctrl.search.orderByFields[key].fieldPriority - 1;
							}
							// console.log(" " +
							// JSON.stringify(ctrl.search.orderByFields[key]) +
							// " is #" + key); // "User john is #234"
						}

						delete ctrl.search.orderByFields[ctrl.column.title];
						ctrl.refresh();
					}

					return ctrl;
				})
.controller('uploadModalController', function($scope, $modalInstance, $modal, item) {

		$scope.title = item.title + item.id;
		$scope.url = item.url;
		$scope.id = item.id;
		//console.log(item.trainerId);
		$scope.close = function () {
		        console.log("close")
		    	  $modalInstance.dismiss('cancel');
		      };
			

		})
		
.controller('deleteModalController', function($scope, $modalInstance, $modal, item, jsonService) {

		$scope.title = item.title;
		$scope.warningText = item.warning;   
		$scope.id = item.id;
		$scope.deleteClick = function () {
	        console.log(item.deleteUrl + " " + item.id)
			jsonService.getData(item.deleteUrl, item.id)
	  		.then(function(res){
	  			
	  				console.log(res);
	  				$modalInstance.dismiss('cancel');
	  		        item.refresh();
	  			
	  			//console.log(JSON.stringify(ctrl.data));
	  			//console.log(JSON.stringify(ctrl.config));
	  			
	  		}, function(err){
	  			$modalInstance.dismiss('cancel');
	  			alert("Server Error");
	  		})
	    	  
	    	  
	      };
		

	
		$scope.close = function () {
		        console.log("close")
		    	  $modalInstance.dismiss('cancel');
		      };
			

		})
		.controller('fileUploadController', [
            '$scope', '$http', '$filter', '$window',
            function ($scope, $http, $modalInstance) {
                
            	$scope.init = function(userName, url){
            		
            		$scope.userName = userName;
            		console.log(userName);
                	$scope.loadingFiles = true;
								                    $http
										.get(
												url + userName)
                        .then(
                            function (response) {
                                $scope.loadingFiles = false;
                                console.log(JSON.stringify(response.data.files));
                                $scope.queue = response.data.files || [];
                            },
                            function () {
                                $scope.loadingFiles = false;
                            }
                        );
                    $scope.options = {
                            url: url + userName,
                            autoUpload : true
                        };
                }
            	
            	

                    $scope.loadingFiles = false;
                   
              
            }
        ])
        .controller('FileDestroyController', [
            '$scope', '$http',
            function ($scope, $http) {
                //console.log("cancel");
            	var file = $scope.file,
                    state;
                if (file.url) {
                    file.$state = function () {
                        return state;
                    };
                    file.$destroy = function () {
                        state = 'pending';
                        return $http({
                            url: file.deleteUrl,
                            method: file.deleteType
                        }).then(
                            function () {
                                state = 'resolved';
                                $scope.clear(file);
                            },
                            function () {
                                state = 'rejected';
                            }
                        );
                    };
                } else if (!file.$cancel && !file._index) {
                    file.$cancel = function () {
                        $scope.clear(file);
                    };
                }
            }
        ])
		
		.controller('buttonController', function($location, $window, $modal, $routeParams ,jsonService, $scope, $timeout) {
			var ctrl = this;

			// console.log(JSON.stringify(ctrl.buttonDirective));
			var key = ctrl.directive.key;
			if(key.toLowerCase().indexOf(".") > -1) {

				var keys = key.split(".");
				// console.log(attrs.fieldKey);

				for (var i = 0; i < keys.length; i++) {

					convertedKey += "['" + keys[i] + "']";

				}

				// console.log(ctrl.data);

			}
			else {
				convertedKey = "['" + key + "']";

			}

			// console.log(JSON.stringify(ctrl.data));
			// console.log(convertedKey)
			// console.log(eval("ctrl.data" +
			// convertedKey));
			ctrl.id = eval("ctrl.data" + convertedKey);
			
			// open the date picker if the button is clicked firing this event
			ctrl.event = function() {
				if(ctrl.directive.functionType == "EXTERNALURLNEWTAB") {
					$window.open(ctrl.directive.url + ctrl.id, '_blank');
				}
				else if(ctrl.directive.functionType == "EXTERNALURL"){
					$window.location.href= ctrl.directive.url + ctrl.id;
				}
				else if(ctrl.directive.functionType == "MODAL"){
					ctrl.showModal(ctrl.id, ctrl.directive.modal, ctrl.refresh);
				}
				else if(ctrl.directive.functionType == "ONOFF"){
					//json service to url and then add the refreshed data to the table row
				
					if(ctrl.modelValue == false){
						console.log("SET TRUE");
						
						
						ctrl.modelValue = true;
						$timeout(function(){
							if(ctrl.directive.onOffUseRouteParam){
								console.log("Route" +  $routeParams.id);
								jsonService.getData(ctrl.directive.url, ctrl.data, $routeParams.id)
						  		.then(function(res){
						  			
						  			console.log(ctrl.data);
						  				

						  		}, function(err){
						  			
						  			alert("Server Error");
						  		})
						  		
							}
							else{
								jsonService.getData(ctrl.directive.url, ctrl.data)
						  		.then(function(res){
						  			
						  		

						  		}, function(err){
						  			
						  			alert("Server Error");
						  		})
							}
						});
						
						
					}
					else{
						ctrl.modelValue = false;
						console.log("SET FALSE");
						$timeout(function(){
							if(ctrl.directive.onOffUseRouteParam){
								console.log("Route" +  $routeParams.id);
								jsonService.getData(ctrl.directive.url, ctrl.data, $routeParams.id)
						  		.then(function(res){
						  			
						  			console.log(ctrl.data);
						  				

						  		}, function(err){
						  			
						  			alert("Server Error");
						  		})
						  		
							}
							else{
								jsonService.getData(ctrl.directive.url, ctrl.data)
						  		.then(function(res){
						  			
						  		

						  		}, function(err){
						  			
						  			alert("Server Error");
						  		})
							}
						});
					}
					
					
				}
				else {
					$location.path(ctrl.directive.url + ctrl.id);
				}

				
			};

			
			 ctrl.showModal = function(id, modalDirective, refresh) {

				 console.log(JSON.stringify(modalDirective));
				 if(modalDirective.type == "UPLOAD"){
					 ctrl.opts = {
						        backdrop: true,
						        backdropClick: false,
						        dialogFade: false,
						        keyboard: false,
						        templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/modal/file-upload-modal-view.html',
						        controller : "uploadModalController",
						        resolve: {} // empty storage
						          };
					 ctrl.opts.resolve.item = function() {
				            return angular.copy(
				                                {id: id, url: modalDirective.uploadUrl, title : modalDirective.uploadTitle, refresh : refresh  }
				                          ); // pass name to resolve storage
				        }
				 }
				 else if(modalDirective.type == "DELETE"){
					 ctrl.opts = {
						        backdrop: true,
						        backdropClick: false,
						        dialogFade: false,
						        keyboard: false,
						        templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/modal/delete-modal-view.html',
						        controller : "deleteModalController",
						        resolve: {
						        	
						        	
						        } // empty storage
						          }; 
					 ctrl.opts.resolve.item = function() {
				            return angular.copy(
				                                {id: id, deleteUrl: modalDirective.deleteUrl, title : modalDirective.title, warning: modalDirective.warningText , refresh : refresh }
				                          ); // pass name to resolve storage
				        }
				 }
				 
			        


			        

			          var modalInstance = $modal.open(ctrl.opts);

			          modalInstance.result.then(function(){
			            //on ok button press 
			          },function(){
			            //on cancel button press
			            console.log("Modal Closed");
			          });
			      };          
			
			
			return ctrl;

		}).controller('multiButtonController', function() {
			var ctrl = this;

			return ctrl;

		})
		
		.controller(
				'trueFalseWithButtonController',
				function($location, $window) {
					var ctrl = this;

					// console.log(JSON.stringify(ctrl.buttonDirective));
					var key = ctrl.directive.key;
					if(key.toLowerCase().indexOf(".") > -1) {

						var keys = key.split(".");
						// console.log(attrs.fieldKey);

						for (var i = 0; i < keys.length; i++) {

							convertedKey += "['" + keys[i] + "']";

						}

						// console.log(ctrl.data);

					}
					else {
						convertedKey = "['" + key + "']";

					}

					// console.log(JSON.stringify(ctrl.data));
					// console.log(convertedKey)
					// console.log(eval("ctrl.data" +
					// convertedKey));
					ctrl.id = eval("ctrl.data" + convertedKey);

					// open the date picker if the button is clicked firing this
					// event
					ctrl.event = function() {
						if(ctrl.directive.functionType == "EXTERNALURL") {
							$window
									.open(ctrl.directive.url + ctrl.id,
											'_blank');
						}
						else {
							$location.path(ctrl.directive.url + ctrl.id);
						}

						console.log("ID:" + ctrl.directive.url + ctrl.id);
					};

					return ctrl;

				})

// check if input json object is an array
function isArray(what) {
	return Object.prototype.toString.call(what) === '[object Array]';
}