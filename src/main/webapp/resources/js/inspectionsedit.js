$(function() {


	$('#inspectionsOfficialsOnDutys').select2();
	//showHideSections();



	$( "#formSubmit" ).click(function() {
		 $( "#save" ).val("yes");  
		$( "#inspectionsForm" ).submit();
		});
	
	//initialize the date picker
	$(".datepicker").datepicker({
		dateFormat : 'dd/mm/yy'
	});
	
	

	//load the data to the drop down.
	if($('#personTrainerId').val() != ''){
		console.log("T");
		$('#personId').val($('#personTrainerId').val());
	}
	else if($('#personHandlerId').val() != ''){
		console.log("H");
		$('#personId').val($('#personHandlerId').val());
	}
	else if($('#personUnregId').val() != ''){
		console.log("U");
		$('#personId').val($('#personUnregId').val());
	}
	else{
		console.log("Blank");
		$('#personTrainerId').text(null);
		$('#personTrainerId').val(null);
		$('#personHandlerId').text(null);
		$('#personHandlerId').val(null);
		$('#personUnregId').text(null);
		$('#personUnregId').val(null);
		$('#personId').text(null);
		$('#personId').val(null);
	}
	
	
	
    $('#personId').select2({
        minimumInputLength: 3,
 
        placeholder: 'Enter Name (3 characters or more)',
        
        
        ajax: {
            url: "/turfclubPrograms/inspections/service/persons",
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
            	console.log("Selected data is: "+JSON.stringify(data));
            	
            	var dataReturn = [];
            	$.each( data, function( key, value ) {
            		var type = data[key][0].substring(0, 1);
            		var personType = "";
            		if(type == 'T'){
            			personType = " (Trainer)";
            		}
            		else if(type == 'H'){
            			personType = " (Handler)";
            		}
            		else if(type == 'U'){
            			personType = " (Unregistered)";
            		}
            		dataReturn.push( {id:data[key][0] ,text: data[key][1] + " " +  data[key][2] + personType } );
            		});
            	
            	return { results: dataReturn };
            }
        },
    initSelection: function(element, callback) {
    	
    	//console.log("INIT:" + $(element).data("personName"));
    	callback({id: $(element).val(), text:  $(element).data("personName") });
    	
    }
      
    }).on("change", function(e) {
            // load the address for the new selected person
    		//console.log("change select2" + $('#personId').val());
    		//if a yard address is not available for selected person disable the use yard address button
    		var id = $('#personId').val().substring(1, $('#personId').val().length);
        	var type = $('#personId').val().substring(0, 1);
    		if(type == "T"){
    			$('#personTrainerId').text($('#personId').text());
    			$('#personTrainerId').val($('#personId').val());
    			$('#personHandlerId').text(null);
    			$('#personHandlerId').val(null);
    			$('#personUnregId').text(null);
    			$('#personUnregId').val(null);
    		}
    		else if(type == "H"){
    			$('#personTrainerId').text(null);
    			$('#personTrainerId').val(null);
    			$('#personHandlerId').text($('#personId').text());
    			$('#personHandlerId').val($('#personId').val());
    			$('#personUnregId').text(null);
    			$('#personUnregId').val(null);
    		}
    		else if (type == "U"){
    			$('#personTrainerId').text(null);
    			$('#personTrainerId').val(null);
    			$('#personHandlerId').text();
    			$('#personHandlerId').val();
    			$('#personUnregId').text($('#personId').text());
    			$('#personUnregId').val($('#personId').val());
    		}
    		else{
    			$('#personTrainerId').text(null);
    			$('#personTrainerId').val(null);
    			$('#personHandlerId').text(null);
    			$('#personHandlerId').val(null);
    			$('#personUnregId').text(null);
    			$('#personUnregId').val(null);
    			$('#personId').text(null);
    			$('#personId').val(null);
    		}
        	$.ajax({
        	    url : "/turfclubPrograms/inspections/service/address",
        	    type: "GET",
        	    data : {id: id, type : type},
        	    dataType: 'json',
        	    success: function(data, textStatus, jqXHR)
        	    {
        	       // console.log(data.regAddress1);
        	    	//data - response from server
        	        $("#address1").data("registeredAddress1", data.regAddress1);
            		$("#address2").data("registeredAddress2", data.regAddress2);
            		$("#address3").data("registeredAddress3", data.regAddress3);
            		$("#address4").data("registeredAddress4", data.regAddress4);
            		$("#address1").data("yardAddress1", data.yardAddress1);
            		$("#address2").data("yardAddress2", data.yardAddress2);
            		$("#address3").data("yardAddress3", data.yardAddress3);
            		$("#address4").data("yardAddress4", data.yardAddress4);
            		if($("#address1").data("yardAddress1") == ''){
            			$("#yardAddress").attr("disabled", true);
            			if($("#address1").data("registeredAddress1") == ''){
            				$("#address1").val(null);
            				$("#address2").val(null);	
            				$("#address3").val(null);	
            				$("#address4").val(null);
            			}
            			else{
            				$("#address1").val($("#address1").data("registeredAddress1"));
            				$("#address2").val($("#address2").data("registeredAddress2"));	
            				$("#address3").val($("#address3").data("registeredAddress3"));	
            				$("#address4").val($("#address4").data("registeredAddress4"));
            			}
            			
     
            		}
            		else{
               			$("#address1").val($("#address1").data("yardAddress1"));
            			$("#address2").val($("#address2").data("yardAddress2"));	
            			$("#address3").val($("#address3").data("yardAddress3"));	
            			$("#address4").val($("#address4").data("yardAddress4"));
            			$("#yardAddress").attr("disabled", false);
            		}
            		$("#registeredAddress").attr("disabled", false);
            		
        	    },
        	    error: function (jqXHR, textStatus, errorThrown)
        	    {
        	 
        	    }
        	});
    		
    		
    		
    });
    
   
    //setup address buttons
    $("#clearAddress")
	.click(
			function() {
				$("#address1").val(null);
				$("#address2").val(null);	
				$("#address3").val(null);	
				$("#address4").val(null);	
				
			});
    $("#yardAddress").attr("disabled", true);
    $("#yardAddress")
	.click(
			function() {
			$("#address1").val($("#address1").data("yardAddress1"));
			$("#address2").val($("#address2").data("yardAddress2"));	
			$("#address3").val($("#address3").data("yardAddress3"));	
			$("#address4").val($("#address4").data("yardAddress4"));	
				
			});
    $("#registeredAddress").attr("disabled", true);
    $("#registeredAddress")
	.click(
			function() {
				$("#address1").val($("#address1").data("registeredAddress1"));
				$("#address2").val($("#address2").data("registeredAddress2"));	
				$("#address3").val($("#address3").data("registeredAddress3"));	
				$("#address4").val($("#address4").data("registeredAddress4"));
				
			});
    
    $("#catSel").select2({
        placeholder: 'Select a category'
    });
    $("#subCatSel").select2({
       placeholder: 'Select a subcategory'
    });
    $("#statusSel").select2({
        placeholder: 'Select a status'
     });
    
    $("#catSel")
	.change(
			function() {
				console.log($("#catSel").val());
				if($("#catSel").val() == ''){
					$("#subCatSel").select2("val", "");
				}
				else{
					var inList = false;
					 $("#subCatSel option").each(function(){
						// console.log($(this).val());
						 //console.log(this.data("catId"));
						 //console.log($(this).attr('data-cat-id'));  
						// console.log($(this).data("catId"));
						 
					        console.log(inList + "'" +  $("#subCatSel").val().toString() + "' '"  + $(this).val().toString() + "'");   
						 if($(this).data("catId") == $("#catSel").val()){
					        console.log("show");	
							 $(this).removeClass( "hide_me" );
					        	
					        }
					        else{
					        	console.log("hide");
					        	$(this).addClass( "hide_me" );
					        }
					        
						 if($("#subCatSel").val().toString() == $(this).val().toString() && !$(this).hasClass('hide_me') ){
					        	inList = true;
					        } 
						 //alert($(this).text() + " : " + $(this).val());
					    });
			
					 if(!inList){
						 $("#subCatSel").select2("val", "");
					 }
				}
				
			});		
    $("#catSel").trigger("change");
    

    
    $('#inspectionsForm').validator({
		delay : '2000',
		disable: false
		
	});
    
    $("#formSubmit").click(function(e){
    	console.log("''" + $('#personId').val() +"''" +  $('#address1').val() + "''" );
    	$('#inspectionsForm').validator('validate');

    if ( $( ".has-error" ).length == 0 ){
    	 console.log("submit");
    	$('#inspectionsForm').submit();
    }
    });
    
    
    
    
});
/*
function showHideSections(){
	console.log($( "#typeSel" ).val());
	

	//check if the handler has a value and set handler type as select if it does
	if($('#handlerSelect').val() != 0 && $( "#typeSel" ).val() == 0){
		$( "#typeSel" ).val("Handler");
		$( "#handlerSelection" ).show();
		$( "#trainerSelection" ).hide();
	}
	//check if the trainer has a value and set handler type as select if it does
	else if($('#trainerSelect').val() != 0 && $( "#typeSel" ).val() == 0){
		$( "#typeSel" ).val("Trainer");
		$( "#handlerSelection" ).hide();
		$( "#trainerSelection" ).show();
	}

	
	//if the type selector is not selected hide the trainer and handler selectors
	if($( "#typeSel" ).val() == 0){
		$( "#handlerSelection" ).hide();
		$( "#trainerSelection" ).hide();
		if($( "#trainerDetail" ).length){
			$( "#trainerDetail" ).hide();
		}
		if($( "#handlerDetail" ).length){
			$( "#handlerDetail" ).hide();
		}
	}
	else if($( "#typeSel" ).val() == "Trainer"){
		$( "#handlerSelection" ).hide();
		$( "#trainerSelection" ).show();
		if($( "#trainerDetail" ).length && $('#trainerSelect').val() != 0){
			$( "#trainerDetail" ).show();
		}
		else{
			$( "#trainerDetail" ).hide();
		}
	}
	else if($( "#typeSel" ).val() == "Handler"){
		$( "#handlerSelection" ).show();
		$( "#trainerSelection" ).hide();
		if($( "#handlerDetail" ).length && $('#handlerSelect').val() != 0){
			$( "#handlerDetail" ).show();
		}
		else{
			$( "#handlerDetail" ).hide();
		}
	}

	//if the trainer or handler detail sections exist show the inspections form
	if($( "#trainerDetail" ).length || $( "#handlerDetail" ).length){
		$( "#formDetail" ).show();
	}
	else{
		$( "#formDetail" ).hide();
	}
}*/

