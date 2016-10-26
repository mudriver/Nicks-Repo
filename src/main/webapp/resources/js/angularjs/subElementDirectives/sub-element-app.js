angular.module('sub-elements',['spring-security-csrf-token-interceptor','ui.bootstrap']).factory('RecursionHelper', ['$compile', function($compile){
	return {
		/**
		 * Manually compiles the element, fixing the recursion loop.
		 * @param element
		 * @param [link] A post-link function, or an object with function(s) registered via pre and post properties.
		 * @returns An object containing the linking functions.
		 */
		compile: function(element, link){
			// Normalize the link parameter
			if(angular.isFunction(link)){
				link = { post: link };
			}

			// Break the recursion loop by removing the contents

				var contents = element.contents().remove();

			
			//console.log(element.contents());
			var compiledContents;
			return {
				pre: (link && link.pre) ? link.pre : null,
				/**
				 * Compiles and re-adds the contents
				 */
				post: function(scope, element, attrs, ctrl){
					console.log(ctrl.complete);
					console.log("Field Key: " + ctrl.fieldKey);
					if(!ctrl.complete){
						console.log("Loop");
						contents.removeAttr("nested");
						contents.removeAttr("data-nested");
						contents.attr('data-nested', '');
						// Compile the contents
						if(!compiledContents){
							compiledContents = $compile(contents);
						}
						// Re-add the compiled contents to the element
						compiledContents(scope, function(clone){
							element.append(clone);
						});

						// Call the post-linking function, if any
						if(link && link.post){
							link.post.apply(null, arguments);
						}
					}
					else{
						console.log("Complete Loop");
						contents.removeAttr("nested");
						contents.removeAttr("data-nested");
						if(ctrl.fieldKey == ""){
							contents.removeAttr("data-data");
							contents.attr("data-data", 'ctrl.data');
						}
						
						
						contents.attr('data-dynamic-element', 'ctrl.dyn');
						console.log(contents);
						if(!compiledContents){
							compiledContents = $compile(contents);
						}
						// Re-add the compiled contents to the element
						compiledContents(scope, function(clone){
							
							element.append(clone);
						});
					}
					
				}
			};
		}
	};
}]);