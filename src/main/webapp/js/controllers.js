'use strict';

/**
 * Contacts list controller.
 * 
 * @param $scope
 * @param $http
 */
app.controller('listContactCtrl', function listCountryCtrl($scope, $http) {
	$scope.predicate = 'iso';
	$scope.reverse = 'false';
	$scope.country = {};
	$http.get('rest/contacts').success(function(data) {
		$scope.contacts = data;
	});
});
