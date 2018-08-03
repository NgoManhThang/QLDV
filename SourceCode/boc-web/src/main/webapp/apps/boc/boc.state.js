/**
 * Created by VTN-PTPM-NV04 on 2/5/2018.
 */
(function () {
    'use strict';

    angular.module('app').config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig ($stateProvider) {
        $stateProvider.state('boc', {
            abstract: true,
            url: '/boc',
            views: {
                '': {
                    templateUrl: './views/layout/layout.html'
                }
            },
            resolve: {
                authorize: ['Auth', function (Auth) {
                        return Auth.authorize('DASHBOARD');
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }
})();