$(document).ready(function() {

	setupDocument();

});

// hides or shows relevant sections. Sets values
function setupDocument() {

	// show the sections of the form suited to the current selected type.
	checkSectionsToShow();
	// when the report type changes show the sections of the form applicable to
	// the type
	$("#reportTypeSel").change(function() {
		checkSectionsToShow();
	});
	// setup date picker
	$(".datepicker").datepicker({
		maxDate : '+0m +0w +0d',
		dateFormat : 'dd/mm/yy'
	});
	// setup dynamic loading on meeting name, horse name and link to change of
	// items
	setupDynamicLoading();
	//console.log("Trigger change" + $("#racecardId").val());
	triggerChanges();

	// set the save button to check for errors and if none submit the form else
	// show errors
	$("#saveButton").click(function() {
		checkErrors();
	});

	// set select two on the vet selector
	$("#selectvet").select2();

	hideErrors();
}

function checkSectionsToShow() {
	// hide the vet section and show the non run section if the type is non run
	if($("#reportTypeSel").val() == "NonRunner") {
		$("#nonRunnerInfo").show();
		$("#vet").hide();
	}
	else {
		$("#nonRunnerInfo").hide();
		$("#vet").show();
	}
	// hide the race card section if random stable inspection
	if($("#reportTypeSel").val() == "0") {

		$("#dateSection").hide();
		$("#raceCardOnly").hide();
		$("#randomOnly").hide();

	}
	else if($("#reportTypeSel").val() == "RandomStableInspection") {
		$("#raceCardOnly").hide();
		$("#dateSection").show();
		$("#randomOnly").show();
	}
	else {
		$("#raceCardOnly").show();
		$("#dateSection").show();
		$("#randomOnly").hide();
	}
	// hide the racecard
	if($("#horseName").val() != "-1" && $("#horseName").val() != "") {
		$("#loadRaceCardDetail").hide();
	}
	else {
		$("#loadRaceCardDetail").show();
	}

}

function checkErrors() {
	console.log("Check called.");
	var error = false;
	// check report type
	if($("#reportTypeSel").val() == "0") {
		$("#reportTypeError").show();
		error = true;
	}
	else {
		$("#reportTypeError").hide();
	}
	// check meeting date
	if(validateDate($("#date").val())) {
		$("#dateerror").hide();
	}
	else {
		error = true;
		$("#dateerror").show();
	}
	//check racecard info if not random type and not type has not been selected
	if($("#reportTypeSel").val() != -1 && $("#reportTypeSel").val() != "RandomStableInspection"){
		
		
		// check meeting
		if($("#meetingName").val() != "-1") {
			$("#meetingerror").hide();
		}
		else {
			error = true;
			$("#meetingerror").show();
		}
		// check horse - racecard id
		if($("#horseName").val() != "-1") {
			$("#horseerror").hide();
		}
		else {
			error = true;
			$("#horseerror").show();
		}
	}
	
	//check random horse selection field if random report
	if($("#reportTypeSel").val() == "RandomStableInspection"){
		// check horse - random id
		if($("#randomHorseName").val() != "") {
			$("#randomHorseerror").hide();
		}
		else {
			error = true;
			$("#randomHorseerror").show();
		}
		// check trainer - random id
		if($("#randomTrainerName").val() != "") {
			$("#randomTrainererror").hide();
		}
		else {
			error = true;
			$("#randomTrainererror").show();
		}
	
		
	}
	
	//check that the report text is not blank
	if($("#reportContent").val() == ""){
		error = true;
		$("#reportContenterror").show();
	}
	else{
		$("#reportContenterror").hide();
	}
	
	// check at least one vet selected if report type not non run
	if($("#reportTypeSel").val() != "0"
			&& $("#reportTypeSel").val() != "NonRunner"
			&& $("#selectvet").val() == null) {
		console.log("VETS:" + $("#selectvet").val());
		$("#veterror").show();
		error = true;
	}
	else {
		$("#veterror").hide();
	}

	console.log(error + $("#randomHorseName").val());
	if(!error) {
		console.log("submitting");
		$("#vetReportForm").submit();
	}

}

function hideErrors() {
	$("#reportTypeError").hide();
	$("#dateerror").hide();
	$("#meetingerror").hide();
	$("#horseerror").hide();
	$("#randomTrainererror").hide();
	$("#randomHorseerror").hide();
	$("#reportContenterror").hide();
	$("#veterror").hide();
}

// Date Validator Function
function validateDate(dtValue) {
	var dtRegex = new RegExp(/\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/);
	return dtRegex.test(dtValue);
}

function triggerChanges() {

	console.log($("#date").val());
	if($("#date").val() != "" && $("#date").val() != "") {
		$("#date").trigger("change");
	}

}

