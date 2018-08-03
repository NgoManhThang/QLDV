(function() {
    'use strict';

    angular
        .module('app')
        .factory('authInterceptor', authInterceptor);

    authInterceptor.$inject = ['$rootScope', '$q', '$injector', '$location', '$localStorage', '$sessionStorage', '$translate'];

    function authInterceptor ($rootScope, $q, $injector, $location, $localStorage, $sessionStorage, $translate) {
    	var requestCount = 0;
        var isShowing = false;
        var xhrCreations = 0;
        var xhrResolutions = 0;
        var errorShowing = false;
        var _token = false;
        function isLoading() {
            return xhrResolutions < xhrCreations;
        }

        function updateStatus(config) {
            var loading = isLoading();
            if (loading && !isShowing) {
                isShowing = true;
                if(config.url !== undefined && config.url.indexOf("/api") > -1) {
                    $rootScope.$broadcast("loader_show");
                }
                //console.log('loader_show');
            } else {
                if (requestCount === 0){
                    isShowing = false;
                    $rootScope.$broadcast("loader_hide");
                    //console.log('loader_hide');
                }
            }
        }
        var service = {
            request: request
            ,response: response
            ,requestError: requestError
            ,responseError: responseError
        };

        return service;

        function request (config) {
        	var loading = isLoading();
            if (!loading) {
                errorShowing = false;
            }
            requestCount++;
            xhrCreations++;
            updateStatus(config);
            //if (!config || !config.url || /^http/.test(config.url)) return config;

            /*jshint camelcase: false */
            config.headers = config.headers || {};
            config.params = config.params || {};
            var token = $localStorage.authenticationToken || $sessionStorage.authenticationToken;
            if (token) {
                if(token != undefined && token.indexOf('Bearer') < 0) {
                    token = 'Bearer ' + token;
                }
                config.headers.Authorization = token;
            }
            
            // NamNH: add language code to header
            config.headers.LanguageCode = $translate.proposedLanguage() || $translate.use();
            
			/*if(config.method == "GET"){
				return config;
			}
			if(_token){ 
				config.data._csrf = _token;
				return config;
			}
			var deferred = $q.defer();
			var Auth = $injector.get('Auth');
        	Auth.getCsrfToken(function(result){
        		console.log(result.token);
            	config.headers["X-XSRF-TOKEN"] = result.token;
            	deferred.resolve(config);
            });
			return deferred.promise;*/
            return config;
        }
        
        function requestError(config) {
        	requestCount--;
            xhrResolutions++;
            updateStatus(config);
            return config;
        }
        
        function response(config) {
        	requestCount--;
            xhrResolutions++;
            updateStatus(config);
            return config;
        }
        
        function responseError(response) {
        	requestCount--;
            xhrResolutions++;
            updateStatus(response);
            var status = response.status;
            //console.log('Status:' + status);
            if(status <= 0) {
                $rootScope.$broadcast('response_status', {template: 'ERR_CONNECTION_REFUSED'});
            }else if(status == 401) {
                $rootScope.$broadcast('response_status', {template: 'ERR_UNAUTHORIZED'});
            }else if(status == 500) {
                $rootScope.$broadcast('response_status', {template: 'ERR_SYSTEM'});
            }
            return $q.reject(response);
        }
    }
})();

