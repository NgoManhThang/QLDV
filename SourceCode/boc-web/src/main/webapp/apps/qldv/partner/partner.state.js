(function () {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
        $stateProvider
            .state('boc.partner', {
                parent: 'boc',
                url: '/partner',
                templateUrl: 'apps/qldv/partner/partner.html',
                data: {title: 'Partner'},
                controller: "PartnerController",
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dashboard');
                        $translatePartialLoader.addPart('datatable');
                        $translatePartialLoader.addPart('user');
                        $translatePartialLoader.addPart('employee');
                        return $translate.refresh();
                    }],
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'apps/qldv/partner/partner.service.js'
                            , 'apps/qldv/partner/partner.controller.js'
                            , 'apps/qldv/partner/detail.controller.js'
                            , 'apps/qldv/common/qldv_common.service.js'
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
            })
            .state('partner-detail', {
                parent: 'boc.employee',
                url: '/detail',
                params: {
                    employeeId: null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$resource', '$rootScope', function ($stateParams, $state, $uibModal, $rootScope) {
                    $uibModal.open({
                        templateUrl: 'apps/qldv/manager_employee/detail.html',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('dashboard');
                                $translatePartialLoader.addPart('datatable');
                                $translatePartialLoader.addPart('user');
                                return $translate.refresh();
                            }]
                        },
                        controller: 'EmployeeDetailController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    });
                }]
            })
    }
})();
