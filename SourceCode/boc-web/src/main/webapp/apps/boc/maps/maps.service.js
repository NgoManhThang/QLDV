(function() {
    'use strict';

    angular.module('app').factory('MapsService', MapsService);

    MapsService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function MapsService ($resource, APP_REST_SERVICE) {
    	var contentType = 'application/octet-stream';
        var service =  $resource(APP_REST_SERVICE, {}, {
            getKv: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'maps/getKv',
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
            getProvince: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'maps/getProvince',
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
            getGeometryProvince: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getGeometryProvince'
                	+'?bocCode=:bocCode',
                params: {
                	bocCode: '@bocCode'
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
            getGeometryDistrict: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getGeometryDistrict'
                	+'?provinceCode=:provinceCode'
                	+'&bocCode=:bocCode',
                params: {
                	provinceCode: '@provinceCode',
                	bocCode: '@bocCode'
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
            getBocCode: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'maps/getBocCode',
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
            getMonthTitle: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'maps/getMonthTitle',
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
            getTopTenWorstBOC: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getTopTenWorstBOC'
                	+'?regionCode=:regionCode',
                params: {
                	regionCode: '@regionCode'
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
            getBadEmployeeWorstBOC: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getBadEmployeeWorstBOC'
                	+'?regionCode=:regionCode',
                params: {
                	regionCode: '@regionCode'
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
            getListBocModule: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'maps/getListBocModule',
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
            getBocDataChart: {
                method: 'POST',
                isArray: false,
                responseType: 'json',
                url: APP_REST_SERVICE + 'maps/getBocDataChart',
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
            getListTargetsDistrict: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getListTargetsDistrict'
                	+'?regionCode=:regionCode',
                params: {
                	regionCode: '@regionCode'
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
            getBocTitle: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getBocTitle'
                	+'?bocCodeGroup=:bocCodeGroup',
                params: {
                	bocCodeGroup: '@bocCodeGroup'
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
            getBocData: {
                method: 'GET',
                url: APP_REST_SERVICE + 'maps/getBocData'
                	+'?regionCode=:regionCode'
                	+'&bocCodeGroup=:bocCodeGroup',
                params: {
                	regionCode: '@regionCode',
                	bocCodeGroup: '@bocCodeGroup'
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
            getStatisticsTargets: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'statistics/getStatisticsTargets'
	            	+'?regionCode=:regionCode'
	            	+'&bocCode=:bocCode'
	            	+'&regionLevel=:regionLevel',
	            params: {
	            	regionCode: '@regionCode',
	            	bocCode: '@bocCode',
	            	regionLevel: '@regionLevel'
	            },
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
            getListTargetsStatistics: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'statistics/getListTargetsStatistics'
	            	+'?regionCode=:regionCode'
	            	+'&bocCode=:bocCode'
	            	+'&regionLevel=:regionLevel',
	            params: {
	            	regionCode: '@regionCode',
	            	bocCode: '@bocCode',
	            	regionLevel: '@regionLevel'
	            },
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
            getListStatisticsInfoEmployee: {
                method: 'GET',
                isArray: false,
                responseType: 'json',
                cache: false,
                url: APP_REST_SERVICE + 'statistics/getListStatisticsInfoEmployee',
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
            getStatisticsTargetsByBocCode: {
                method: 'GET',
                url: APP_REST_SERVICE + 'statistics/getStatisticsTargetsByBocCode'
	            	+'?regionCode=:regionCode'
	            	+'&bocCode=:bocCode'
	            	+'&regionLevel=:regionLevel',
	            params: {
	            	regionCode: '@regionCode',
	            	bocCode: '@bocCode',
	            	regionLevel: '@regionLevel'
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
            }
        });
        return service;
    }
})();
