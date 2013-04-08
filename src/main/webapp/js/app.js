'use strict';

var app = angular.module('app', []).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/home', {
				templateUrl : 'partials/home.html',
				controller : 'homeCtrl'
			}).when('/contacts', {
				templateUrl : 'partials/contact-list.html',
				controller : 'listContactCtrl'
			})
			// .when('/contact/add', {templateUrl: 'partials/contact-form.html',
			// controller: 'addContactCtrl'})
			// .when('/contact/edit/:iso', {templateUrl:
			// 'partials/contact-form.html', controller: 'editContactCtrl'})
			.otherwise({
				redirectTo : '/home'
			});
		} ]);