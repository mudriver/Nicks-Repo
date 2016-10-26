//Plug-in to fetch page data 
jQuery.fn.dataTableExt.oApi.fnPagingInfo = function(oSettings) {
	return {
		"iStart" : oSettings._iDisplayStart,
		"iEnd" : oSettings.fnDisplayEnd(),
		"iLength" : oSettings._iDisplayLength,
		"iTotal" : oSettings.fnRecordsTotal(),
		"iFilteredTotal" : oSettings.fnRecordsDisplay(),
		"iPage" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
		"iTotalPages" : oSettings._iDisplayLength === -1 ? 0 : Math
				.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength),

	};
};


function download() {
	// Retrieve download token
	// When token is received, proceed with download
	$.get('/turfclubPrograms/trainersEmployees/download/token', function(response) {
		// Store token
		var token = response.message[0];
		
	
		$("#please-wait").modal('show');
		// Start download
		$("#pdf-section").show();
		
		//$('#pdf-preview').html('<iframe id="pdf" src="/turfclubPrograms/trainersEmployees/employeeReportDownload?'+'token='+token +'"></iframe>');
		$('#pdf-preview').html('<object id="pdf" standby="please wait for pdf to load" type="application/pdf" data="/turfclubPrograms/trainersEmployees/employeeReportDownload?'+'token='+token +'" width="100%" height="50%" ></object>');

		$("#pdf").css("height", ($(window).height()/2));
		console.log($(window).height());
		console.log($(window).height()/2);
		// Check periodically if download has started
		var frequency = 1000;
		var timer = setInterval(function() {
			$.get('/turfclubPrograms/trainersEmployees/download/progress', {token: token}, 
					function(response) {
						// If token is not returned, download has started
						// Close progress dialog if started
						if (response.message[0] != token) {
							$("#please-wait").modal('hide');
							
							/*
							$('html, body').animate({
							    scrollTop: $("#listEmployeeForm").offset().top
							}, 500);*/
							console.log($("#pdf").scrollTop());
							clearInterval(timer);
						}
				});
		}, frequency);
		
	});
}


$(document).ready(
		function() {
			
			
		
			//set the width of the pdf section dynamically
			 $("#pdf").width($("#pdf-section").width());
			//hide the pdf section
			 $("#pdf-section").hide();
			 $("#pdf-section").click(function() {
				 
				 
		    		console.log("TEST");
		    		

	 });
			 $("#pdf-show").click(function() {
				 
				 
				    		download();
				    		

			 });

			 $("#pdf-close").click(function() {
				 console.log($("#pdf").scrollTop());
				 $("#pdf-section").hide();
			 });
			 $("#pdf-print").click(function() {
				 var t=document.querySelector("#pdf");
				 var htmlDocument= t.contentDocument;
				 console.log($(htmlDocument).html());
			 });
			
			 $('body').on('focus', '#page-indicator', function() {
				
				console.log("test pdf on load");
				 
			 });

			 
			/*
			$('body').on('click', '.notes', function() {
				if(!$(this).next().hasClass("save-notes-button")){
				$("<div class='text-center save-notes-button'><button type='button' class='btn btn-danger '><span class='glyphicon glyphicon-share'></span> <span><strong>Save</strong></span></button><div>").insertAfter($(this))
				}
				
			});
			$('body').on('click', '.save-notes-button', function() {
				console.log($(this).prev().data("trainerId"));
				$.ajax({
					  url: "/turfclubPrograms/trainersEmployees/service/notes",
					  type: "get", //send it through get method
					  data:{trainerId:$(this).prev().data("trainerId") , notes: $(this).prev().val()},
					  success: function(response) {
					    //Do Something
					  },
					  error: function(xhr) {
					    //Do Something to handle error
					  }
					});
				
				
				$(this).remove();
				
			});*/
			
			
			$('#example').dataTable(
					{
						
						
				        
						"bProcessing" : true,
						"bServerSide" : true,
						"sAjaxSource" : "employees-pages",

						"fnServerData" : function(sSource, aoData, fnCallback) {
							aoData.push({
								"name" : "trainerId",
								"value" : $("#trainerId").val()
							});
							$.ajax({
								"dataType" : 'json',
								"type" : "GET",
								"url" : sSource,
								"data" : aoData,
								"success" : function (data) {
						           console.log(JSON.stringify(data));
						           fnCallback(data);
						            },
							});
						},
				
						
						"aoColumnDefs" : [
								{
									"aTargets" : [ 0 ],
									"bSortable" : true,
									"mRender" : function(data, type, full) {

										return "<p>" +  full.employeesFirstname + " "
												+ full.employeesSurname  + "</p>";
									}

								},
								{
									"aTargets" : [ 1 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

										
										return   "<p>" + (
												full.employeesAddress1 !== "" ?  ("<br/>"
														+ full.employeesAddress1) : "") + (
										full.employeesAddress2 !== "" ?  ("<br/>"
												+ full.employeesAddress2) : "") + (
														full.employeesAddress3 !== "" ?  ("<br/>"
																+ full.employeesAddress3) : "") 
																+ (
										full.employeesAddress4 !== "" ?  ("<br/>"
												+ full.employeesAddress4) : "") 
												+ (
										full.employeesAddress5 !== "" ?  ("<br/>"
												+ full.employeesAddress5) : "") + "</p>";
												
									}

								}, {
									"width": "20%",
									"aTargets" : [ 2 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

										
										
										console.log(full.teEmployentHistories.ehPpsNumber);
										return (
												full.teEmployentHistories.ehPpsNumber !== undefined && full.teEmployentHistories.ehPpsNumber !== "" ?  ("<br/>"
														+ full.teEmployentHistories.ehPpsNumber) : "Not Available");
									}
								}
								, {
									"aTargets" : [ 3 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {
										console.log(full.employeesHasTaxableEarnings);
										console.log(full.teEmployentHistories[0].ehEarnings);
										return "€" + (
												full.teEmployentHistories[0].ehEarnings !== 0 ?  (full.teEmployentHistories[0].ehEarnings.toString().indexOf(".00", this.length - ".00".length) === -1 ? (full.teEmployentHistories[0].ehEarnings.toString() + ".00") :full.teEmployentHistories[0].ehEarnings  ) : "0.00");
										
										
									}

								}
								, {
									"aTargets" : [ 4 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

								
										
											return "<div class='text-center'><a href='/turfclubPrograms/trainersEmployees/edit?employeeId=" + full.employeesEmployeeId + "'><button type='button' class='btn btn-default'><span class='glyphicon glyphicon-check'></span>  Edit<span><strong></strong></span></button></a></div>";
									
										
									}

								},
								{
									"aTargets" : [ 5 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {
										
											return "<div class='text-center'><a href='/trainersEmployees/uploadP35'><img class='complete' src='../resources/img/redXClickToAttach.png' alt='Complete' height='80' width='90'></img></a></div>";
										
									}
								}
								,
								{
									"aTargets" : [ 6 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {
										
											return "<div class='text-center'><img class='show-more' src='../resources/img/redXClickToAttach.png' alt='Complete' height='80' width='90'></img></a></div>";
										
									}
								}
								
								]

					});

		});



