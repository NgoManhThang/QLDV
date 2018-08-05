(function() {
    'use strict';

    angular.module('app').factory('ManaEmployeeService', ManaEmployeeService);

    ManaEmployeeService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function ManaEmployeeService ($resource, APP_REST_SERVICE) {
        var contentType = 'application/octet-stream';
        var service =  $resource(APP_REST_SERVICE, {}, {
            search: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocUser/search',
                responseType: 'json',
                cache: false,
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers: headers()
                    };
                }
            }
        });
        return service;
    }
})();
