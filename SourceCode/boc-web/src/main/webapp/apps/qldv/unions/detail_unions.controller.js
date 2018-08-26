// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('UnionsDetailController', UnionsDetailController);

    UnionsDetailController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'QldvCommonService', '$stateParams', 'PartnerService', 'UnionsService'];

    function UnionsDetailController($scope, $rootScope, $controller, $state, $uibModalInstance,
                                    $translate, $http, $timeout, $sessionStorage, $localStorage, QldvCommonService, $stateParams, PartnerService, UnionsService) {
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
            vm.lstUnionType = [];
            vm.lstStatus = [];
            vm.lstPartnerId = [];
            vm.lstPlacesId = [];
            vm.lstEmployeeId = [];
            vm.detail = {};
            vm.loadCombo = {};
            vm.objDate = {
                fromDate: "",
                toDate: ""
            };
            if (vm.stringIsNotNullOrEmpty($stateParams.unionId)) {
                vm.title = $translate.instant('unions.title.update');
            } else {
                vm.title = $translate.instant('unions.title.addNew');
            }
            vm.showAddMember = true;
            //Member
            vm.lstMemberType = [];
            vm.lstNationalId = [];
            vm.member = {};
            vm.titleMember = "";
            vm.showMember = false;
            //endregion

            //region Function
            vm.loadDataCombo = loadDataCombo;
            vm.doExit = doExit;
            vm.doSaveData = doSaveData;
            vm.getDetail = getDetail;
            vm.openPopupDate = openPopupDate;
            vm.changeEmployee = changeEmployee;
            vm.doAddMember = doAddMember;
            //endregion

            //region Function init
            vm.loadDataCombo();
            //endregion
        })();

        function loadDataCombo() {

            //region Đối tác
            vm.loadCombo.page = 1;
            vm.loadCombo.pageSize = 1000000;
            vm.loadCombo.sortName = 'partnerCode';
            vm.loadCombo.sortType = 'asc';
            vm.lstPartner = [];
            PartnerService.search(vm.loadCombo).$promise.then(function (resp) {
                var lstData = resp.data.data;
                $.each(lstData, function (i, obj) {
                    obj.code = obj.partnerId;
                    obj.decode = obj.partnerName;
                });
                vm.lstPartnerId = lstData;
            }, function (err) {

            });
            //endregion

            //region Loại đoàn và trạng thái đoàn
            vm.lstUnionType = [];
            vm.lstStatus = [];
            QldvCommonService.search({codeGroup: 'STATUS_UNIONS,UNIONS_TYPE'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'UNIONS_TYPE') {
                        vm.lstUnionType.push(obj);
                    } else {
                        vm.lstStatus.push(obj);
                    }
                });
            }, function (err) {

            });
            //endregion

            //region Địa điểm
            vm.lstPlacesId = [];
            QldvCommonService.getPlaceById({}).$promise.then(function (resp) {
                vm.lstPlacesId = resp;
            }, function (err) {

            });
            //endregion

            //region Nhân viên
            vm.lstEmployeeId = [];
            QldvCommonService.getEmployeeByIdOrUserName({}).$promise.then(function (resp) {
                vm.lstEmployeeId = resp;
                if (vm.stringIsNotNullOrEmpty($stateParams.unionId)) {
                    vm.getDetail($stateParams.unionId);
                }
            }, function (err) {

            });
            //endregion
        }

        function doSaveData(type) {
            if (type === 'member') {
                var formData = new FormData();
                formData.append('dataString', JSON.stringify(vm.member));

                UnionsService.saveDataMember(formData).$promise.then(function (resp) {
                    console.log(resp);
                }, function (err) {

                });
            } else {

                /*if (!vm.stringIsNotNullOrEmpty(vm.detail.partnerCode)) {
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
                }*/

                vm.detail.fromDate = vm.filterDate(vm.objDate.fromDate, FORMAT_DATE);
                vm.detail.toDate = vm.filterDate(vm.objDate.toDate, FORMAT_DATE);

                var formData = new FormData();
                formData.append('dataString', JSON.stringify(vm.detail));

                UnionsService.saveData(formData).$promise.then(function (resp) {
                    var result = resp.data.key;
                    if (result === SUCCESS) {
                        vm.showAlert("success", $translate.instant('global.message.success'));
                        // vm.doExit();
                        vm.showAddMember = true;
                        $rootScope.$broadcast("loadDataUnions");
                    } else {
                        vm.showAlert("danger", $translate.instant('global.message.error'));
                    }
                }, function (err) {
                    vm.showAlert("danger", $translate.instant('global.message.error'));
                });
            }
        }

        function doAddMember() {
            vm.lstMemberType = [];
            vm.lstNationalId = [];
            QldvCommonService.search({codeGroup: 'MEMBER_TYPE,NATIONAL'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'MEMBER_TYPE') {
                        vm.lstMemberType.push(obj);
                    } else {
                        vm.lstNationalId.push(obj);
                    }
                });
                console.log(vm.detail.partnerId);
                console.log(vm.lstPartnerId);
                for (var i = 0; i < vm.lstPartnerId.length; i++){
                    if(parseInt(vm.detail.partnerId) === vm.lstPartnerId[i].partnerId){
                        vm.member.unionName = vm.lstPartnerId[i].partnerName;
                    }
                }
                vm.member.unionId = vm.detail.unionId;
                vm.titleMember = $translate.instant('unions.title.addMember');
                vm.showMember = true;
            }, function (err) {

            });
        }

        function doExit(type) {
            if (type === "member") {
                vm.showMember = false;
            } else {
                $stateParams.unionId = null;
                $uibModalInstance.close();
                $state.go('boc.unions');
            }
        }

        function getDetail(id) {
            UnionsService.getDetail({unionId: id}).$promise.then(function (resp) {
                vm.detail = resp.data;
                vm.detail.partnerId = vm.detail.partnerId + "";
                vm.objDate.fromDate = new Date(resp.data.fromDate);
                vm.objDate.toDate = new Date(resp.data.toDate);
                console.log(resp);
            }, function (err) {

            });
        }

        function changeEmployee() {
            if (vm.stringIsNotNullOrEmpty(vm.detail.employeeId)) {
                QldvCommonService.getEmployeeByIdOrUserName({employeeId: vm.detail.employeeId}).$promise.then(function (resp) {
                    // console.log(resp);
                    vm.detail.phoneRepresentCompany = resp[0].phone;
                }, function (err) {

                });
            } else {
                vm.detail.phoneRepresentCompany = "";
            }
        }

        function openPopupDate(type) {
            if (type === "fromDate") {
                vm.openedFromDate = true;
            } else if (type === "toDate") {
                vm.openedToDate = true;
            }
        }
    }
})();