(function() {
    'use strict';

    angular
        .module('app')
        .factory('Auth', Auth);

    Auth.$inject = ['$rootScope', '$state', '$sessionStorage', '$localStorage', '$q', 'AuthServerProvider', '$http', 'APP_REST_SERVICE'];

    function Auth ($rootScope, $state, $sessionStorage, $localStorage, $q, AuthServerProvider, $http, APP_REST_SERVICE) {
        var service = {
            activateAccount: activateAccount,
            authorize: authorize,
            changePassword: changePassword,
            createAccount: createAccount,
            getPreviousState: getPreviousState,
            login: login,
            logout: logout,
            loginWithToken: loginWithToken,
            resetPasswordFinish: resetPasswordFinish,
            resetPasswordInit: resetPasswordInit,
            resetPreviousState: resetPreviousState,
            storePreviousState: storePreviousState,
            updateAccount: updateAccount,
            getCsrfToken: getCsrfToken
        };

        return service;
        
        function getCsrfToken (callback) {
        	$http.get(APP_REST_SERVICE + 'access/getCsrfToken').success(function(result){
        		var cb = callback || angular.noop;
        		return cb(result);
        	});
        }

        function activateAccount (key, callback) {
            var cb = callback || angular.noop;

            return Activate.get(key,
                function (response) {
                    return cb(response);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function authorize (role) {
        	return $http.get(APP_REST_SERVICE + 'access/checkSession').success(authenticateSuccess);
            function authenticateSuccess (data, status, headers) {
            	if($localStorage.user != null) {
            		var hasAuthority = $localStorage.user.listRole && $localStorage.user.listRole.indexOf(role) !== -1;
            		/*if (!hasAuthority) {
            			delete $localStorage.user;
                    	delete $localStorage.applicationName;
                    	$state.go('access.signin');
            		}*/
                }
                if(data == null || data == '') {
                	delete $localStorage.user;
                	delete $localStorage.applicationName;
                	$state.go('access.signin');
                }
            }
        }

        function changePassword (newPassword, callback) {
            var cb = callback || angular.noop;

            return Password.save(newPassword, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function createAccount (account, callback) {
            var cb = callback || angular.noop;

            return Register.save(account,
                function () {
                    return cb(account);
                },
                function (err) {
                    this.logout();
                    return cb(err);
                }.bind(this)).$promise;
        }

        function login (credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            AuthServerProvider.login(credentials)
                .then(loginThen)
                .catch(function (err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen (data) {
                return cb(data);
            }

            return deferred.promise;
        }

        function loginWithToken(jwt, rememberMe) {
            return AuthServerProvider.loginWithToken(jwt, rememberMe);
        }

        function logout () {
            AuthServerProvider.logout();
        }

        function resetPasswordFinish (keyAndPassword, callback) {
            var cb = callback || angular.noop;

            return PasswordResetFinish.save(keyAndPassword, function () {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function resetPasswordInit (mail, callback) {
            var cb = callback || angular.noop;

            return PasswordResetInit.save(mail, function() {
                return cb();
            }, function (err) {
                return cb(err);
            }).$promise;
        }

        function updateAccount (account, callback) {
            var cb = callback || angular.noop;

            return Account.save(account,
                function () {
                    return cb(account);
                },
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        }

        function getPreviousState() {
            var previousState = $sessionStorage.previousState;
            return previousState;
        }

        function resetPreviousState() {
            delete $sessionStorage.previousState;
        }

        function storePreviousState(previousStateName, previousStateParams) {
            var previousState = { "name": previousStateName, "params": previousStateParams };
            $sessionStorage.previousState = previousState;
        }
    }
})();
