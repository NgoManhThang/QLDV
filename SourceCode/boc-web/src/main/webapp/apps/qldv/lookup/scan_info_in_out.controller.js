// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('ScanAndInfoController', ScanAndInfoController);

    ScanAndInfoController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance', '$window',
        '$translate', '$http', '$timeout', '$sessionStorage', '$stateParams'];

    function ScanAndInfoController($scope, $rootScope, $controller, $state, $uibModalInstance, $window,
                                   $translate, $http, $timeout, $sessionStorage, $stateParams) {
        var vm = this;
        scopeHolder = $scope;

        var SUCCESS = "SUCCESS";
        var FORMAT_DATE = "dd/MM/yyyy";

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });


            //region Init variable
            vm.showScanCode = true;
            vm.showInfoLap = false;
            vm.showInfoMember = false;
            vm.showNone = false;
            vm.titleTypeScan = "";
            if ($stateParams.typeScan == "IN") {
                vm.titleTypeScan = "QUÉT MÃ VẠCH VÀO";
            } else {
                vm.titleTypeScan = "QUÉT MÃ VẠCH RA";
            }
            vm.objSearch = {};
            vm.result = {};
            console.log($stateParams.unionId);

            //endregion

            //region Function
            vm.btnScanCode = btnScanCode;
            vm.btnBack = btnBack;
            //endregion

            //region Function init
            //endregion
        })();


        function btnScanCode() {

        }

        function btnBack() {

        }
    }
})();