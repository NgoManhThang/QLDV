(function () {
    'use strict';

    angular
        .module('app')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('boc.lookup', {
                parent: 'boc',
                url: '/lookup',
                templateUrl: 'apps/qldv/lookup/lookup.html',
                data: {title: 'Lookup'},
                controller: "LookupController",
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('datatable');
                        $translatePartialLoader.addPart('partner');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('unions');
                        return $translate.refresh();
                    }],
                    loadPlugin: function ($ocLazyLoad) {
                        return $ocLazyLoad.load([
                            'apps/qldv/lookup/lookup.service.js'
                            , 'apps/qldv/lookup/lookup.controller.js'
                            , 'apps/qldv/lookup/list_member.controller.js'
                            , 'apps/qldv/partner/partner.service.js'
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
            .state('lst-member', {
                parent: 'boc.lookup',
                url: '/list-member',
                params: {
                    unionId: null
                },
                onEnter: ['$stateParams', '$state', '$uibModal', '$resource', '$rootScope', function ($stateParams, $state, $uibModal, $rootScope) {
                    $uibModal.open({
                        templateUrl: 'apps/qldv/lookup/list_member.html',
                        resolve: {
                            translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                                $translatePartialLoader.addPart('datatable');
                                $translatePartialLoader.addPart('partner');
                                $translatePartialLoader.addPart('global');
                                return $translate.refresh();
                            }]
                        },
                        controller: 'ListMemberController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg'
                    });
                }]
            })
    }
})();
