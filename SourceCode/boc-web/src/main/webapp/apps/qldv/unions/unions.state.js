(function () {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG'];

    function stateConfig($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
        $stateProvider
            .state('boc.unions', {
                parent: 'boc',
                url: '/unions',
                templateUrl: 'apps/qldv/unions/unions.html',
                data: {title: 'Partner'},
                controller: "UnionsController",
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('datatable');
                        $translatePartialLoader.addPart('partner');
                        $translatePartialLoader.addPart('unions');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'apps/qldv/unions/unions.service.js'
                            , 'apps/qldv/partner/partner.service.js'
                            , 'apps/qldv/unions/unions.controller.js'
                            , 'apps/qldv/unions/detail_unions.controller.js'
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
            .state('unions-detail', {
                parent: 'boc.unions',
                url: '/detail',
                params: {
                    unionId: null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$resource', '$rootScope', function ($stateParams, $state, $uibModal, $rootScope) {
                    $uibModal.open({
                        templateUrl: 'apps/qldv/unions/detail_unions.html',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('datatable');
                                $translatePartialLoader.addPart('unions');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }]
                        },
                        controller: 'UnionsDetailController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    });
                }]
            })
            // .state('member-detail', {
            //     parent: 'unions-detail',
            //     url: '/member',
            //     params: {
            //         unionMemberId: null
            //     },
            //     onEnter: ['$stateParams', '$state', '$uibModal', '$resource', '$rootScope', function ($stateParams, $state, $uibModal, $rootScope) {
            //         $uibModal.open({
            //             templateUrl: 'apps/qldv/unions/member_unions.html',
            //             resolve: {
            //                 translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //                     $translatePartialLoader.addPart('datatable');
            //                     $translatePartialLoader.addPart('unions');
            //                     $translatePartialLoader.addPart('global');
            //                     return $translate.refresh();
            //                 }]
            //             },
            //             controller: 'MemberDetailController',
            //             controllerAs: 'vm',
            //             backdrop: 'static',
            //             size: 'lg'
            //         });
            //     }]
            // })
    }
})();
