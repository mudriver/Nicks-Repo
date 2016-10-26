$(document)
		.ready(
				function() {

					// add the datepicker selector to the date input
					$(".datepicker").datepicker({
						maxDate : '+0m +0w +0d',
						dateFormat : 'dd/mm/yy'
					});
					// hide the error messages
					console.log("hide errors");
					$('#dateerror').hide();
					$('#meetingerror').hide();
					$('#horseerror').hide();
					$('#reporterror').hide();
					$('#veterror').hide();

					initializeVetReports ();
					

					

					

					

				});//END DOCUMENT READY

/**
 * Initializes the edit Vet report form
  */
function initializeVetReports (){

	//parameters for meeting name selector to load
	var jsonParameters = {
			date : $("#meetingDate").val()
	};
	var dynamicMeetingSelect = new initializeDynamicLoading($("#meetingName"), "/vetReports/service/meetings", jsonParameters, "Loading Meetings", "No Meetings Available For Date", "Please Select", 0, 0, true, 2);
	//load the values into the meeting name selector
	dynamicMeetingSelect.setLinkItemChange($("#meetingDate"));
	jsonParameters = {
			date : $("#meetingDate").val(),
			meeting : $('#meetingName').val()
	};
	//load the values into horse name selector
	var dynamicHorseSelect = new initializeDynamicLoading($("#horseName"), "/vetReports/service/horses", jsonParameters, "Loading Horses", "No Horses Available For Meeting", "Please Select", 0, 1, true, 3);
	dynamicHorseSelect.setLinkItemChange($('#meetingName'));
	
	// apply select2 to the vet selector
	$("#selectvet").select2();

	$('#formsubmit').click(function(event) {

		event.preventDefault();
		if($('.dateerror').is(":visible")) {

		}
		else {
			$('#vetReportForm').submit();
		}

	});
	
};



/**
 * Initializes a dynamic load on a select element which can be linked to the change of another object
 * @param inputSelector The DOM Element (Select Only) To be initialized for dynamic loading
 * @param inputErrorCheckingType 0:none 1:date 2:date and empty 3:empty
 */
function initializeDynamicLoading(inputSelector, inputJsonUrl, inputJsonParameters, inputLoadingText,
		inputEmptyDataText, inputPleaseSelectText, inputIntValuePosition, inputIntTextPosition,
		inputSetSelect2Boolean, inputErrorCheckingType) {

	var selector = inputSelector;
	var jsonUrl = inputJsonUrl;
    var jsonParameters = inputJsonParameters;
	var loadingText = inputLoadingText;
	var emptyDataText = inputEmptyDataText;
	var pleaseSelectText = inputPleaseSelectText;
	var intValuePosition = inputIntValuePosition;
	var intTextPosition = inputIntTextPosition;
	var setSelect2Boolean = inputSetSelect2Boolean;
	var errorCheckingType = inputErrorCheckingType?inputErrorCheckingType:0;

	/**
	 * apply external library select2 functionality to the select element
	 */
	this.setSelect2 = function() {
		selector.select2();
	};

	

	
	
	
	/**
	 * apply external library select2 functionality to the select element
	 * @param fireEventWhenThisItemChanges a dom element that fires a reload of the items in the
	 * select element
	 */
	this.setLinkItemChange = function(fireEventWhenThisItemChanges){
		fireEventWhenThisItemChanges.change(
				function() {
					if(fireEventWhenThisItemChanges.hasErrors){
						console.log("Error checking Some");
					}
					else{
						console.log("Error checking None");
						
					}
					
				});
	};
	

	// loads the values from json url into select options html.
	this.loadValues = function() {

		
		
		selector.get(0).options.length = 0;
		selector.get(0).options[0] = new Option(loadingText, "-1");

		$
				.getJSON(jsonUrl, jsonParameters)
				.done(

						function(data) {
							// alert("Selected data is:
							// "+JSON.stringify(data));
							if(data.length == 0) {

								selector.empty();
								selector.get(0).options[selector.get(0).options.length] = new Option(
										emptyDataText, -1);
								if(setSelect2Boolean){
									selector.select2().select2("disable", true);
								}
								
							}
							else {
								selector.empty();

								selector.get(0).options[selector.get(0).options.length] = new Option(
										pleaseSelectText, -1);
								$
										.each(
												data,
												function(key, value) {

													selector.get(0).options[selector
															.get(0).options.length] = new Option(
															data[key][intTextPosition],
															data[key][intValuePosition]);

												});
								if(setSelect2Boolean){
								selector.select2().select2("enable", true);
								}

							}

						});

		
	};

	

};

