angular.module('sub-elements')
// filter for select2 element
.filter(
		'multiFilter',
		function() {
			return function(items, selected) {
				var out = [];

				// iterates each item on the list
				if(angular.isArray(items) && selected.selected !== undefined && selected.selected != null) {
					//console.log(JSON.stringify(selected.selected));
					items.forEach(function(item) {
						var itemMatches = true;

						// iterates each selected element
						for (var i = 0; i < selected.selected.length; i++) {

							// if the item is already selected do not add to
							// list
							if(item.id == selected.selected[i].id) {
								itemMatches = false;
								break;
							}

						}
						// if the item does not match the search string do not
						// add to list
						if(item.value.toString().toLowerCase().indexOf(
								selected.search.toLowerCase()) === -1) {
							itemMatches = false;

						}
						if(itemMatches) {
							out.push(item);
						}
					});
				}
				else {
					// Let the output be the input untouched
					out = items;
				}

				return out;
			};
		})

.filter(
		'normalFilter',
		function() {
			return function(items, selected) {
				var out = [];
//console.log("selected search" + selected.search + " item" + selected.selected.id);
				// iterates each item on the list
				if(angular.isArray(items)) {
					items.forEach(function(item) {
						var itemMatches = true;

						//console.log("Item:" + JSON.stringify(item));
						if(selected.selected !== null && selected.selected !== undefined && item.id == selected.selected.id) {
							itemMatches = false;

						}

						// if the item does not match the search string do not
						// add to list
						if(item.value.toString().toLowerCase().indexOf(
								selected.search.toLowerCase()) === -1) {
							itemMatches = false;

						}
						//console.log(itemMatches)
						if(itemMatches) {
							
							out.push(item);
						}
					});
				}
				else {
					// Let the output be the input untouched
					out = items;
				}

				return out;
			};
		})

.filter('linkedFilter', function() {
	return function(items, filt) {
		var out = [];

		// iterates each item on the list
		if(angular.isArray(items) && filt.linkId !== undefined) {
			
			items.forEach(function(item) {
				console.log(item.linkedId +" " + filt.linkId);
				var itemMatches = (item.linkedId == filt.linkId);
				
				// iterates each selected element

				if(itemMatches) {
					out.push(item);
				}
			});
		}
		else {
			// Let the output be the input untouched
			out = items;
		}
			
		
		return out;
	};
});