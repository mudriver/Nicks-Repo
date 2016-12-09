$(function() {
	
	setTimeout(function() {
		$('#successMsg').hide();
	}, 5000);
	
	$('#trainerVerifiedStatus').select2({
		placeholder : "You must select a verified status",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if ($(data).filter(function() {
				return this.text.localeCompare(term) === 0;
			}).length === 0) {
				return {
					id : term,
					text : term
				};
			}
		},
		multiple : false,
		data : $.parseJSON($("#verifiedStatusEnum").val())
	});
	
	$('#trainerAddForm').validator({
		delay : '2000',
		disable : false,
		custom : {
			dateofbirth : function($el) {

				return checkDateOfBirth($el);
			},
			emailpattern : function($el) {

				if ($('#email').val() != '') {
					return $el.val().match(/.*?@.*?\..{2,}/);
				} else {
					return true;
				}
			}
		},
		errors : {
			dateofbirth : "You must enter a date of birth and employee must be at least 14 years old",
			emailpattern : "You must enter correct email eg. joe@bloggs.ie"
		}
	});

	$("#submitButton").click(function(e) {

		console.log("submit click");
		// validate the entire form
		$('#trainerAddForm').validator('validate');
		var submit = true;
		// check each error and if the error section is visible then do
		// not
		// submit form until error is fixed

		if ($('.has-error:visible').length > 0) {

			submit = false;

		}
		if(submit)
			$('#trainerAddForm').submit();
	});
});


function checkDateOfBirth(date) {
	// console.log("Test" + date.val());
	if(date.val() != '') {
		var pattern = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
		var arrayDate = date.val().match(pattern);
		var dt = new Date(arrayDate[3], arrayDate[2] - 1, arrayDate[1]);
		var input = dt;//$.datepicker.parseDate('dd/mm/yyyy', );
		// console.log(input);
		var newdate = new Date();
		newdate.setFullYear(newdate.getFullYear() - 14);
	
		// console.log(newdate);
		if (input < newdate) {
			// console.log("valid");
			return true;
		} else {
			// console.log("not valid");
			return false;
		}
	} else
		return false;
}

function validateDate(dtValue) {
	var dtRegex = new RegExp(/\b\d{1,2}[\/-]\d{1,2}[\/-]\d{4}\b/);
	return dtRegex.test(dtValue);
}