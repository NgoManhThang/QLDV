(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
        $stateProvider.state('boc.target', {
            parent: 'boc',
            url: '/target',
            templateUrl: 'apps/boc/target/target.html',
            data : { title: 'Target' },
            controller: "TargetController",
            controllerAs: 'vm',
            resolve: {
            	authorize: ['Auth', function (Auth) {
	                    return Auth.authorize('MANAGER_TARGET');
	                }
	            ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    $translatePartialLoader.addPart('target');
                    $translatePartialLoader.addPart('datatable');
                    return $translate.refresh();
                }],
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
						'apps/boc/target/target.service.js'
                        , 'apps/boc/target/target.controller.js'
                        , 'apps/base.controller.js'
                        , 'apps/base.service.js'
                        , 'ui.select'
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
