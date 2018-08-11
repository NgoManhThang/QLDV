// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'ManageEmployeeService'];
    function EmployeeDetailController($scope, $rootScope, $controller, $state, $window,
                                    $translate, $http, $timeout, $sessionStorage, $localStorage, ManageEmployeeService){
        var vm = this;
        scopeHolder = $scope;

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });


        })();


        $rootScope.$on('$loadSortingDatatable', function(event, object){
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            if(object.pageName == 'pageUser') {
            }
        });


    }
})();