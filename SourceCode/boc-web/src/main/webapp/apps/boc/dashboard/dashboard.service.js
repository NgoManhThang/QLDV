(function() {
    'use strict';

    angular.module('app').factory('DashboardService', DashboardService);

    DashboardService.$inject = ['$resource', 'APP_REST_SERVICE'];

    function DashboardService ($resource, APP_REST_SERVICE) {
    	var contentType = 'application/octet-stream';
        var service =  $resource(APP_REST_SERVICE, {}, {
            getSession: {
                method: 'GET',
                isArray: false,
                url: APP_REST_SERVICE + 'getSession',
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers:headers()
                    };
                }
            },
            getEmployee: {
                method: 'POST',
                isArray: false,
                url: APP_REST_SERVICE + 'employee/search',
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers:headers()
                    };
                }
            },
            getUserByUserName: {
                method: 'GET',
                url: APP_REST_SERVICE + 'user/getUserByUserName?userName=:userName',
                params: {
                	userName: '@userName'
                },
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
            getFile: {
                method: 'GET',
                url: APP_REST_SERVICE + 'common/getPdfPrinter?barCode=:barCode',
                params: {
                	barCode: '@barCode'
                },
                responseType: 'arraybuffer',
                cache: false,
                headers: {accept: contentType},
                transformResponse: function (data, headers) {
                    return {
                        response: new Blob([ data ], {
                            type: contentType
                        }),
                        headers: headers()
                    };
                }
            }
        });
        return service;
    }
})();
