(function() {
    'use strict';

    angular.module('app').factory('LookupService', LookupService);

    LookupService.$inject = ['$resource', 'APP_REST_SERVICE'];
    function LookupService ($resource, APP_REST_SERVICE) {
        var contentType = 'application/octet-stream';
        var pdfContentType = 'application/pdf';
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
            searchMember: {
                method: 'POST',
                url: URL + '/searchMember',
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
            getFilePathPdfPrinter:{
                method: 'POST',
                url: APP_REST_SERVICE + 'qldv-common/getPdfPrinter/:barCode',
                params: {
                    barCode: '@barCode'
                },
                responseType: 'arraybuffer',
                cache: false,
                headers: {accept: pdfContentType},
                transformResponse: function (data, headers) {
                    return {
                        response: new Blob([ data ], {
                            type: pdfContentType
                        }),
                        headers: headers()
                    };
                }
            },
            scanBarcode: {
                method: 'POST',
                url: URL + '/scanBarcode',
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
