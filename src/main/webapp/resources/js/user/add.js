$(function() {

	setTimeout(function() {
		$('#successMsg').hide();
		$('#errorMsg').hide();
	}, 5000);

	$("#submitButton").click(function(e) {

		console.log("submit click");
		// validate the entire form
		$('#userAddForm').validator('validate');
		var submit = true;
		// check each error and if the error section is visible then do
		// not
		// submit form until error is fixed

		if ($('.has-error:visible').length > 0) {

			submit = false;
		} else if(submit) {
			
			$('#userAddForm').submit();
		}
	});
});