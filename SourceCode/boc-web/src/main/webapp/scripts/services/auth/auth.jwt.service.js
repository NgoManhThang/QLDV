(function() {
    'use strict';

    angular
        .module('app')
        .factory('AuthServerProvider', AuthServerProvider);

    AuthServerProvider.$inject = ['$http', '$localStorage', '$sessionStorage', '$state', '$q', 'APP_REST_SERVICE'];

    function AuthServerProvider ($http, $localStorage, $sessionStorage, $state, $q, APP_REST_SERVICE) {
        var service = {
            getToken: getToken,
            login: login,
            loginWithToken: loginWithToken,
            storeAuthenticationToken: storeAuthenticationToken,
            logout: logout
        };

        return service;

        function getToken () {
            return $localStorage.authenticationToken || $sessionStorage.authenticationToken;
        }

        function login (credentials) {

            var data = {
                username: credentials.username,
                password: credentials.password,
                rememberMe: credentials.rememberMe
            };
            return $http.post(APP_REST_SERVICE + 'access/login', data).success(authenticateSuccess);
            function authenticateSuccess (data, status, headers) {
                var bearerToken = data.authToken;
                if (angular.isDefined(bearerToken) && bearerToken.slice(0, 7) === 'Bearer ') {
                    var jwt = bearerToken.slice(7, bearerToken.length);
                    service.storeAuthenticationToken(jwt, credentials.rememberMe);
                    return jwt;
                }
            }
        }

        function loginWithToken(jwt, rememberMe) {
            var deferred = $q.defer();

            if (angular.isDefined(jwt)) {
                this.storeAuthenticationToken(jwt, rememberMe);
                deferred.resolve(jwt);
            } else {
                deferred.reject();
            }

            return deferred.promise;
        }

        function storeAuthenticationToken(jwt, rememberMe) {
            if(rememberMe){
                $localStorage.authenticationToken = jwt;
            } else {
                $sessionStorage.authenticationToken = jwt;
            }
        }

        function logout () {
        	return $http.get(APP_REST_SERVICE + 'access/logout').success(logoutSuccess);
            function logoutSuccess (data, status, headers) {
            	delete $localStorage.authenticationToken;
                delete $sessionStorage.authenticationToken;
                $state.go('access.signin');
            }
        }
    }
})();
