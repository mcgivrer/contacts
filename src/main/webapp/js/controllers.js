'use strict';

/**
 * Contacts list controller.
 * 
 * @param $scope
 * @param $http
 */
app.controller('listContactCtrl', function listContactCtrl($scope, $http) {
	$scope.predicate = 'iso';
	$scope.reverse = 'false';
	$scope.contacts = {};
	$http.get('rest/contact').success(function(data) {
		$scope.contacts = data['contact'];
	});
});
