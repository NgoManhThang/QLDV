(function() {
    'use strict';

    angular.module('app').factory('TargetService', TargetService);

    TargetService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function TargetService ($resource, APP_REST_SERVICE) {
    	var contentType = 'application/octet-stream';
        var service =  $resource(APP_REST_SERVICE, {}, {
        	search: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/search',
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
            getTypeTarget: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'bocTarget/getTypeTarget',
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
            getListProvince: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'bocTarget/getListProvince',
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
            getListDistrictByProvinceCode: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/getListDistrictByProvinceCode',
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
            deleteTarget: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/delete',
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
            exportTarget:{
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/exportTarget',
                responseType: 'arraybuffer',
                cache: false,
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function (data, headers) {
                    return {
                        response: new Blob([data], {
                        }),
                        headers: headers()
                    };
                }
            },
            downloadFileTemp: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/downloadFileTemp',
                responseType: 'arraybuffer',
                cache: false,
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function (data, headers) {
                    return {
                        response: new Blob([data], {
                        }),
                        headers: headers()
                    };
                }
            },
            importTarget: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocTarget/importData',
                responseType: 'arraybuffer',
                cache: false,
                processData: false,
                transformRequest: angular.identity,
                headers: {'Content-type': undefined},
                transformResponse: function(data, headers) {
                    return {
                        response: new Blob([data], {
                        }),
                        headers: headers()
                    };
                }
            }
        });
        return service;
    }
})();
