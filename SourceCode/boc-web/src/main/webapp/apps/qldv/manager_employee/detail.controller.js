// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'ManageEmployeeService', 'QldvCommonService'];

    function EmployeeDetailController($scope, $rootScope, $controller, $state, $uibModalInstance,
                                      $translate, $http, $timeout, $sessionStorage, $localStorage, ManageEmployeeService, QldvCommonService) {
        var vm = this;
        scopeHolder = $scope;

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });


            //region Init variable
            vm.lstPosition = [];
            vm.lstStatus = [];
            //endregion

            //region Function
            vm.loadDataCombo = loadDataCombo;
            vm.doExit = doExit;
            vm.doSaveData = doSaveData;
            //endregion

            //region Function init
            vm.loadDataCombo();
            //endregion

        })();

        function loadDataCombo() {
            vm.lstPosition = [];
            vm.lstStatus = [];
            QldvCommonService.search({codeGroup: 'POSITION_EMPLOYEE,STATUS_COMMON'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'POSITION_EMPLOYEE') {
                        vm.lstPosition.push(obj);
                    } else {
                        vm.lstStatus.push(obj);
                    }
                });
            }, function (err) {

            });
        }

        function doSaveData() {

        }

        function doExit() {
            $uibModalInstance.close();
            $state.go('boc.employee');
        }


        $rootScope.$on('$loadSortingDatatable', function (event, object) {
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            if (object.pageName == 'pageUser') {
            }
        });


    }
})();