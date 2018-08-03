(function() {
    'use strict';

    angular.module('app').factory('resourcePathInterceptor', resourcePathInterceptor);

    resourcePathInterceptor.$inject = ['$rootScope', '$q', '$location'];

    function resourcePathInterceptor($rootScope, $q, $location) {
        var service = {
            request: request
            ,response:response
        };

        return service;

        function request (config) {
            // Mo hieu ung load trang ----------------------------------------------------------------------------------
           /* $("div.pace-inactive").show();
            $("div.pace-inactive").find(".pace-progress").css("transform", "translate3d(100%, 0px, 0px)");*/
            // ---------------------------------------------------------------------------------------------------------
            return config;
        }
        function response (config) {
            // Dong hieu ung load trang --------------------------------------------------------------------------------
            /*$("div.pace-inactive").hide();*/
            // ---------------------------------------------------------------------------------------------------------
            return config;
        }
    }
})();
