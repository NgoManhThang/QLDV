(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider,   MODULE_CONFIG) {
        $stateProvider.state('app.dashboard', {
            parent: 'app',
            url: '/dashboard',
            templateUrl: 'apps/boc/dashboard/dashboard.html',
            data : { title: 'Dashboard' },
            controller: "DashboardController",
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    $translatePartialLoader.addPart('datatable');
                    return $translate.refresh();
                }],
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        'apps/boc/dashboard/dashboard.service.js'
                        , 'apps/boc/dashboard/dashboard.controller.js'
                        , 'apps/base.controller.js'
                        , 'apps/base.service.js'
                        , 'vectorMap'
                    ]);
		        }
            }
        });
    }
})();
