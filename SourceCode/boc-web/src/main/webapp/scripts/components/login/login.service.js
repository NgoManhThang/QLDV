(function() {
    'use strict';

    angular
        .module('app')
        .factory('LoginService', LoginService);

    LoginService.$inject = [];

    function LoginService () {
        var service = {
            open: open
        };

        return service;

        function open () {
            
        }
    }
})();
