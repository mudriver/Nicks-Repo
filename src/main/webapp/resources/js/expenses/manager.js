var id;
$(function() {
	var table = $('#example').DataTable( {
        dom: 'Bfrtip',
        "scrollX": true,
        "scrollY":        "400px",
        "scrollCollapse": true,
        "paging": false,
        "searching": false,
        buttons: [
            'colvis'
        ],
        columnDefs: [ {
            targets: 3,
            visible: false
        },{
            targets: 6,
            visible: false
        },{
            targets: 7,
            visible: false
        },{
            targets: 8,
            visible: false
        },{
            targets: 9,
            visible: false
        },{
            targets: 16,
            data: null,
            defaultContent: "<button class='btn btn-default'>Edit</button>"
        }]
    } );
	
	$('#example tbody').on( 'click', 'button', function () {
	    var data = table.row( $(this).parents('tr') ).data();
	    id = data[0];
	    var subsistanceChange = data[11];
	    var subsistanceReason = data[12];
	    var additionalMileage = data[14];
	    var mileageReason = data[15];
	    
	    $('#subsistanceChange').val(subsistanceChange);
	    $('#subsistanceReason').val(subsistanceReason);
	    $('#additionalMileage').val(additionalMileage);
	    $('#mileageReason').val(mileageReason);
	    $('#myModal').modal('show');
	} );
	
	if(yearVal != '')
		$('#year').find('option[value="'+yearVal+'"]').prop('selected', true);
	if(monthVal != '')
		$('#month').find('option[value="'+monthVal+'"]').prop('selected', true);
	
	$('#exportExcel').click(function() {
		
		var year = $('#year').val();
		var month = $('#month').val();
		
		
	});
});

function handleEditForm() {
	
	var subsistanceChange = $('#subsistanceChange').val();
    var subsistanceReason = $('#subsistanceReason').val();
    var additionalMileage = $('#additionalMileage').val();
    var mileageReason = $('#mileageReason').val();
    var message = '';
    
    if(subsistanceChange.trim() != '') {
    	if(subsistanceReason.trim() == '')
    		message += subsistanceReasonMessage;
    }
    
    if(additionalMileage.trim() != '') {
    	if(mileageReason.trim() == '')
    		message += "\n"+mileageReasonMessage;
    }
    
    if(message != '') {
    	$('#errorMsg').html(message);
    } else {
    	var formData = new FormData();
    	formData.append('subsistanceChange',subsistanceChange);
    	formData.append('subsistanceReason',subsistanceReason);
    	formData.append('additionalMileage',additionalMileage);
    	formData.append('mileageReason',mileageReason);
    	
    	/*$.ajax({
            type: "POST",
            data: {},
            url: "http://localhost:8080/turfclubPrograms/expenses/edit/"+id,
            dataType: 'json',
            success: function(data)
            {   
                
            	
            }
        });*/
    	
    	$.post('/turfclubPrograms/expenses/edit/'+id, $('#editForm').serialize(), function(data) {
    		
    	});
    }
    	
}

function searchRecord() {
	
	var year = $('#year').val();
	var month = $('#month').val();
	
	if(year != '' && month != '') {
		window.location.replace('/turfclubPrograms/expenses/search/'+year+'/'+month);
	}
}