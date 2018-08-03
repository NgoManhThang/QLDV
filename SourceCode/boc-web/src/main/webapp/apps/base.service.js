/**
 * Created by VTN-PTPM-NV04 on 2/5/2018.
 */
(function () {
    'use strict';
    angular.module('app').factory('BaseService', BaseService);

    BaseService.$inject = ['$resource', 'APP_REST_SERVICE'];

    function BaseService($resource, APP_REST_SERVICE) {
        var service =  $resource(APP_REST_SERVICE, {}, {

        });

        return service;
    }
})();