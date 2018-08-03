(function () {
    'use strict';
    // EDIT THIS FILE TO SETUP PROJECT
    angular
        .module('app')
        .constant('VERSION', "0.0.1-SNAPSHOT")
        .constant('DEBUG_INFO_ENABLED', true)
        .constant('BUILD_TIMESTAMP', "")
        .constant('APP_NAME', "BOC")
        .constant('APP_REST_SERVICE', "http://localhost:9090/api/boc/")
        //.constant('APP_REST_SERVICE', "http://10.61.2.249:8086/boc-api/api/boc/")
})();
