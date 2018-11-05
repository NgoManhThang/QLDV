(function() {
    'use strict';

    angular.module('app').factory('UnionsService', UnionsService);

    UnionsService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function UnionsService ($resource, APP_REST_SERVICE) {
        var contentType = 'application/octet-stream';
        var URL = APP_REST_SERVICE + "unions";
        var URL_MEMBER = APP_REST_SERVICE + "member";
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
            searchMember: {
                method: 'POST',
                url: URL_MEMBER + '/searchMember',
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
            saveDataMember: {
                method: 'POST',
                url: URL_MEMBER + '/saveData',
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
            getDetailMember:{
                method: 'POST',
                url: URL_MEMBER + '/getDetail',
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
            deleteMember:{
                method: 'POST',
                url: URL_MEMBER + '/delete',
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
