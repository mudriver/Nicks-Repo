$(document).ready(function() {
$(".datepicker").datepicker({
		maxDate : '+0m +0w +0d',
		dateFormat : 'dd/mm/yy'
	});
$(".onoff").attr("disabled", true);
$("#meetingName").get(0).options.length = 0;
$("#meetingName").get(0).options[0] = new Option(
		"Select Date First", "-1");
$("#meetingName").attr("disabled", true);




function downloadXls() {download('xls');}

function downloadPdf() {download('pdf');}

function download(type) {
	// Retrieve download token
	// When token is received, proceed with download
	$.get('/turfclubPrograms/vetReports/download/token', function(response) {
		// Store token
		var token = response.message[0];
		
		// Show progress dialog
		$('#msgbox').text('Processing download...');
		$('#msgbox').dialog( 
				{	title: 'Download',
					modal: true,
					buttons: {"Close": function()  {
						$(this).dialog("close");} 
					}
				});
		
		// Start download
		$('#pdfPreview').html('<object id="pdf" standby="please wait for pdf to load" type="application/pdf" data="/turfclubPrograms/vetReports/svlDownload?'+'token='+token+'&type='+ type + '&date=' + encodeURIComponent($('#date').val()) + '&meeting=' + $('#meetingName').val()  +'" width="800" height="1000"></object>');


		// Check periodically if download has started
		var frequency = 1000;
		var timer = setInterval(function() {
			$.get('/turfclubPrograms/vetReports/download/progress', {token: token}, 
					function(response) {
						// If token is not returned, download has started
						// Close progress dialog if started
						if (response.message[0] != token) {
							$('#msgbox').dialog('close');
							clearInterval(timer);
						}
				});
		}, frequency);
		
	});
}

$("#svlPreview").click(function( event ) {
	  //event.preventDefault();
	
	download("pdf");
	//$('#loader_img').show();

	
	//var obj = $("#pdf");
	//obj.attr("data", "/turfclubPrograms/vetReports/svlPdf?date=17%2F04%2F2015&meeting=Dundalk");
	//var orig = obj.html();


	    
	//obj.html(orig);
	    
	  

	
	//$('#pdfPreview').html('<object id="pdf" standby="please wait for pdf to load" type="application/pdf" data="/turfclubPrograms/vetReports/svlPdf?date=17%2F04%2F2015&meeting=Dundalk" width="800" height="1000"></object>');
	

	// main image loaded ?
	
	
	
	//$("#svlPreview").attr("href", "/turfclubPrograms/vetReports/svlPdf?date=17%2F04%2F2015&meeting=Dundalk");
	//$('#svlPreview').mousedown();
	//checkErrors();
	//execute();
});

$("#svlExcel").click(function() {
	checkErrors();
});

$("#svlPdf").click(function() {
	checkErrors();
});

function loaded(){
	  console.log("DONE!");
	}
function execute()
{
    $.ajax({
        type:"GET",
        url: "/turfclubPrograms/vetReports/svlPdf",
        data: "date=17%2F04%2F2015&meeting=Dundalk",
        dataType: "html",
        beforeSend : function() {
            //It works
        	$("#svlPreview").hide();
            $('#loadingImage').show();
        },
        complete : function() {
        	$("#svlPreview").show();
            $('#loadingImage').hide();
        },
        success : function(data) {
               
        }
    });    
}




function checkErrors() {
	console.log("Check called.");
	var error = false;
	// check report type
	if($("#meetingName").val() == "-1") {
		$("#meetingName").css({'background-color' : '#993333'});
		error = true;
	}
	else{
		$("#meetingName").css({'background-color' : '#FFFFFF'});
	}
	if(validateDate($("#date").val())){
		$("#date").css({'background-color' : '#FFFFFF'});
	}
	else{
		$("#date").css({'background-color' : '#993333'});
	}

	if(!error) {
		console.log("submitting");
		console.log($("#meetingName").val());
		$("#svlForm").submit();
	}
}

//Date Validator Function
function validateDate(dtValue) {
	var dtRegex = new RegExp(/\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/);
	return dtRegex.test(dtValue);
}

$("#date")
.change(
		function() {
			
			if(validateDate($("#date").val())){
		    $("#date").css({'background-color' : '#FFFFFF'});
			
			
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
									$("#meetingName").attr("disabled", true);
									$(".onoff").attr("disabled", true);

								}
								else {
									$("#meetingName").attr("disabled", false);
									$(".onoff").attr("disabled", false);
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
															
														

													});
								
									
									if($("#meetingName").val() != "-1") {
										$("#meetingName").trigger(
												"change");
										
									}
									

								}

							});
			}
			else{
				$("#date").css({'background-color' : '#993333'});
			}
		}

		);


});

