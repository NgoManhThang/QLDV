(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
        $stateProvider.state('boc.user', {
            parent: 'boc',
            url: '/user',
            templateUrl: 'apps/boc/user/user.html',
            data : { title: 'User' },
            controller: "UserController",
            controllerAs: 'vm',
            resolve: {
            	authorize: ['Auth', function (Auth) {
	                    return Auth.authorize('MANAGER_USER');
	                }
	            ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    $translatePartialLoader.addPart('datatable');
                    $translatePartialLoader.addPart('user');
                    return $translate.refresh();
                }],
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
						'apps/boc/user/user.service.js'
                        , 'apps/boc/user/user.controller.js'
                        , 'apps/base.controller.js'
                        , 'apps/base.service.js'
                        , 'ui.select'
                        , 'scripts/directives/its-select-tree.js'
                        , 'scripts/directives/tlt-drop-down.js'
                        , 'scripts/directives/multiselect.js'
                        , 'scripts/directives/its-datatable-common.js'
                        , 'scripts/directives/its-table-config.js'
                        , 'scripts/directives/file-upload-directive.js'
                    ]);
		        }
            }
        });
    }
})();
