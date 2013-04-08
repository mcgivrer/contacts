'user strict';

/**
 * Service Internationalization (i18n)
 */
app.factory('i18n', function($locale, $http) {

	var res = {};
	$http({
		method : 'GET',
		url : "js/i18n/messages.json"
	}).success(function(data) {
		res = data;
	}).error(function(data, status) {

		errorMessage = 'Unable to contact i18n service for translation.';
		error = true;
	});

	return {
		getString : function(key) {
			return res[$locale.id][key];
		}
	};
});