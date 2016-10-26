$(document)
		.ready(
				function() {
$('#formSubmit').click(function(event){

 	   event.preventDefault();
 	   if($('#reportTypeSel').val() != -1){
 		  $('#vetReportNewForm').submit(); 
 	   }
 	   else{
 		  $('#reportTypeError').show();
 	   }
 	   

 });
$('#reportTypeError').hide();
				});