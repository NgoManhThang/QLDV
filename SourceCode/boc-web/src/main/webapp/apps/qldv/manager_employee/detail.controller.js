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

        var SUCCESS = "SUCCESS";
        var regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });


            //region Init variable
            vm.lstPosition = [];
            vm.lstStatus = [];
            vm.detail = {};
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

            if(!vm.stringIsNotNullOrEmpty(vm.detail.code)){
                vm.showAlert("warning", $translate.instant('employee.message.employeeCodeNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.fullName)){
                vm.showAlert("warning", $translate.instant('employee.message.fullNameNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.password)){
                vm.showAlert("warning", $translate.instant('employee.message.passwordNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.retypePassword)){
                vm.showAlert("warning", $translate.instant('employee.message.retypePasswordNotnull'));
                return;
            }

            if(vm.detail.password !== vm.detail.retypePassword){
                vm.showAlert("warning", $translate.instant('employee.message.passwordMissmatched'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.email)){
                vm.showAlert("warning", $translate.instant('employee.message.emailNotnull'));
                return;
            }

            if(!regexEmail.test(vm.detail.email)){
                vm.showAlert("warning", $translate.instant('employee.message.emailNotFormat'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.status)){
                vm.showAlert("warning", $translate.instant('employee.message.statusNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.userName)){
                vm.showAlert("warning", $translate.instant('employee.message.userNameNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.phone)){
                vm.showAlert("warning", $translate.instant('employee.message.phoneNotnull'));
                return;
            }

            if(!vm.stringIsNotNullOrEmpty(vm.detail.position)){
                vm.showAlert("warning", $translate.instant('employee.message.positionNotnull'));
                return;
            }

            var formData = new FormData();
            formData.append('dataString', JSON.stringify(vm.detail));

            ManageEmployeeService.saveData(formData).$promise.then(function (resp) {
                var result = resp.data.key;
                if (result === SUCCESS) {
                    vm.showAlert("success", $translate.instant('global.message.success'));
                    vm.doExit();
                    $rootScope.$broadcast("loadDataEmployee");
                } else {
                    vm.showAlert("danger", $translate.instant('global.message.error'));
                }
            }, function (err) {
                vm.showAlert("danger", $translate.instant('global.message.error'));
            });
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