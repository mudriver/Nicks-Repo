$(function() {
	
	setTimeout(function() {
		$('#successMsg').hide();
	}, 5000);
	
	$("#employeesTitle").select2({
		placeholder : "You must enter a title",
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
		data : $.parseJSON($("#titles").val())
	});

	$("#title").select2({
		placeholder : "You must enter a title",
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
		data : $.parseJSON($("#titles").val())
	});
	
	$("#sex").select2({
		placeholder : "You must select a gender",
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
		data : $.parseJSON($("#sexEnum").val())
	});
	
	$('#maritalStatus').select2({
		placeholder : "You must select a marital status",
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
		data : $.parseJSON($("#maritalEnum").val())
	});
	
	$('#county').select2({
		placeholder : "You must select a county",
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
		data : $.parseJSON($("#countiesEnum").val())
	});

	$('#country').select2({
		placeholder : "You must select a country",
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
		data : $.parseJSON($("#countriesEnum").val())
	});
	
	var select = $("#nationality");
	select.find('option[value="Irish"]').insertBefore(
			select.find('option:eq(1)'));
	select.find('option[value="British"]').insertBefore(
			select.find('option:eq(2)'));

	$('#nationality').select2({
		placeholder : "You must select an Nationality",
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
		data : $.parseJSON($("#nationalityEnum").val())
	});

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
			minlength : function($el) {
				if ($el.val().length == 0
						|| $el.val().length >= 7) {
					return true;
				}
				return false;
			},
			county : function($el) {
				if ($el.val().length > 0) {
					return true;
				}
				return false;
			},
			country : function($el) {

				if ($el.val().length > 0) {
					return true;
				}
				return false;
			},
			title : function($el) {

				if ($el.val().length > 0) {
					return true;
				}
				return false;
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
			minlength : "Phone number must be a minimum of 7 digits",
			county : "You must select or type a county",
			country : "You must select or type a country",
			title : "You must select or type a title",
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