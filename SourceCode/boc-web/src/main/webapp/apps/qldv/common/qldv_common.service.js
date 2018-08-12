/**
 * Created by VTN-PTPM-NV04 on 2/5/2018.
 */
(function () {
    'use strict';
    angular.module('app').factory('QldvCommonService', QldvCommonService);

    QldvCommonService.$inject = ['$resource', 'APP_REST_SERVICE'];

    function QldvCommonService($resource, APP_REST_SERVICE) {
        var multiContentType = 'multipart/form-data';
        var URL = APP_REST_SERVICE + "qldv-common";
        var service =  $resource(URL, {}, {
            search: {
                method: 'POST',
                isArray: true,
                url: URL + '/search',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            }
        });

        return service;
    }
})();