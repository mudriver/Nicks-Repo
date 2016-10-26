$(document).ready(function() {

	
	console.log("GEt Table");
	loadTable("","", "", $("#type").val());
	

	$('#horseSearch').on('keyup', function() {
		if($('#horseSearch').val().length > 2){
			loadTable("", $('#horseSearch').val(), "horse", "");
		}
		
	});

	
	$("#horseLabel").click(function() {
		loadTable("horse", "", "",$("#type").val());
	});
	
	$("#dateLabel").click(function() {
		loadTable("date", "", "", $("#type").val());
	});
	
	
});// END DOCUMENT READY

function loadTable(sort, search, searchType, type){
	console.log(sort);
	$.getJSON(
			  "/turfclubPrograms/vetReports/service/reports",
			  {sortBy:sort, search:search, searchType:searchType , type: type},
			  function(data){ 
				  console.log(JSON.stringify(data));
				  drawTable(data);
				  }
			);
	
}


function drawTable(data) {

	$("#reportTable tbody").empty();
	console.log(data.length);
	$.each(data, function(key,row){
	    console.log(key + row);
	    $("#reportTable").find('tbody')
	    .append($( '<tr>' )
	          .append($('<td>' + row.reportRacecardId.horseName + '</td><td>'
	        		  + row.reportRacecardId.trainerName + '</td><td>' + row.htmlDate + '</td><td>' + row.reportContent + '</td><td>' 
	        		  + row.reportType + '</td><td><a href="/turfclubPrograms/vetReports/edit?id=' 
	        		  + row.reportId + '"> <button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;Edit</button></a></td>'))
	        		  
	        		  //USE THIS WHEN DELETE BUTTON REQUIRED
	        		  //.append($('<td>' + row.reportRacecardId.horseName + '</td><td>'
	    	        		//  + row.reportRacecardId.trainerName + '</td><td>' + row.htmlDate + '</td><td>' + row.reportContent + '</td><td>' 
	    	        		//  + row.reportType + '</td><td><a href="/turfclubPrograms/vetReports/edit?id=' 
	    	        		//  + row.reportId + '"> <button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;Edit</button></a> &nbsp;<a href="/vetReports/delete(id='
	    	        		//  + row.reportId + ',phase=stage)"><button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;Delete</button></a></td>'))
	    	        			  
	                                             
	    );

	 });

	}