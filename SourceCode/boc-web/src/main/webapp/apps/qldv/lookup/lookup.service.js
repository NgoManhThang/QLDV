(function() {
    'use strict';

    angular.module('app').factory('LookupService', LookupService);

    LookupService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function LookupService ($resource, APP_REST_SERVICE) {
        var contentType = 'application/octet-stream';
        var URL = APP_REST_SERVICE + "lookup";
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
            },
            saveData: {
                method: 'POST',
                url: URL + '/saveData',
                responseType: 'json',
                cache: false,
                processData: false,
                headers: {'Content-type': undefined},
                transformRequest: angular.identity,
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers: headers()
                    };
                }
            },
            getDetail: {
                method: 'POST',
                url: URL + '/getDetail',
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
            },
            delete: {
                method: 'POST',
                url: URL + '/delete',
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
