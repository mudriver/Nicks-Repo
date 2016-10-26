angular
		.module("employee-edit-app",
				[ 'spring-security-csrf-token-interceptor' ])
		.controller(
				"employment-history-controller",
				[
						'$scope',
						'$http',
						function($scope, $http) {

							console.log($("#employee-id").val());

							$scope.showHide = {};
							$scope.showHide.addNew = true;
							$scope.variables = {};
							$scope.variables.histories;
							$scope.employeeid;
							$scope.functions = {};
							$scope.functions.toggleNew = function() {
								$scope.showHide.addNew = !$scope.showHide.addNew;
							};

							

							$scope.functions.getHistories = function() {
								console.log("RUN " + $scope.employeeid);
								// Writing it to the server
								//		
								var dataObj = {
									employeeId : 5872
								};
								var res = $http
										.post(
												'/turfclubPrograms/trainersEmployees/getHistories',
												dataObj);
								res.success(function(data, status, headers,
										config) {
									console.log(JSON.stringify(data));
									$scope.variables.histories = data;
								});
								res.error(function(data, status, headers,
										config) {
									alert("Server Failure");
								});
								// Making the fields empty
								//

							};

								$scope.functions.getHistories();

							

						} ]);