// Date Validator Function
function validateDate(dtValue) {
	var dtRegex = new RegExp(/\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/);
	return dtRegex.test(dtValue);
}

// Angular JS Controller
function vetReportEditController($scope, $http) {

	$scope.rcId;

	// $scope.loadRaceCardInfo = function(){console.log("Selected Item:" +
	// $scope.rcId);};

	$scope.$watch('rcId', function() {
		console.log("rcID Watch function called");
		$scope.loadRaceCardInfo();
	});

	// console.log("race card");
	$scope.loadRaceCardInfo = function() {

		// console.log("/vetReports/service/racecardDetail/" + $scope.rcId);
		console.log("Load Race Card Info Called:" + $scope.rcId + "!");
		
		if($scope.rcId && $scope.rcId != "" && $scope.rcId != "-1") {
			$http.get("/vetReports/service/racecardDetail/" + $scope.rcId)

			.success(function(response) {
				// console.log(JSON.stringify(response));
				// console.log("Hello");
				$scope.info = response[0];
				// console.log($scope.info.trainerName);
			}

			);
		}
		else {
			console.log("empty info");
			$scope.info = null;
		}

	};

};

/*
//if the racecard only section exists initialize the
// selectors
if($("#raceCardOnly").length) {
	// if the meeting does not already have a value
	// initialize the meeting name and horse name selector
	if($("#meetingName").val() == "") {
		console
				.log("initialize the meeting & horse select");
		$("#meetingName").empty();
		$("#meetingName").select2();
		$("#meetingName").get(0).options[$("#meetingName")
				.get(0).options.length] = new Option(
				"Select A Date First", -1);
		$("#meetingName").select2()
				.select2("disable", true);
		// initialize the horse name selector
		$("#horseName").empty();
		$("#horseName").select2();
		$("#horseName").get(0).options[$("#horseName").get(
				0).options.length] = new Option(
				"Select A Meeting First", -1);
		$("#horseName").select2().select2("disable", true);

	}
	else {
		console.log("meeting has value not initializing");
	}
	// if the horse name does not have a value initialize
	// it.
	if($("#horseName").val() == "") {
		console.log("initialize the horse select");
	}
	else {
		console.log("horse has value not initializing");
	}

	// create the function to get meetings when a date is
	// selected if the date is correct
	$("#meetingDate")
			.change(
					function() {

						var dtVal = $('#meetingDate').val();
						if(dtVal != "") {
							if(ValidateDate(dtVal)) {
								var dateFormat = $.datepicker
										.formatDate(
												'yy-mm-dd',
												$.datepicker
														.parseDate(
																'dd/mm/yy',
																dtVal));
								getMeetings(dateFormat);
								$('#dateerror').hide();
							}
							else {
								$('#dateerror').show();
							}
						}
						else {
							$('#dateerror').show();
						}

					});

	// trigger the change when the documents loads if there
	// is a value for editing
	if($("#meetingDate").val() != ""
			&& $("#meetingDate").val() != null) {
		console.log("meeting date trigger");
		$("#meetingDate").trigger("change");
	}

	// create the function to get meetings when a date is
	// selected if the date is correct
	$("#meetingName").change(
			function() {
				console.log("meeting name change");
				// if the meeting date is valid
				if(ValidateDate($('#meetingDate').val())) {
					var date = $.datepicker.formatDate(
							'yy-mm-dd',
							$.datepicker.parseDate(
									'dd/mm/yy', $(
											'#meetingDate')
											.val()));
					var meeting = $('#meetingName').val();
					// alert(meeting + " " + date);
					// check the meeting is not empty
					if(meeting != "") {
						// check the meeting is not a null
						// value
						if(meeting != -1) {
							getHorses(date, meeting);
							$('#meetingerror').hide();
						}
						else {
							$('#meetingerror').show();
						}
					}
					else {
						$('#meetingerror').show();
					}
				}

			});

	$("#horseName").change(
			function() {
				console.log("horse name change"
						+ $("#horseName").val());

			});

	// create the function to retrieve meetings when a date
	// is selected
	function getMeetings(dateString) {

		$("#meetingName").get(0).options.length = 0;
		$("#meetingName").get(0).options[0] = new Option(
				"Loading Meetings", "-1");

		var parameters = {
			date : dateString
		};
		$
				.getJSON("/vetReports/service/meetings",
						parameters)
				.done(
						function(data) {
							if(data.length == 0) {

								$("#meetingName").empty();
								$("#meetingName").get(0).options[$(
										"#meetingName")
										.get(0).options.length] = new Option(
										"No Meetings On Date",
										-1);
								$("#meetingName").select2()
										.select2("disable",
												true);
								$("#meetingName").val(
										data[0]).trigger(
										"change");
							}
							else {
								$("#meetingName").empty();
								if(data.length == 1) {

									$("#meetingName")
											.get(0).options[$(
											"#meetingName")
											.get(0).options.length] = new Option(
											data[0],
											data[0]);
									$("#meetingName")
											.select2()
											.select2(
													"disable",
													true);
									$("#meetingName")
											.val(data[0])
											.trigger(
													"change");

								}
								else {

									$("#meetingName")
											.get(0).options[$(
											"#meetingName")
											.get(0).options.length] = new Option(
											"Please Select",
											-1);
									$
											.each(
													data,
													function(
															key,
															value) {

														$(
																"#meetingName")
																.get(
																		0).options[$(
																"#meetingName")
																.get(
																		0).options.length] = new Option(
																data[key],
																data[key]);
													});
									$("#meetingName")
											.select2()
											.select2(
													"enable",
													true);

								}

							}

						});

	}

	// create the function to retrieve horses when a meeting
	// is selected
	function getHorses(date, meeting) {

		$("#horseName").get(0).options.length = 0;
		$("#horseName").get(0).options[0] = new Option(
				"Loading Horses", "-1");

		// alert(date + " " + meeting);
		if(meeting == null) {
			// alert("Null Meeting");
		}
		else {
			var parameters = {
				date : date,
				meeting : meeting
			};
			$
					.getJSON("/vetReports/service/horses",
							parameters)
					.done(

							function(data) {
								// alert("Selected data is:
								// "+JSON.stringify(data));
								if(data.length == 0) {

									$("#horseName").empty();
									$("#horseName").get(0).options[$(
											"#horseName")
											.get(0).options.length] = new Option(
											"No Horses Available",
											-1);
									$("#horseName")
											.select2()
											.select2(
													"disable",
													true);
								}
								else {
									$("#horseName").empty();

									$("#horseName").get(0).options[$(
											"#horseName")
											.get(0).options.length] = new Option(
											"Please Select",
											-1);
									$
											.each(
													data,
													function(
															key,
															value) {

														// $('#horseName').append('<option
														// value="'
														// +
														// data[key][0]
														// + '"
														// data-ng-options="'
														// +
														// data[key][0]
														// +
														// '">'
														// +
														// data[key][1]
														// +
														// '</option>');

														$(
																"#horseName")
																.get(
																		0).options[$(
																"#horseName")
																.get(
																		0).options.length] = new Option(
																data[key][1],
																data[key][0]);
														// console.log($("#horseName").get(0).options[$("#horseName").get(0).options.length
														// -
														// 1]);

													});
									$("#horseName")
											.select2()
											.select2(
													"enable",
													true);

								}

							});
		}

	}

	$('#horseName')
			.change(
					function() {
						console.log("horsename change"
								+ $('#horseName').val());
						$('#racecardId').val(
								$('#horseName').val());
						console.log("rid change"
								+ $('#racecardId').val());
						// angular.element($('#racecardId')).triggerHandler('input');
						var scope = angular.element(
								$("#racecardId")).scope();
						scope.$apply(function() {
							// console.log(scope.horsey);
							scope.rcId = $('#horseName')
									.val();
							console.log(scope.rcId);
						});
					});

}*/ 
// END RACECARD ONLY SECTION