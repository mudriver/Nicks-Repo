$(document).ready(function() {

	

	
	
	
	console.log("GEt Table");
	loadTable("","", "", $("#type").val());
	

	$('#trainerSearch').on('keyup', function() {
		if($('#trainerSearch').val().length > 2){
			loadTable();
		}
		
	});

	$("#catSearch").change(function() {
		  //alert( "Handler for .change() called." );
		loadTable();
	});
	
	$("#subCatSearch").change(function() {
		  //alert( "Handler for .change() called." );
		loadTable();
	});
	
	setupSortByButtons();
	setupDocument();
	
});// END DOCUMENT READY

function setupDocument(){
	//initialize the date picker
	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy'
	});
	//select 2 on the official search

	$("#catSearch").select2({
		placeholder: "Select Category"
	});
	
	$("#subCatSearch").select2({
		placeholder: "Select Subcategory"
	});
	$("#statusSearch").select2({
		placeholder: "Select Status"
	});
	$("#officialSearch").select2({
		placeholder: "Select Official"
	});
	$("#sortBySearch").select2({
		placeholder: "Select Sort"
	});
	$('#maxResults option[value=30]').attr('selected','selected');
	
}

function selectSortBy(num){
	var sort = $("#sortBySearch").val();
	if(sort == null){
		sort = [num];
	}
	else{
	//if odd number
	if(num % 2){

			var numb = num-1;
			var i = sort.indexOf(numb.toString());
			if(i != -1) {
				sort.splice(i, 1);
			}
		
	}
	else{
		var numb = num+1;
		var i = sort.indexOf(numb.toString());
		if(i != -1) {
			sort.splice(i, 1);
		}
	}
	sort.push(num);
	}

	console.log("Inc" + num);
	
	
		;

	
	$("#sortBySearch").select2("val", sort); 

}

function setupSortByButtons(){
	$("#trainerLabel").click(function() {
		
		if($("#sortBySearch").val() != null){
			var sort = $("#sortBySearch").val();
			var i = sort.indexOf("2");
			console.log(i);
			if(i != -1) {
				selectSortBy(3);
			}
			else{
				selectSortBy(2);
			}
		}
		else{
			selectSortBy(2);
		}
		console.log($("#sortBySearch").val());
		loadTable();
	});
	
	$("#dateLabel").click(function() {
		if($("#sortBySearch").val() != null){
			var sort = $("#sortBySearch").val();
			var i = sort.indexOf("4");
			//console.log(i);
			if(i != -1) {
				selectSortBy(5);
			}
			else{
				selectSortBy(4);
			}
		}
		else{
			selectSortBy(4);
		}
		loadTable();
	});
	$("#catLabel").click(function() {
		if($("#sortBySearch").val() != null){
			var sort = $("#sortBySearch").val();
			var i = sort.indexOf("6");
			//console.log(i);
			if(i != -1) {
				selectSortBy(7);
			}
			else{
				selectSortBy(6);
			}
		}
		else{
			selectSortBy(6);
		}
		loadTable();
	});
	$("#subCatLabel").click(function() {
		loadTable();
	});
}

function loadTable(){
	//console.log( $("#maxResults").val() );
	//console.log(JSON.stringify($("#searchForm").serialize()) );
	//console.log($("#searchForm").serialize() );
	$.post(
			  "/turfclubPrograms/inspections/service/inspections",
			  $("#searchForm").serialize() ,
			  function(data){ 
				  console.log(JSON.stringify(data));
				  drawTable(data);
				  }
			);
	
}



