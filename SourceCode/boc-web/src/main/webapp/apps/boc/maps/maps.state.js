(function() {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
        $stateProvider.state('boc.maps', {
            parent: 'boc',
            url: '/maps',
            templateUrl: 'apps/boc/maps/maps.html',
            data : { title: 'Maps' },
            params: {
            	bocCode: null,
            	provinceCode: null,
            	districtCode: null,
            	regionLevel: null
            },
            controller: "MapsController",
            controllerAs: 'vm',
            resolve: {
            	authorize: ['Auth', function (Auth) {
	                    return Auth.authorize('DASHBOARD');
	                }
	            ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    $translatePartialLoader.addPart('maps');
                    return $translate.refresh();
                }],
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
						'ui.map'
						, 'ui.select'
						, 'angular-chart-js'
                        , 'apps/boc/maps/maps.service.js'
                        , 'apps/boc/maps/maps.controller.js'
                        , 'apps/base.controller.js'
                        , 'apps/base.service.js'
                    ]);
		        }
            }
        }).state('boc.statistics', {
            parent: 'boc',
            url: '/statistics',
            templateUrl: 'apps/boc/maps/statistics.html',
            data : { title: 'Statistics' },
            params: {
            	bocCode: null,
            	provinceCode: null,
            	districtCode: null,
            	regionLevel: null
            },
            controller: "StatisticsController",
            controllerAs: 'vm',
            resolve: {
            	authorize: ['Auth', function (Auth) {
	                    return Auth.authorize('DASHBOARD');
	                }
	            ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dashboard');
                    $translatePartialLoader.addPart('statistics');
                    return $translate.refresh();
                }],
                loadPlugin: function ($ocLazyLoad) {
                    return $ocLazyLoad.load([
						'ui.select'
						, 'angular-chart-js'
                        , 'apps/boc/maps/maps.service.js'
                        , 'apps/boc/maps/statistics.controller.js'
                        , 'apps/base.controller.js'
                        , 'apps/base.service.js'
                    ]);
		        }
            }
        });
    }
})();
