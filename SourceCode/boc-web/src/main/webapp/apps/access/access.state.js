(function () {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('access', {
			url: '/access',
			template: '<div ui-view></div>',
			resolve: {
				translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
					$translatePartialLoader.addPart('account');
					return $translate.refresh();
				}]
			}
		}).state('access.signin', {
			url: '/signin',
			templateUrl: 'apps/access/signin/signin.html',
			data : { title: 'SignIn' },
			controller: "SigninController",
			controllerAs: 'vm',
			resolve: {
				loadPlugin: function ($ocLazyLoad) {
					return $ocLazyLoad.load(['apps/access/signin/signin.controller.js']);
				}
			}
		}).state('access.signup', {
			url: '/signup',
			templateUrl: 'apps/access/signup/signup.html'
		})
		.state('access.forgot-password', {
			url: '/forgot-password',
			templateUrl: 'apps/access/forgot-password/forgot-password.html'
		})
		.state('access.lockme', {
			url: '/lockme',
			templateUrl: 'apps/access/lockme/lockme.html'
		});
    }
})();
