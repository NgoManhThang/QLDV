(function () {
    'use strict';

    angular.module('app').factory('fileManagerClient', fileManagerClient);

    fileManagerClient.$inject = ['$resource'];
    function fileManagerClient($resource) {
        var serviceBase = "http://localhost:9090/";
        return $resource(serviceBase + "api/bikt/download_upload_file/upload_file:fileName",
            {id: "@fileName"},
            {
                'query': {method: 'GET'},
                'save': {method: 'POST', transformRequest: angular.identity, headers: {'Content-Type': undefined}},
                'download': {method: 'GET', url: serviceBase + 'api/template', responseType: 'arraybuffer'},
                'remove': {method: 'DELETE', url: 'api/photo/:fileName', params: {name: '@fileName'}}
            });
    }

})();