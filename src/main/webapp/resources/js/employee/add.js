$(function() {
	$("#employeesTitle").select2({
		placeholder : "You must enter a title",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
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
	
	$("#teCard\\.cardsCardType").select2({
		placeholder : "You must select a Card Type",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
				return this.text.localeCompare(term) === 0;
			}).length === 0) {
				return {
					id : term,
					text : term
				};
			}
		},
		multiple : false,
		data : $.parseJSON($("#cardTypeEnum").val())
	});

	$("#employeesSex").select2({
		placeholder : "You must select a gender",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
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
	$('#employeesMaritalStatus').select2({
		placeholder : "You must select a marital status",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
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
	$('#employeesAddress4').select2({
		placeholder : "You must select a county",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
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
	$('#employeesAddress5').select2({
		placeholder : "You must select a country",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
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
	$('#employeeCategoryOfEmployment').select2({
		placeholder : "You must select an Employment Category",
		allowClear : true,
		createSearchChoice : function(term, data) {
			if($(data).filter(function() {
				return this.text.localeCompare(term) === 0;
			}).length === 0) {
				return {
					id : term,
					text : term
				};
			}
		},
		multiple : false,
		data : $.parseJSON($("#employmentCatEnum").val())
	});
});