function drawTable(data) {

	$("#reportTable tbody").empty();
	//console.log(data.length);
	$.each(data, function(key,row){
	    //console.log(key + row);
	    //console.log(JSON.stringify(row));
	    var rowString = "";
	    rowString += '<td>';
	    //console.log(row.inspectionTrainerId);
	    //console.log("TRAINER" +  (row.inspectionTrainerId != null));
	    if(row.inspectionTrainerId != null){
	    	//console.log("ADD TRAINER");
	    	rowString += (row.inspectionTrainerId.trainerSurname + " " + row.inspectionTrainerId.trainerFirstName + " (Trainer)");
	    	rowString +='</td><td>';
	    	rowString += (row.inspectionTrainerId.trainerAddress1 + " " + row.inspectionTrainerId.trainerAddress2 + " " + row.inspectionTrainerId.trainerAddress3);
	    }
	    else if(row.inspectionHandlerId != null){
	    	rowString += (row.inspectionHandlerId.ownerSurname + " " + row.inspectionHandlerId.ownerFirstName + " (Handler)");
	    	rowString +='</td><td>';
	    	rowString += (row.inspectionHandlerId.ownerAddress1 + " " + row.inspectionHandlerId.ownerAddress2 + " " + row.inspectionHandlerId.ownerAddress3+ " " + row.inspectionHandlerId.ownerAddress4);
	    }
	    rowString +='</td>';
	    var subCat = "";
	    if(row.inspectionSubCategoriesId != null){
	    	subCat = row.inspectionSubCategoriesId.subCatName;
	    }
	    else{
	    	subCat = "N/A";
	    }
	   
	    rowString += '<td>' + row.htmlDate + '</td><td class="wrap">' + row.inspectionCategoriesId.catName + '</td><td>' + subCat + '</td>';
	    //add all vets
	    rowString += '<td>';
	    	 if(row.inspectionsOfficials == null){
	 	    	rowString += "";
	 	    }
	    	 else{
	    		 $.each(row.inspectionsOfficials, function(keyOff,rowOff){
	    		    	//console.log("OfficialK" + keyOff + " L" + row.inspectionsOfficials.length);
	    		    	//console.log("OfficialR" + rowOff);
	    		    	//console.log(JSON.stringify(rowOff));
	    		    	if(keyOff == 0){
	    		    		rowString += rowOff.officialsSurname + ' ' + rowOff.officialsFirstName;
	    		    	}
	    		    	else{
	    		    		rowString += '<br>' + rowOff.officialsSurname + ' ' + rowOff.officialsFirstName;
	    		    	}
	    		    	if(keyOff != row.inspectionsOfficials.length-1){
	    		    		rowString += ',';
	    		    	}
	    	
	    	 
	    	
	    	
	    	
	    	
	    });
	}
	    rowString +='</td>';
	   
	    var deptOfficial = "";
	    if(row.inspectionDeptOfficials != null){
	    	deptOfficial = row.inspectionDeptOfficials;
	    }
	    var edit = '<td></td>';
	    if(row.canEdit == true){
	    	edit = '<td><a href="/turfclubPrograms/inspections/edit?id=' 
      		  + row.inspectionId + '"> <button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;Edit</button></a></td>';
	    }
	    
	    $("#reportTable").find('tbody')
	    .append($( '<tr>' )
	          .append($( rowString + '<td>' + deptOfficial + '</td><td>' + row.inspectionStatusId.statusType + '</td>' + edit))
	        		  
	        		  //USE THIS WHEN DELETE BUTTON REQUIRED
	        		  //.append($('<td>' + row.reportRacecardId.horseName + '</td><td>'
	    	        		//  + row.reportRacecardId.trainerName + '</td><td>' + row.htmlDate + '</td><td>' + row.reportContent + '</td><td>' 
	    	        		//  + row.reportType + '</td><td><a href="/turfclubPrograms/vetReports/edit?id=' 
	    	        		//  + row.reportId + '"> <button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-pencil"></span>&nbsp;&nbsp;Edit</button></a> &nbsp;<a href="/vetReports/delete(id='
	    	        		//  + row.reportId + ',phase=stage)"><button type="button" class="btn btn-default btn-xs"><span class="glyphicon glyphicon-trash"></span>&nbsp;&nbsp;Delete</button></a></td>'))
	    	        			  
	                                             
	    );

	 });

	}


