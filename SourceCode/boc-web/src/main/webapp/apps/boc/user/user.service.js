(function() {
    'use strict';

    angular.module('app').factory('UserService', UserService);

    UserService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function UserService ($resource, APP_REST_SERVICE) {
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
            },
            add: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocUser/add',
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
            edit: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocUser/edit',
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
            deleteUser: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocUser/delete'
	            	+'?userId=:userId',
	            params: {
	            	userId: '@userId'
	            },
                responseType: 'json',
                cache: false,
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers: headers()
                    };
                }
            },
            getDetail: {
                method: 'POST',
                url: APP_REST_SERVICE + 'bocUser/getDetail'
            		+'?userId=:userId',
                params: {
                	userId: '@userId'
                },
                responseType: 'json',
                cache: false,
                transformResponse: function(data, headers) {
                    return {
                        data: data,
                        headers: headers()
                    };
                }
            },
            getListBocRole: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'common/getListBocRole',
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
            getListBocUnit: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'common/getListBocUnit',
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
            getListBocRoleTarget: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'common/getListBocRoleTarget',
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
            getListBocConstant: {
            	method: 'POST',
                url: APP_REST_SERVICE + 'common/getListBocConstant'
            		+'?constantType=:constantType',
                params: {
                	constantType: '@constantType'
                },
                responseType: 'json',
                cache: false,
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
