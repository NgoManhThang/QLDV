/**
 * @ngdoc function
 * @name app.controller:AppCtrl
 * @description
 * # MainCtrl
 * Controller of the app
 */

(function() {
    'use strict';
    angular
      .module('app')
      .controller('AppCtrl', AppCtrl);
      AppCtrl.$inject  = ['$scope', '$localStorage', '$location', '$rootScope', '$anchorScroll', '$timeout', '$window', 'Auth', '$state', '$sessionStorage', 'APP_NAME', 'APP_REST_SERVICE'];
      function AppCtrl($scope, $localStorage, $location, $rootScope, $anchorScroll, $timeout, $window, Auth, $state, $sessionStorage, APP_NAME, APP_REST_SERVICE) {
        $scope.userLoggedIn = {};
        loginSuccess();
        getBreadcrumb();
        checkStateMapStatistics();
        
        $rootScope.$on('$elementLoadJavascript', function(event, element){
    		var include = '<script type="text/javascript" src="js/settings.js"></script>'
    			+ '<script type="text/javascript" src="js/actions.js"></script>'
            	+ '<script type="text/javascript" src="bower_components/js-map-label/src/maplabel.js"></script>';
    		element.append(include);
            var js = element.find("script")[0].innerHTML;
      	  	eval(js);
        });
        
        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParam, error){
        	console.log(error);
        	event.preventDefault();
        	//$state.go('access.signin');
        	return;
        });
        
        $scope.menuIcons = {
            "BOC_STATISTICS" : "fa fa-desktop",
            "BOC_USER" : "fa-users",
            "BOC_TARGET" : "fa-bar-chart-o"
        };
        
    	function getBreadcrumb() {
    		$scope.breadcrumb = [];
        	var parentMenu = $scope.userLoggedIn.parentMenu;
        	var locationHref = $window.location.href;
        	if(parentMenu) {
        		for (var i = 0; i < parentMenu.length; i++) {
        			var objUrl = parentMenu[i].objectUrl.replace('.', '/');
        			if(locationHref.indexOf(objUrl) > -1) {
        				$scope.breadcrumb.push({
        	        		objectName: parentMenu[i].objectName
        	        	});
					}
        		}
        	} else {
        		$scope.breadcrumb.push({objectName: 'Dashboard'});
        	}
        }
    	
    	function checkStateMapStatistics() {
    		$localStorage.valueForMapStatistics = null;
    		var locationHref = $window.location.href;
    		if(locationHref.indexOf('boc/maps') > -1) {
    			locationHref = locationHref.replace('boc/maps', 'boc/statistics');
    			$window.location.href = locationHref;
			}
    	}
    	
    	$scope.navMenuClickOpenPageParent = function(name) {
        	$scope.breadcrumb = [];
        	$scope.breadcrumb.push({
        		objectName: name
        	});
        }
        
        $scope.navMenuClickOpenPageChild = function($event) {
        	console.log($event.target.textContent);
        }
        
        $scope.navMenuClick = function($event) {
        	var li = $($event.currentTarget);
            var active = li[0].querySelectorAll('.active');
            li.toggleClass('active');
            angular.element(active).removeClass('active');
        }
        
        $scope.logout = function() {
            Auth.logout();
        }
        
        $rootScope.$on('authenticationSuccess', loginSuccess);
        
        function loginSuccess(){
        	if($localStorage.applicationName == APP_NAME) {
        		$state.go('boc.statistics');
                if($localStorage.user != null && $localStorage.user != undefined) {
                	$scope.userLoggedIn = $localStorage.user;
                	if($scope.userLoggedIn.fileId != null) {
                		$scope.userLoggedIn.urlFileImage = APP_REST_SERVICE + "common/getFileById?fileId=" + $scope.userLoggedIn.fileId;
                	} else {
                		$scope.userLoggedIn.urlFileImage = './img/person.png';
                	}
             		var aaa = [{
             		                "childObjects":[],
             		                "ord":3,
             		                "parentId":-1,
             		                "status":1,
             		                "objectId":387930,
             		                "objectType":"M",
             		                "objectCode":"BOC_STATISTICS",
             		                "description":"",
             		                "objectName":"Dashboard",
             		                "objectUrl":"boc.statistics",
             		                "objectRole":"DASHBOARD"
             		            },{
             		                "childObjects":[],
             		                "ord":3,
             		                "parentId":-1,
             		                "status":1,
             		                "objectId":387930,
             		                "objectType":"M",
             		                "objectCode":"BOC_TARGET",
             		                "description":"",
             		                "objectName":"Quản lý mục tiêu",
             		                "objectUrl":"boc.target",
             		                "objectRole":"MANAGER_TARGET"
             		            },{
             		                "childObjects":[
    									/*{
    									     "childObjects":[],
    									     "ord":3,
    									     "parentId":-1,
    									     "status":1,
    									     "objectId":387930,
    									     "objectType":"M",
    									     "objectCode":"BOC_USER_A",
    									     "description":"",
    									     "objectName":"Quản lý người dùng A",
    									     "objectUrl":"boc.user",
    									     "objectRole":"MANAGER_USER"
    									 }, 
    									 {
    									      "childObjects":[],
    									      "ord":3,
    									      "parentId":-1,
    									      "status":1,
    									      "objectId":387930,
    									      "objectType":"M",
    									      "objectCode":"BOC_USER_B",
    									      "description":"",
    									      "objectName":"Quản lý người dùng B",
    									      "objectUrl":"boc.user",
    									      "objectRole":"MANAGER_USER"
    									  }*/
             		                ],
             		                "ord":3,
             		                "parentId":-1,
             		                "status":1,
             		                "objectId":387930,
             		                "objectType":"M",
             		                "objectCode":"BOC_USER",
             		                "description":"",
             		                "objectName":"Quản lý người dùng",
             		                "objectUrl":"boc.user",
             		                "objectRole":"MANAGER_USER"
             		            }];
        	     	$scope.userLoggedIn.parentMenu = aaa;
                }
        	} else {
        		Auth.logout();
        	}
        }
      }
})();
