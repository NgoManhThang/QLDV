// code style: https://github.com/johnpapa/angular-styleguide 
(function() {
    'use strict';

    angular.module('app').controller('SigninController', SigninController);

    SigninController.$inject = ['$rootScope', '$state', '$timeout', 'Auth', '$window', '$location', '$sessionStorage', '$localStorage', 'APP_NAME'];

    function SigninController ($rootScope, $state, $timeout, Auth, $window,$location, $sessionStorage, $localStorage, APP_NAME) {
        var vm = this;
    	$('#session-timeout-dialog').remove();
        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.password = null;
        vm.rememberMe = true;
        vm.username = null;
        
        vm.isOpen = false;
        vm.errorMessage = "";

        $timeout(function (){angular.element('#username').focus();});
        
        function cancel () {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
        }

        function login () {
            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }, function (response) {
            	var result = response.data;
            	if(result.key === "400") {
            		vm.errorMessage = "Mật khẩu không đúng !";
            		vm.authenticationError = true;
            	} else if(result.key === "403") {
            		vm.errorMessage = "Bạn không có quyền truy cập hệ thống !";
            		vm.authenticationError = true;
            	} else if(result.key === "500") {
            		vm.errorMessage = "Hệ thống tạm thời gián đoạn !";
            		vm.authenticationError = true;
            	} else if(result.key === "200") {
            		$localStorage.user = result.object;
            		$localStorage.applicationName = APP_NAME;
                    vm.authenticationError = false;
                    $rootScope.$broadcast('authenticationSuccess');
            	} else {
            		vm.errorMessage = "Không kết nối được với hệ thống !";
                    vm.authenticationError = true;
            	}
            });
        }
    }
})();
