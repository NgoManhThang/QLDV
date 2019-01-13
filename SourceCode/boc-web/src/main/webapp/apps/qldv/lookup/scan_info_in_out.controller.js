// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('ScanAndInfoController', ScanAndInfoController);

    ScanAndInfoController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance', '$window',
        '$translate', '$http', '$timeout', '$sessionStorage', '$stateParams', 'LookupService'];

    function ScanAndInfoController($scope, $rootScope, $controller, $state, $uibModalInstance, $window,
                                   $translate, $http, $timeout, $sessionStorage, $stateParams, LookupService) {
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
            vm.objSearch.typeScan = $stateParams.typeScan;
            LookupService.scanBarcode(vm.objSearch).$promise.then(function (resp) {
                console.log(resp);
                vm.result = resp.data;

                vm.result.urlBarCodeUser = vm.getImageByBarcode(vm.result.barCodeUser);
                vm.result.urlBarCodeComputer = vm.getImageByBarcode(vm.result.barCodeComputer);

                vm.result.imageId = vm.getUrlImageByFileId(vm.result.fileIdCMT);
                vm.result.imageLaptop = vm.getUrlImageByFileId(vm.result.fileIdComputer);

                if (vm.objSearch.barCode == vm.result.barCodeUser) {
                    vm.showScanCode = false;
                    vm.showInfoLap = false;
                    vm.showInfoMember = true;
                    vm.showNone = false;
                    var audio = new Audio('content/assets/audio/ting_ting.mp3');
                    audio.play();
                } else if (vm.objSearch.barCode == vm.result.barCodeComputer) {
                    vm.showScanCode = false;
                    vm.showInfoLap = true;
                    vm.showInfoMember = false;
                    vm.showNone = false;
                    var audio = new Audio('content/assets/audio/ting_ting.mp3');
                    audio.play();
                } else {
                    vm.showScanCode = false;
                    vm.showInfoLap = false;
                    vm.showInfoMember = false;
                    vm.showNone = true;
                    var audio = new Audio('content/assets/audio/fail_button.mp3');
                    audio.play();
                }
            }, function (err) {

            });
        }

        function btnBack() {
            $stateParams.typeScan = null;
            $uibModalInstance.close();
            $state.go('boc.lookup');
        }
    }
})();