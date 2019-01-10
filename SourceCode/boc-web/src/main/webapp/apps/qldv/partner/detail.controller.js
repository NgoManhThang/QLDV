// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('PartnerDetailController', PartnerDetailController);

    PartnerDetailController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'QldvCommonService', '$stateParams', 'PartnerService'];

    function PartnerDetailController($scope, $rootScope, $controller, $state, $uibModalInstance,
                                      $translate, $http, $timeout, $sessionStorage, $localStorage, QldvCommonService, $stateParams, PartnerService) {
        var vm = this;
        scopeHolder = $scope;

        var SUCCESS = "SUCCESS";

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });


            //region Init variable
            vm.lstPartnerType = [];
            vm.lstStatus = [];
            vm.detail = {};
            if (vm.stringIsNotNullOrEmpty($stateParams.partnerId)) {
                vm.title = $translate.instant('partner.title.update');
            } else {
                vm.title = $translate.instant('partner.title.addNew');
            }
            //endregion

            //region Function
            vm.loadDataCombo = loadDataCombo;
            vm.doExit = doExit;
            vm.doSaveData = doSaveData;
            vm.getDetail = getDetail;
            //endregion

            //region Function init
            vm.loadDataCombo();
            //endregion
        })();

        function loadDataCombo() {
            vm.lstPosition = [];
            vm.lstStatus = [];
            QldvCommonService.search({codeGroup: 'PARTNER_TYPE,STATUS_COMMON'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'PARTNER_TYPE') {
                        vm.lstPartnerType.push(obj);
                    } else {
                        vm.lstStatus.push(obj);
                    }
                });
                if (vm.stringIsNotNullOrEmpty($stateParams.partnerId)) {
                    vm.getDetail($stateParams.partnerId);
                }
            }, function (err) {

            });
        }

        function doSaveData() {

            if (!vm.stringIsNotNullOrEmpty(vm.detail.partnerCode)) {
                vm.showAlert("warning", $translate.instant('partner.message.partnerCodeNotNull'));
                return;
            }

            if (!vm.stringIsNotNullOrEmpty(vm.detail.partnerName)) {
                vm.showAlert("warning", $translate.instant('partner.message.partnerNameNotNull'));
                return;
            }

            if (!vm.stringIsNotNullOrEmpty(vm.detail.representName)) {
                vm.showAlert("warning", $translate.instant('partner.message.representNameNotNull'));
                return;
            }

            if (!vm.stringIsNotNullOrEmpty(vm.detail.phoneRepresent)) {
                vm.showAlert("warning", $translate.instant('partner.message.representPhoneNotNull'));
                return;
            }

            if (!vm.stringIsNotNullOrEmpty(vm.detail.partnerType)) {
                vm.showAlert("warning", $translate.instant('partner.message.partnerTypeNotNull'));
                return;
            }

            if (!vm.stringIsNotNullOrEmpty(vm.detail.status)) {
                vm.showAlert("warning", $translate.instant('partner.message.statusNotNull'));
                return;
            }

            var formData = new FormData();
            formData.append('dataString', JSON.stringify(vm.detail));

            PartnerService.saveData(formData).$promise.then(function (resp) {
                var result = resp.data.key;
                if (result === SUCCESS) {
                    vm.showAlert("success", $translate.instant('global.message.success'));
                    vm.doExit();
                    $rootScope.$broadcast("loadDataPartner");
                } else {
                    vm.showAlert("danger", $translate.instant('global.message.error'));
                }
            }, function (err) {
                vm.showAlert("danger", $translate.instant('global.message.error'));
            });
        }

        function doExit() {
            $stateParams.partnerId = null;
            $uibModalInstance.close();
            $state.go('boc.partner');
        }

        function getDetail(id) {
            PartnerService.getDetail({partnerId: id}).$promise.then(function (resp) {
                resp.data.status = resp.data.status + "";
                vm.detail = resp.data;
                console.log(resp);
            }, function (err) {

            });
        }
    }
})();