/**
 * Created by VTN-PTPM-NV04 on 2/5/2018.
 */
(function () {
    'use strict';
    angular.module('app').factory('CommonServices', CommonServices);

    CommonServices.$inject = ['$resource', 'APP_REST_SERVICE'];
    
    function CommonServices($resource, APP_REST_SERVICE) {
    	var multiContentType = 'multipart/form-data';
        var service =  $resource(APP_REST_SERVICE, {}, {
            getUnit: {
                method: 'POST',
                isArray: true,
                url: APP_REST_SERVICE + 'common/getUnit',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            downloadFileById: {
            	method: 'POST',
                url: APP_REST_SERVICE + 'common/downloadFileById',
                responseType: 'arraybuffer',
                cache: false,
                headers: {accept: multiContentType},
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        response: new Blob([ data ], {
                            type: multiContentType
                        }),
                        headers: headers()
                    };
                }
            },
            getFile: {
                method: 'POST',
                url: APP_REST_SERVICE + 'common/getFile',
                responseType: 'arraybuffer',
                cache: false,
                headers: {accept: xlsxContentType},
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        response: new Blob([ data ], {
                            type: xlsxContentType
                        }),
                        headers: headers()
                    };
                }
            },
            downloadFileTemp: {
                method: 'POST',
                url: APP_REST_SERVICE + 'common/downloadFileTemp',
                responseType: 'arraybuffer',
                cache: false,
                headers: {accept: xlsxContentType},
                transformRequest: function (data) {
                    return angular.toJson(data);
                },
                transformResponse: function(data, headers) {
                    return {
                        response: new Blob([ data ], {
                            type: xlsxContentType
                        }),
                        headers: headers()
                    };
                }
            }
        });

        return service;
    }
})();