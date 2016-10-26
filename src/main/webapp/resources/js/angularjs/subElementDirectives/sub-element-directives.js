angular
		.module('sub-elements')
		// sets up element as the directive type set in the config for a
		// particular element
		.directive(
				'dynamicElement',
				function($compile, $parse) {
					return {
						restrict : 'A',
						link : {

							pre : function(scope, element, attrs) {

								var directiveType = scope
										.$eval(attrs.dynamicElement);
								element.attr(directiveType, "");
								// remove the attribute to avoid indefinite loop
								element.removeAttr("dynamic-element");
								element.removeAttr("data-dynamic-element");
								// console.log(scope.$eval(attrs.dynamicElement));

								// check what directive type is used and get the
								// key based on the type
								var key = null;
								var idKey = null;
								var linkedKey = null;
								// console.log("DIRECTIVE" + directiveType);
								switch (directiveType)
								{
								case "INPUT":
									key = scope.$eval(attrs.directive).key;
									break;
								case "SELECT2":
									key = scope.$eval(attrs.directive).key;
									idKey = scope.$eval(attrs.directive).idKey;

									break;
								case "SELECTBOOLEAN":

									key = scope.$eval(attrs.directive).key;
									// console.log("SELECTBOOLEAN" + key);
									break;
								case "SELECTENUMIMAGE":

									key = scope.$eval(attrs.directive).key;
									// console.log("SELECTBOOLEAN" + key);
									break;
								case "SELECT2MULTI":
									// console.log(JSON.stringify(scope.$eval(attrs.directive)));
									key = scope.$eval(attrs.directive).key;
									// console.log("SELECTMulti" + key);
									idKey = scope.$eval(attrs.directive).idKey;
									break;
								case "SELECT2LINKED":
									key = scope.$eval(attrs.directive).key;
									idKey = scope.$eval(attrs.directive).idKey;
									linkedKey = scope.$eval(attrs.directive).linkedFormFieldModel;
									break;
								case "DATEPICKERDIR":
									key = scope.$eval(attrs.directive).key;
									break;
								case "ENUMTOIMAGE":
									key = scope.$eval(attrs.directive).key;

									break;
								case "TRUEFALSETICK":
									key = scope.$eval(attrs.directive).key;

									//console.log(key);
									break;
								case "COMMENTBOXENUMTOGGLE":
									key = scope.$eval(attrs.directive).toggleButtonKey;

									break;
								case "BUTTON":
									key = scope.$eval(attrs.directive).onOffKey;

									break;
								case "FILLFIELDSBUTTON":

									break;

								}

								// if the key is not null set the data model
								// value
								if(key != null) {
									// console.log("Key:" + key);
									// if the key contains a space split it and
									// get values using each split key
									var convertedKey = "";
									if(key.toLowerCase().indexOf(" ") > -1) {

										var values = getKeyValue(scope
												.$eval(attrs.data), key);

										var array = [];
										var string = "";
										for (var i = 0; i < values.length; i++) {

											// ////console.log(values[i]);

											// ////console.log("Modulus:" + i +
											// " " + (i%2 == 0));
											if((i + 1) % 2 == 0) {
												string += values[i];
												array.push(string);
												if(values.length > 2) {
													string = "";
												}

											}
											else {
												string += values[i] + " ";
											}
										}

										if(array.length > 1) {
											// ////console.log("array" + array);
											// console.log(array)
											element.attr("data-model-value",
													"\"" + array + "\"");
										}
										else {

											element.attr("data-model-value",
													"\"" + string + "\"");
										}

									}
									// otherwise it is a key for a single item
									else {
										if(key.toLowerCase().indexOf(".") > -1) {

											var keys = key.split(".");
											// //console.log(attrs.fieldKey);

											for (var i = 0; i < keys.length; i++) {

												convertedKey += "['" + keys[i]
														+ "']";

											}

											element.attr("data-model-value",
													attrs.data + convertedKey);

											// //console.log(ctrl.data);

										}
										else {
											convertedKey = "['" + key + "']";
											element.attr("data-model-value",
													attrs.data + convertedKey);
										}
									}
									//console.log(convertedKey+ "Value "+ JSON.stringify(scope.$eval(element.attr("data-model-value"))));

								}
								// if the idKey is not null then set the data
								// model id (only used on certain fields)
								if(idKey != null) {
									// console.log("IDKey:" + idKey);
									if(idKey.toLowerCase().indexOf(".") > -1) {
										var convertedKey = "";
										var keys = idKey.split(".");
										// //console.log(attrs.fieldKey);
										var sub = false;
										var subKey = "";
										for (var i = 0; i < keys.length; i++) {

											if(sub) {

												subKey = "" + keys[i] + "";

											}
											else {
												convertedKey += "['" + keys[i]
														+ "']";
											}

											if(isArray(scope.$eval(attrs.data
													+ convertedKey))
													&& !sub) {
												// console.log("sub array");
												sub = true;

											}
										}

										// console.log(convertedKey);
										// console.log(subKey);
										// console.log(scope.$eval(attrs.data +
										// convertedKey));
										element.attr("data-model-id",
												attrs.data + convertedKey);
										element.attr("data-sub-key", "\""
												+ subKey + "\"");

										// //console.log(ctrl.data);

									}
									else {

										var convertedKey = "['" + idKey + "']";
										// console.log("Single Key" +
										// convertedKey);
										element.attr("data-model-id",
												attrs.data + convertedKey);
									}

								}
								// if the linkedId is not null then set the
								// linked form field model
								if(linkedKey != null) {
									if(linkedKey.toLowerCase().indexOf(".") > -1) {
										var key = "";
										var keys = linkedKey.split(".");
										// //console.log(attrs.fieldKey);

										for (var i = 0; i < keys.length; i++) {

											key += "['" + keys[i] + "']";

										}
										// console.log("Link " + key);

										element.attr("linked-form-field-model",
												attrs.data + key);

										// //console.log(ctrl.data);

									}
									else {
										var key = "['"
												+ scope
														.$eval(attrs.select.linkedFormFieldModel)
												+ "']";
										// console.log("Link " + key);
										element.attr("linked-form-field-model",
												attrs.data + key);
									}
								}

								$compile(element)(scope);
							}

						}
					};
				})

		// select2 directive
		.directive(
				'select2',
				function() {
					return {
						restrict : 'A',
						scope : {
							directive : '=',
							modelValue : "=",
							modelId : "=",
							linkedFormFieldModel : "="
						},
						controller : "select2Controller",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-select2-view.html',
						link : function(scope, element, attrs, ctrl) {

							// //console.log(ctrl.modelId);
							// //console.log(ctrl.modelValue);
							scope.$watch("ctrl.selection", function(v) {
								if(v !== null && v !== undefined
										&& v.id != undefined) {

									ctrl.modelId = ctrl.selection.id;
									ctrl.modelValue = ctrl.selection.value;
									// ctrl.searchConfig.savedSearch.queryItems
									// = [];
									// console.log(v.id);
								}

							});
						}
					};
				})
		// select2 linked directive
		.directive(
				'select2linked',
				function() {
					return {
						restrict : 'A',
						scope : {
							directive : '=',
							modelValue : "=",
							modelId : "=",
							linkedFormFieldModel : "="
						},
						controller : "linkedSelect2Controller",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-select2-linked-view.html',
						link : function(scope, element, attrs, ctrl) {

							scope.$watch("ctrl.selection", function(v) {

								if(v !== undefined && v != null
										&& v.id != undefined) {

									ctrl.modelId = ctrl.selection.id;
									ctrl.modelValue = ctrl.selection.value;
									// ctrl.searchConfig.savedSearch.queryItems
									// = [];
									// //console.log(v.id);
								}
								else {
									ctrl.modelId = null;
									ctrl.modelValue = null;
								}

							});
							scope.$watch("ctrl.linkedFormFieldModel", function(
									v) {
								// console.log("Linked Value Change" + v);
								if(v !== undefined && ctrl.items.length > 0) {

									ctrl.refreshItems();
								}

							});
						}
					};
				})
		// select2 directive
		.directive(
				'select2multi',
				function() {
					return {
						restrict : 'A',
						scope : {
							directive : '=',
							modelValue : "=",
							modelId : "=",
							subKey : "=",
							refresh : "&"
						},
						controller : "multiSelect2Controller",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-multi-select2-view.html',
						link : function(scope, element, attrs, ctrl) {

							scope
									.$watch(
											attrs.ngModel,
											function(v) {
												if(v !== undefined && v != null) {

													// console.log(JSON.stringify(v));
													// console.log(JSON.stringify(ctrl.modelId));
													var itemChanged = false;
													if(ctrl.modelId !== undefined) {
														// remove items from
														// data not on input
														// list
														for (var j = 0; j < ctrl.modelId.length; j++) {
															var onList = false;

															for (var i = 0; i < v.length; i++) {
																if(v[i].id == ctrl.modelId[j][ctrl.subKey]) {
																	onList = true;
																}
															}
															if(!onList) {
																// console.log("Remove:
																// " )
																itemChanged = true;
																ctrl.modelId
																		.splice(
																				j,
																				1);
															}

															// ctrl.searchConfig.savedSearch.queryItems.push(v[i].id);

														}

													}

													// add input items to data
													// not on list
													for (var i = 0; i < v.length; i++) {
														var onList = false;

														for (var j = 0; j < ctrl.modelId.length; j++) {
															if(v[i].id == ctrl.modelId[j][ctrl.subKey]) {
																onList = true;

															}
														}
														if(!onList) {
															var obj = {};
															obj[ctrl.subKey] = v[i].id;
															// console.log("Add
															// to list" +
															// v[i].id)
															// console.log(JSON.stringify(ctrl.modelId));
															itemChanged = true;
															ctrl.modelId
																	.push(obj);
															// console.log(JSON.stringify(ctrl.modelId));
														}

														// ctrl.searchConfig.savedSearch.queryItems.push(v[i].id);

													}
													if(itemChanged) {
														ctrl.refresh();
													}

												}
												else {
													ctrl.modelId = [];
													console.log("empty")
													// ctrl.refresh();
												}

											});
						}
					};
				})

		.directive(
				'selectboolean',
				function() {
					return {
						restrict : 'A',
						scope : {
							directive : '=',
							modelValue : '=',
							refresh : "&"
						},
						controller : "selectBooleanController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-select-boolean-view.html',
						link : function(scope, element, attrs, ctrl) {

							// //console.log(ctrl.modelId);
							// //console.log(ctrl.modelValue);
							scope
									.$watch(
											"ctrl.selection",
											function(v) {
												// console.log(JSON.stringify(ctrl.modelValue));
												if(v !== null
														&& v !== undefined) {

													ctrl.modelValue.ssBoolean = ctrl.selection.val;
													ctrl.modelValue.inUse = true;
													console
															.log(ctrl.modelValue);
													ctrl.refresh();
													// ctrl.searchConfig.savedSearch.queryItems
													// = [];
													// console.log(v.id);
												}
												else {
													ctrl.modelValue.inUse = false;
													ctrl.refresh();
												}

											});
							scope
									.$watch(
											"ctrl.modelValue",
											function(v) {
												// console.log("MODEL VALUE
												// CHANGE " + ctrl.modelValue);
												if(ctrl.modelValue != null) {
													if(ctrl.modelValue.ssBoolean == true
															&& ctrl.modelValue.inUse == true) {
														ctrl.selection = {
															text : ctrl.directive.trueText,
															url : "/turfclubPrograms/resources/img/greentick.png",
															val : true
														};
													}
													else if(ctrl.modelValue.inUse == true) {
														ctrl.selection = {
															text : ctrl.directive.falseText,
															url : "/turfclubPrograms/resources/img/redx.png",
															val : false
														};
													}
													else {
														ctrl.selection = null;
													}
												}
												else {
													ctrl.selection = null;
												}
											});

						}
					};
				})

				.directive(
				'selectenumimage',
				function() {
					return {
						restrict : 'A',
						scope : {
							directive : '=',
							modelValue : '=',
							refresh : "&"
						},
						controller : "selectEnumImageController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-select-enum-view.html',
						link : function(scope, element, attrs, ctrl) {

							// //console.log(ctrl.modelId);
							// //console.log(ctrl.modelValue);
							scope
									.$watch(
											"ctrl.selection",
											function(v) {
												// console.log(JSON.stringify(ctrl.modelValue));
												if(v !== null
														&& v !== undefined) {

													console.log(ctrl.selection.val);
													ctrl.modelValue.ssEnum = ctrl.selection.val;
													ctrl.modelValue.inUse = true;
													console
															.log(ctrl.modelValue);
													ctrl.refresh();
													// ctrl.searchConfig.savedSearch.queryItems
													// = [];
													// console.log(v.id);
												}
												else {
													ctrl.modelValue.inUse = false;
													ctrl.refresh();
												}

											});
							scope
									.$watch(
											"ctrl.modelValue",
											function(v) {
												 console.log("MODEL VALUE CHANGE " + ctrl.modelValue);
												if(ctrl.modelValue != null) {
													if(ctrl.modelValue.ssBoolean == true
															&& ctrl.modelValue.inUse == true) {
														ctrl.selection = {
															text : ctrl.directive.trueText,
															url : "/turfclubPrograms/resources/img/greentick.png",
															val : true
														};
													}
													else if(ctrl.modelValue.inUse == true) {
														ctrl.selection = {
															text : ctrl.directive.falseText,
															url : "/turfclubPrograms/resources/img/redx.png",
															val : false
														};
													}
													else {
														ctrl.selection = null;
													}
												}
												else {
													ctrl.selection = null;
												}
											});

						}
					};
				})
		.directive('requireMultiple', function() {
			return {
				require : 'ngModel',
				link : function postLink(scope, element, attrs, ngModel) {

					ngModel.$validators.required = function(value) {
						// console.log(value);
						// console.log(attrs["requireMultiple"]);
						if(attrs["requireMultiple"] == "true") {
							// console.log("Using");
							// console.log(angular.isArray(value));
							return angular.isArray(value) && value.length > 0;
						}
						else {
							return true;
						}

					};
				}
			};
		})

		.directive(
				'datepickerdir',
				function() {
					return {
						restrict : 'A',
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-datepicker-view.html',
						scope : {

							preLabel : "@",
							modelValue : "=",
							directive : "=",
							refresh : "&"
						},
						bindToController : true,
						controller : "datePickerController",
						controllerAs : "ctrl",
						link : function(scope, element, attrs, ctrl) {
							// watch for change in the value
							scope.$watch("ctrl.modelValue", function(v) {
								if(v !== undefined) {
									// run service with new value here

									// ctrl.refresh();
								}

							});
						}
					}
				})

		.directive(
				'input',
				function($compile) {
					return {
						restrict : 'A',
						scope : {
							data : "=",
							directive : "=",
							modelValue : "="
						},
						controller : "inputController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,

						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-input-view.html',
						link : {

							post : function(scope, element, attrs, ctrl) {
								// //console.log(scope.$eval(attrs.ngModel));

								scope
										.$watch(
												"ctrl.modelValue",
												function(v) {
													if(v !== undefined) {
														console
																.log('value changed, new value is: '
																		+ v);
													}

												});
							}
						}
					};
				})

		.directive(
				'commentbox',
				function($compile) {
					return {
						restrict : 'A',
						scope : {
							data : "=",
							directive : "=",

						},
						controller : "commentBoxController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,

						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-comment-box-view.html',

					};
				})
		.directive(
				'commentboxenumtoggle',
				function($compile) {
					return {
						restrict : 'A',
						scope : {
							data : "=",
							directive : "=",
							modelValue : '='
						},
						controller : "commentBoxEnumToggleController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,

						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-comment-box-with-enum-toggle-view.html',

					};
				})
		.directive(
				'fillfieldsbutton',
				function($compile) {
					return {
						restrict : 'A',
						scope : {
							data : "=",
							directive : "=",
						},
						controller : "fillFieldsButtonController",
						controllerAs : "ctrl",
						bindToController : true,
						replace : true,

						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/input/sub-element-fill-button-view.html',
						link : {

							post : function(scope, element, attrs, ctrl) {
								console.log(ctrl.directive.paramFields[0]);

								ctrl.directive.paramFields
										.forEach(function watchParam(param) {
											var key = param;
											var convertedKey = "";
											if(key.toLowerCase().indexOf(".") > -1) {

												var keys = key.split(".");
												// //console.log(attrs.fieldKey);

												for (var i = 0; i < keys.length; i++) {

													convertedKey += "['"
															+ keys[i] + "']";

												}

												// //console.log(ctrl.data);

											}
											else {
												convertedKey = "['" + key
														+ "']";

											}

											scope
													.$watch(
															"ctrl.data"
																	+ convertedKey,
															function(v) {
																if(v !== undefined) {
																	console
																			.log('Data changed: '
																					+ v);
																	ctrl
																			.getData();
																}

															});

										});
							}

						}
					};
				})

		.directive(
				'datepickerfromtodir',
				function($compile) {
					return {
						restrict : 'A',
						scope : {
							searchConfig : '='

						},

						template : '<span><datepickerdir data-pre-label="From:" date="searchConfig.savedSearch.dateFrom"></datepickerdir><datepickerdir data-date="searchConfig.savedSearch.dateTo" data-pre-label="To:"></datepickerdir></span>',
						link : {
							pre : function(scope, element, attrs) {

								console
										.log(scope.searchConfig.savedSearch.dateFrom);
								// //console.log("dynamic d compile");
								// $compile(element)(scope);
							}
						}
					}
				})

		.directive(
				'datepickerLocaldate',
				[
						'$parse',
						function($parse) {
							var directive = {
								restrict : 'A',
								require : [ 'ngModel' ],
								link : link
							};
							return directive;

							function link(scope, element, attr, ctrls) {
								var ngModelController = ctrls[0];

								// called with a JavaScript Date object when
								// picked from the datepicker
								ngModelController.$parsers
										.push(function(viewValue) {
											var dateformat = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)(?:0?2|(?:Feb))\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/;
											// Match the date format through
											// regular expression
											// console.log(viewValue);
											if(viewValue != null
													&& viewValue.toString()
															.match(dateformat)) {

												// undo the timezone adjustment
												// we did
												// during the formatting

												// console.log("format date");
												viewValue
														.setMinutes(viewValue
																.getMinutes()
																- viewValue
																		.getTimezoneOffset());
												// we just want a local date in
												// ISO format
												return viewValue.toISOString()
														.substring(0, 10);
											}
											else if(viewValue instanceof Date) {

												// console.log("no format
												// date");
												viewValue
														.setMinutes(viewValue
																.getMinutes()
																- viewValue
																		.getTimezoneOffset());
												return viewValue.toISOString()
														.substring(0, 10);
											}
											else {
												return viewValue;
											}

										});
							}
						} ])

		// DISPLAY DIRECTIVES

		// button directive
		.directive(
				'button',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',
							modelValue : "=",
							refresh : '&'
						},
						controller : "buttonController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/button-view.html',
						link : function(scope, element, attrs, ctrl) {

						}

					};
				})

		// multi button directive
		.directive(
				'multibutton',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',

						},
						controller : "multiButtonController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/multi-button-view.html',
						link : function(scope, element, attrs, ctrl) {

						}

					};
				})
		// true false directive with button
		.directive(
				'truefalsetickwithbutton',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',

						},
						controller : "trueFalseWithButtonController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/true-false-with-button-view.html',
						link : function(scope, element, attrs, ctrl) {

						}

					};
				})
		.directive(
				'span',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',

						},
						controller : "spanController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/span-view.html',

					};
				})
		.directive(
				'contactdetails',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',

						},
						controller : "contactDetailsController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/contact-details-view.html',

					};
				})

		.directive(
				'datespan',
				function() {
					return {
						restrict : 'A',
						scope : {
							data : '=',
							directive : '=',

						},
						controller : "dateDisplayController",
						controllerAs : "ctrl",
						bindToController : true,
						templateUrl : "/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/date-display-view.html",

					};
				})
		.directive(
				'truefalsetick',
				function() {
					return {
						restrict : 'A',
						replace : true,
						controller : "trueFalseTickController",
						controllerAs : "trueFalseCtrl",
						bindToController : true,
						scope : {
							data : '=',
							directive : '=',
							modelValue : '='
						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/true-false-tick.html',

					};
				})
		.directive(
				'enumtoimage',
				function() {
					return {
						restrict : 'A',
						replace : true,
						controller : "enumToImageController",
						controllerAs : "ctrl",
						bindToController : true,
						scope : {
							data : '=',
							directive : '=',
							modelValue : '='
						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/enum-to-image-view.html',

						link : function(scope, element, attrs, ctrl) {

							scope.$watch("ctrl.modelValue", function(v) {
								if(v !== undefined) {
									ctrl.update();
									//console.log("Update Enum Image" + v);
								}

							})
						}
					};
				})
		.directive(
				'sortbyheader',
				function() {
					return {
						restrict : 'A',
						replace : true,
						controller : "sortByController",
						controllerAs : "sortByCtrl",
						bindToController : true,
						scope : {
							column : '=',
							search : "=",
							refresh : "&"
						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/display/sortby-header.html',

					};
				})
		.directive(
				'menuBar',
				function() {
					return {
						restrict : 'A',
						replace : true,
						controller : "menuBarController",
						controllerAs : "sortByCtrl",
						bindToController : true,
						scope : {

						},
						templateUrl : '/turfclubPrograms/resources/js/angularjs/subElementDirectives/templates/menu/menu-bar.html',

					};
				})

function getKeyValue(input, key) {
	var values = [];

	/*
	 * //if the key contains a dash - get the object using the part of the key
	 * before the dash if(key.toLowerCase().indexOf("-") > -1){ var keys =
	 * key.split("-"); var subinput = input[keys[0]]; var subkey = ""; for(var
	 * i=1; i<keys.length; i++){ if(i != keys.length-1){ subkey += keys[i] +
	 * "-"; } else{ subkey += keys[i]; } } getKeyValue(subinput, subkey); }
	 */
	// if the key has a dot then get the object using the part of the key before
	// the dot
	if(key.toLowerCase().indexOf(".") > -1) {
		var keys = key.split(".");
		var subinput = input[keys[0]];
		var subkey = "";
		for (var i = 1; i < keys.length; i++) {

			if(i != keys.length - 1) {
				subkey += keys[i] + ".";
			}
			else {
				subkey += keys[i];
			}

		}
		// //console.log("subkey:" + subkey + "end");
		if(isArray(subinput)) {
			// //console.log("Array:" + key);
			for (var i = 0; i < subinput.length; i++) {

				values = values.concat(getKeyValue(subinput[i], subkey));
			}

		}
		else {
			values = values.concat(getKeyValue(subinput, subkey));
		}

	}
	else if(key.toLowerCase().indexOf(" ") > -1) {

		var keys = key.split(" ");

		for (var i = 0; i < keys.length; i++) {
			// console.log(keys[i]);
			// console.log(JSON.stringify(input));
			values = values.concat(getKeyValue(input, keys[i]));
		}

	}
	else {

		// //console.log("Final value:" + key + "-" + input[key]);
		if(input !== null) {
			values.push(input[key]);
		}

	}

	return values;
}

// check if input json object is an array
function isArray(what) {
	return Object.prototype.toString.call(what) === '[object Array]';
}