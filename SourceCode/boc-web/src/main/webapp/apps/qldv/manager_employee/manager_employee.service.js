(function() {
    'use strict';

    angular.module('app').factory('ManageEmployeeService', ManageEmployeeService);

    ManageEmployeeService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function ManageEmployeeService ($resource, APP_REST_SERVICE) {
        var contentType = 'application/octet-stream';
        var URL = APP_REST_SERVICE + "employee";
        var service =  $resource(URL, {}, {
            search: {
                method: 'POST',
                url: URL + '/search',
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
