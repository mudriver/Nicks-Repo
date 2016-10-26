$(document).ready(function() {
	$('#formSubmit').click(function(event) {

	
		if(checkErrors()){
			$('#vetReportAlertForm').submit();
		}
		
		

	});

	$("#alertSheet").stupidtable();
	$("#horseSort").click();
	$('#horseSearch').on('keyup', function() {
      
		filterTable();
		
	});
	
	
	
	$('#trainerSearch').on('keyup', function() {
		filterTable();
	});
	
	$('#reportTypeSel').change(function() {
		console.log("type change");
		if($('#reportTypeSel').val() == "Trainer"){
			console.log("show ");
			$('#trainerSelector').show();
			$('#horseSelector').hide();
		}
		if($('#reportTypeSel').val() == "Horse"){
			console.log("show horse");
			$('#trainerSelector').hide();
			$('#horseSelector').show();
		}
		if($('#reportTypeSel').val() == "-1"){
			$('#trainerSelector').hide();
			$('#horseSelector').hide();
		}
	});


	$('#horseSelError').hide();
	$('#trainerSelError').hide();
	$('#trainerSelector').hide();
	$('#horseSelector').hide();
	$('#reportTypeSelError').hide();
	
	$(function() {
		console.log("select 2 horse selector");
	    $('#horseSel').select2({
	        minimumInputLength: 3,
	 
	        placeholder: 'Enter Horse Name',
	        
	        
	        ajax: {
	            url: "/turfclubPrograms/vetReports/service/horsesAll",
	            dataType: 'json',
	            type: "GET",
	          
	            quietMillis: 100,
	            data: function(term, page) {
	            	 
	            	//alert('h' + term);
	            	return {
	                    chars: term
	                };
	            },
	            results: function(data, page ) {
	            	//alert("Selected data is: "+JSON.stringify(data));
	            	var dataReturn = [];
	            	$.each( data, function( key, value ) {
	            		  dataReturn.push( {id:data[key][1] ,text: data[key][0]} );
	            		});
	            	
	            	return { results: dataReturn };
	            }
	        },
	    
	      
	    });
	    
	    
	   
	    	
	    	/*.on("change", function(e) {
	            // mostly used event, fired to the original element when the value changes
	    		$('#horseForm').submit();
	          });*/
	});
	
	 $(function() {
			console.log("select 2 horse selector");
		    $('#trainerSel').select2({
		        minimumInputLength: 3,
		 
		        placeholder: 'Enter Horse Name',
		        
		        
		        ajax: {
		            url: "/turfclubPrograms/vetReports/service/trainersAll",
		            dataType: 'json',
		            type: "GET",
		          
		            quietMillis: 100,
		            data: function(term, page) {
		            	 
		            	//alert('h' + term);
		            	return {
		                    chars: term
		                };
		            },
		            results: function(data, page ) {
		            	//alert("Selected data is: "+JSON.stringify(data));
		            	var dataReturn = [];
		            	$.each( data, function( key, value ) {
		            		  dataReturn.push( {id:data[key][1] ,text: data[key][0]} );
		            		});
		            	
		            	return { results: dataReturn };
		            }
		        },
		    
		      
		    });
	
	 });
	
});


function checkErrors(){
	var ok = true;
	var trainerError = false;
	var horseError = false;
	var table =  document.getElementById("alertSheet");
	if($('#reportTypeSel').val() == "Trainer"){
		
	    var data = $('#trainerSel').select2('data');
		
		console.log(data.text);
		for (var r = 2; r < table.rows.length; r++){
		    trainer = table.rows[r].cells[1].innerHTML.replace(/<[^>]+>/g,"");

		    
			if (trainer.toLowerCase() == data.text.toLowerCase()  ){

				trainerError = true;
				$('#trainerSelError').show();
			}	

		}
	}
	else if($('#reportTypeSel').val() == "Horse"){
	
		var data = $('#horseSel').select2('data');
		
		console.log(data.text);
		for (var r = 2; r < table.rows.length; r++){
		    horse = table.rows[r].cells[0].innerHTML.replace(/<[^>]+>/g,"");

		    
			if (horse.toLowerCase() == data.text.toLowerCase()  ){

				horseError = true;
				$('#horseSelError').show();
				
			}	

		}
		
	}
	
	if(!horseError){
		$('#horseSelError').hide();
	}
	if(!trainerError){
		$('#trainerSelError').hide();
	}
	if($('#reportTypeSel').val() == "-1"){
		$('#reportTypeSelError').show();
		ok = false;
	}
	else{
		$('#reportTypeSelError').hide();
	}
	if(horseError || trainerError){
		ok = false;
	}
	console.log(horseError + " " + trainerError + " " + ok);
	return ok;
	
	
}

function filterTable() {
	var horsesearch = $('#horseSearch').val().toLowerCase();
	var trainersearch = $('#trainerSearch').val().toLowerCase();
	
	var table =  document.getElementById("alertSheet");

	for (var r = 2; r < table.rows.length; r++){
	    horse = table.rows[r].cells[0].innerHTML.replace(/<[^>]+>/g,"");
	    trainer = table.rows[r].cells[1].innerHTML.replace(/<[^>]+>/g,"");
		if (horse.toLowerCase().indexOf(horsesearch)>=0  && trainer.toLowerCase().indexOf(trainersearch)>=0){

				table.rows[r].style.display = '';

		}	
		else{
			table.rows[r].style.display = 'none';
		}
	}
	
	
}