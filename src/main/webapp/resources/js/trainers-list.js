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

$(document).ready(
		function() {
			console.log($("#type").val());
			if($("#type").val() == "All"){
				$("#header").text($("#header").text() +  " All");
			}
			else if($("#type").val() == "Returned"){
				$("#header").text($("#header").text() +  " Return Submitted");
			}
			else if($("#type").val() == "NoP35"){
				$("#header").text($("#header").text() +  " P35 Pending");
			}
			else if($("#type").val() == "Verify"){
				$("#header").text($("#header").text() +  " Pending Verification");
			}
			else if($("#type").val() == "Complete"){
				$("#header").text($("#header").text() +  " Fully Complete");
			}
			
			
			$(window).on("beforeunload", function() { 
			    if($(".save-notes-button").length > 0){
			    	return  "You have not saved all your notes are you sure you want to close?"; 
			    }
			   
				
			});
			
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
				
			});
			
			
			$('#example').dataTable(
					{
						"bProcessing" : true,
						"bServerSide" : true,
						"sAjaxSource" : "trainers-pages",

						"fnServerData" : function(sSource, aoData, fnCallback) {
							aoData.push({
								"name" : "type",
								"value" : $("#type").val()
							});
							$.ajax({
								"dataType" : 'json',
								"type" : "GET",
								"url" : sSource,
								"data" : aoData,
								"success" : fnCallback
							});
						},
						"aoColumnDefs" : [
								{
									"aTargets" : [ 0 ],
									"bSortable" : true,
									"mRender" : function(data, type, full) {

										return "<p>" +  full.trainerFirstName + " "
												+ full.trainerSurname  + "</p>";
									}

								},
								{
									"aTargets" : [ 1 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

										return   "<p>" + full.trainerAddress1
												+ "<br/>"
												+ full.trainerAddress2
												+ "<br/>"
												+ full.trainerAddress3 + "</p>";
									}

								}, {
									"width": "20%",
									"aTargets" : [ 2 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

										
										
										
										return "<p><b><u>Trainer Contact</u></b><br/><b>Home Phone: </b>" + (full.trainerHomePhone === undefined ? "N/A" : full.trainerHomePhone )+ "<br/><b>Mobile: </b>" +  (full.trainerMobilePhone === undefined ? "N/A" : full.trainerMobilePhone) + "<br/><br/><b><u>Person Who Completed Return:</u></b><br/><b>Name: </b>" + (full.trainerContactName === undefined ? "N/A" : full.trainerContactName)  + "<br/><b>Phone: </b>" + (full.trainerContactPhone === undefined ? "N/A" : full.trainerContactPhone) + "<br/></p>";
									}
								}
								, {
									"aTargets" : [ 3 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {
										console.log(full.trainerNotes);
										if(full.trainerNotes !== undefined){
											return "<textarea data-trainer-id=" + full.trainerId + "  class='notes form-control' rows='4' cols='25' >" + full.trainerNotes + "</textarea>";
										}
										else{
											return "<textarea data-trainer-id=" + full.trainerId + "  class='notes form-control' rows='4' cols='25' ></textarea>";
										}
										
										
									}

								}
								, {
									"aTargets" : [ 4 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {

										console.log(full.trainerReturnComplete);
										if(full.trainerReturnComplete == true){
											return "<div class='text-center'><img class='complete' src='../resources/img/greentick.png' alt='Complete' height='65' width='70'></img></div>";
										}
										else{
											return "<div class='text-center'><img class='complete' src='../resources/img/redx.jpg' alt='Complete' height='65' width='70'></img></div>";
										}
										
									}

								},
								{
									"aTargets" : [ 5 ],
									"bSortable" : false,
									"mRender" : function(data, type, full) {
										if(full.trainerP35Attached == true){
											return "<div class='text-center'><a href='/trainersEmployees/viewP35'><img class='complete' src='../resources/img/greenTickViewP35.png' alt='Complete' height='80' width='90'></img></a></div>";
										}
										else{
											return "<div class='text-center'><a href='/trainersEmployees/uploadP35'><img class='complete' src='../resources/img/redXClickToAttach.png' alt='Complete' height='80' width='90'></img></a></div>";
										}
									}
								},
								{
									"aTargets" : [ 6 ],
									"bSortable" : false
									,
									"mRender" : function(data, type, full) {
										if(full.trainerVerified == true){
											return "<div class='text-center'><a href='/turfclubPrograms/trainersEmployees/listTrainersEmployees?type=edit&trainerId=" + full.trainerId +  "'><img class='complete' src='../resources/img/greenTickClickToEdit.png' alt='Complete' height='80' width='90'></img></a></div>";
										}
										else{
											return "<div class='text-center'><a href='/turfclubPrograms/trainersEmployees/listTrainersEmployees?type=verfiy&trainerId=" + full.trainerId +  "'><img class='complete' src='../resources/img/redXClickToVerfiy.png' alt='Complete' height='80' width='90'></img></a></div>";
										}
									}
								}
								]

					});

		});