function setupDynamicLoading() {
	// setup connection between meetingdate select and meeting

	console.log($("#date").val());
	console.log($("#meetingName").val());

	$("#date")
			.change(
					function() {
						if($("#reportTypeSel").val() != "RandomStableInspection"){
						console.log("Date change: " + $("#date").val());
						var jsonParameters = {
							date : $("#date").val()

						};
						$("#meetingName").get(0).options.length = 0;
						$("#meetingName").get(0).options[0] = new Option(
								"Loading", "-1");

						$
								.getJSON(
										"/turfclubPrograms/vetReports/service/meetings",
										jsonParameters)
								.done(

										function(data) {

											if(data.length == 0) {

												$("#meetingName").empty();
												$("#meetingName").get(0).options[$(
														"#meetingName").get(0).options.length] = new Option(
														"No Meetings For Date",
														-1);

											}
											else {
												$("#meetingName").empty();

												$("#meetingName").get(0).options[$(
														"#meetingName").get(0).options.length] = new Option(
														"Please Select A Meeting",
														-1);
												$
														.each(
																data,

																function(key,
																		value) {

																	if($(
																			"#meeting")
																			.val() != ""
																			&& $(
																					"#meeting")
																					.val() == data[key]) {
																		console
																				.log("setting value"
																						+ data[key]);
																		$(
																				"#meetingName")
																				.get(
																						0).options[$(
																				"#meetingName")
																				.get(
																						0).options.length] = new Option(
																				data[key],
																				data[key]);
																		$(
																				"#meetingName")
																				.val(
																						$(
																								"#meeting")
																								.val());
																	}
																	else {
																		$(
																				"#meetingName")
																				.get(
																						0).options[$(
																				"#meetingName")
																				.get(
																						0).options.length] = new Option(
																				data[key],
																				data[key]);
																		console.log(data.length);
																		if(data.length == 1 && key == 0){
																			$("#meetingName").val(data[key]);
																		}
																	}

																});
											
												
												if($("#meetingName").val() != "-1") {
													$("#meetingName").trigger(
															"change");
												}
												

											}

										});
					}

					});

	
	$("#horseName").select2({
		  placeholder: "Select a horse",
		  allowClear: true
		});
	// setup connection between meetingdate and name select and horse name
	$("#meetingName")
			.change(
					function() {

						var jsonParameters = {
							date : $("#date").val(),
							meeting : $("#meetingName").val()
						};
						console.log($("#meetingDate").val()
								+ $("#meetingName").val())
						$("#horseName").get(0).options.length = 0;
						$("#horseName").get(0).options[0] = new Option(
								"Loading", "-1");

						$
								.getJSON(
										"/turfclubPrograms/vetReports/service/horses",
										jsonParameters)
								.done(

										function(data) {

											if(data.length == 0) {

												$("#horseName").empty();
												$("#horseName").get(0).options[$(
														"#horseName").get(0).options.length] = new Option(
														"No Horses available",
														-1);

											}
											else {
												$("#horseName").empty();

												$("#horseName").get(0).options[$(
														"#horseName").get(0).options.length] = new Option();
												$
														.each(
																data,

																function(key,
																		value) {
																	if($(
																			"#horse")
																			.val() != ""
																			&& $(
																					"#horse")
																					.val() == data[key][0]) {
																		console
																				.log("setting value"
																						+ data[key][0]);
																		$(
																				"#horseName")
																				.get(
																						0).options[$(
																				"#horseName")
																				.get(
																						0).options.length] = new Option(
																				data[key][1],
																				data[key][0]);
																		$(
																				"#horseName")
																				.val(
																						$(
																								"#horse")
																								.val());
																	}
																	else {
																		$(
																				"#horseName")
																				.get(
																						0).options[$(
																				"#horseName")
																				.get(
																						0).options.length] = new Option(
																				data[key][1],
																				data[key][0]);
																	}

																});
												if($("#horseName").val() != "-1") {
													$("#horseName").trigger(
															"change");
												}

											}

											
										});

					});

	// setup connection between meetingdate and name select and horse name
	$("#horseName").change(
			function() {

				console.log($("#horseName").val());

				$.getJSON(
						"/turfclubPrograms/vetReports/service/racecardDetail/"
								+ $("#horseName").val()).done(

				function(data) {

					console.log(data.length);
					if(data.length == 1) {

						console.log(data[0]);
						$("#trainerName").empty();
						$("#raceNo").empty();
						$("#horseNum").empty();
						$("#trainerName").val(data[0].trainerName);
						$("#raceNo").val(data[0].raceNo);
						$("#horseNum").val(data[0].horseNum);
						$("#loadRaceCardDetail").show();
					}
					else {
						$("#loadRaceCardDetail").hide();

					}

				});

			});
	
	

	$('#randomHorseName').select2({
		minimumInputLength : 3,

		placeholder : 'Enter Horse Name',

		ajax : {
			url : "/turfclubPrograms/vetReports/service/horsesAll",
			dataType : 'json',
			type : "GET",

			quietMillis : 100,
			data : function(term, page) {

				// alert('h' + term);
				return {
					chars : term
				};
			},
			results : function(data, page) {
				// alert("Selected data is: "+JSON.stringify(data));
				var dataReturn = [];
				$.each(data, function(key, value) {
					dataReturn.push({
						id : data[key][1],
						text : data[key][0]
					});
				});

				return {
					results : dataReturn
				};
			}
		},

	});

	$('#randomTrainerName').select2({
		minimumInputLength : 3,

		placeholder : 'Enter Trainer Name',

		ajax : {
			url : "/turfclubPrograms/vetReports/service/trainersAll",
			dataType : 'json',
			type : "GET",

			quietMillis : 100,
			data : function(term, page) {

				// alert('h' + term);
				return {
					chars : term
				};
			},
			results : function(data, page) {
				// alert("Selected data is: "+JSON.stringify(data));
				var dataReturn = [];
				$.each(data, function(key, value) {
					dataReturn.push({
						id : data[key][1],
						text : data[key][0]
					});
				});

				return {
					results : dataReturn
				};
			}
		},

	});

}
