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
                vm.showAddMember = true;
                vm.title = $translate.instant('unions.title.update');
            } else {
                vm.showAddMember = false;
                vm.title = $translate.instant('unions.title.addNew');
            }
            vm.showTableMember = false;
            //Member
            vm.lstMemberType = [];
            vm.lstNationalId = [];
            vm.member = {};
            vm.titleMember = "";
            vm.showMember = false;
            vm.searchMember = {};

            vm.urlCMT = null;
            vm.fileCMT = null;

            vm.urlComputer = null;
            vm.fileComputer = null;
            //endregion

            //region Function
            vm.getTable = getTable;
            vm.loadDataCombo = loadDataCombo;
            vm.doExit = doExit;
            vm.doSaveData = doSaveData;
            vm.getDetail = getDetail;
            vm.openPopupDate = openPopupDate;
            vm.changeEmployee = changeEmployee;
            vm.doAddMember = doAddMember;
            vm.loadMember = loadMember;
            //endregion

            //region Config Table
            vm.tableMemberHeadDefault = [
                {
                    title: $translate.instant('global.index'),
                    value: "tblIndex",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('global.table.action'),
                    value: "tblAction",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.fullName'),
                    value: "fullName",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.nationalId'),
                    value: "nationalId",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.memberId'),
                    value: "memberId",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                /*{
                    title: $translate.instant('unions.member.fileCMT'),
                    value: "tblImage",
                    checked: true,
                    disable: true,
                    isShow: true
                },*/
                {
                    title: $translate.instant('unions.member.laptopId'),
                    value: "laptopId",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.fileComputer'),
                    value: "tblImage",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.apprStatus'),
                    value: "apprStatus",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.member.reasonNotApp'),
                    value: "reasonNotApp",
                    checked: true,
                    disable: false,
                    isShow: true
                }
            ];
            vm.tableMemberMainConfig = {
                tableName: 'tableMemberMain',
                pageName: 'pageMember',
                data: [],
                totalRecord: 0,
                totalPage: 0,
                currentPage: 1,
                pageSize: 5,
                sortIndex: 3,
                sortName: 'fullName',
                sortType: 'asc',
                propertyName: '',
                lastColumnWith: "10",
                headerDefault: vm.tableMemberHeadDefault,
                hasPaging: true
            };
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
                formData.append('filesCMT', vm.fileCMT);
                formData.append('filesComputer', vm.fileComputer);

                UnionsService.saveDataMember(formData).$promise.then(function (resp) {
                    var result = resp.data.key;
                    if (result === SUCCESS) {
                        vm.showMember = false;
                        vm.showTableMember = true;
                        vm.showAlert("success", $translate.instant('global.message.success'));
                        vm.loadMember();
                    } else {
                        vm.showAlert("danger", $translate.instant('global.message.error'));
                    }
                }, function (err) {

                });
            } else {
                vm.detail.fromDate = vm.filterDate(vm.objDate.fromDate, FORMAT_DATE);
                vm.detail.toDate = vm.filterDate(vm.objDate.toDate, FORMAT_DATE);

                var formData = new FormData();
                formData.append('dataString', JSON.stringify(vm.detail));

                UnionsService.saveData(formData).$promise.then(function (resp) {
                    var result = resp.data.key;
                    if (result === SUCCESS) {
                        vm.detail.unionId = resp.data.id;
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
            var srcImage = vm.getUrlImageByFileId(null);
            $("#idFileCMT").attr("src", srcImage);
            vm.urlCMT = srcImage;

            var srcImageComputer = vm.getUrlImageByFileId(null);
            $("#idFileComputer").attr("src", srcImageComputer);
            vm.urlComputer = srcImage;
            QldvCommonService.search({codeGroup: 'MEMBER_TYPE,NATIONAL'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'MEMBER_TYPE') {
                        vm.lstMemberType.push(obj);
                    } else {
                        vm.lstNationalId.push(obj);
                    }
                });
                for (var i = 0; i < vm.lstPartnerId.length; i++) {
                    if (parseInt(vm.detail.partnerId) === vm.lstPartnerId[i].partnerId) {
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
                vm.showTableMember = true;
                vm.loadMember();
            }, function (err) {

            });
        }

        function loadMember() {
            vm.loadingByIdTable("tableMemberMain", "shown", vm.tableMemberMainConfig.data);
            vm.searchMember.page = vm.tableMemberMainConfig.currentPage;
            vm.searchMember.pageSize = vm.tableMemberMainConfig.pageSize;
            vm.searchMember.sortName = vm.tableMemberMainConfig.sortName;
            vm.searchMember.sortType = vm.tableMemberMainConfig.sortType;
            vm.searchMember.unionId = vm.detail.unionId;
            UnionsService.searchMember(vm.searchMember).$promise.then(function (resp) {
                vm.tableMemberMainConfig.data = [];
                vm.tableMemberMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableMemberMainConfig.totalPage = parseInt(resp.data.draw);
                var lstData = resp.data.data;
                if (lstData.length > 0) {
                    // vm.showTableMember = true;
                    // console.log(lstData);
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-edit"></i></span>' +
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-remove"></i></span>';

                        var imageCMT = '<img class="img-circle" src="'+ vm.getUrlImageByFileId(item.fileIdCMT) +'" style="width: 50px; height: 50px;" />';
                        var imageComputer = '<img class="img-circle" src="'+ vm.getUrlImageByFileId(item.fileIdComputer) +'" style="width: 50px; height: 50px;" />';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.unionMemberId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100'
                            },
                            "fullName": {
                                value: item.fullName,
                                align: "left",
                                header: $translate.instant('unions.member.fullName'),
                                width: '150'
                            },
                            "nationalId": {
                                value: item.nationalId,
                                align: "left",
                                header: $translate.instant('unions.member.nationalId'),
                                width: '150'
                            },
                            "memberId": {
                                value: item.memberId,
                                align: "left",
                                header: $translate.instant('unions.member.memberId'),
                                width: '150'
                            },
                            "fileCMT":{
                                value: imageCMT,
                                align: "center",
                                header: $translate.instant('unions.member.fileCMT'),
                                width: '80'
                            },
                            "laptopId": {
                                value: item.laptopId,
                                align: "left",
                                header: $translate.instant('unions.member.laptopId'),
                                width: '170'
                            },
                            "fileComputer":{
                                value: imageComputer,
                                align: "center",
                                header: $translate.instant('unions.member.fileComputer'),
                                width: '80'
                            },
                            "apprStatus": {
                                value: item.apprStatus === null ? "" : item.apprStatus,
                                align: "left",
                                header: $translate.instant('unions.member.apprStatus'),
                                width: '120'
                            },
                            "reasonNotApp": {
                                value: item.reasonNotApp === null ? "" : item.reasonNotApp,
                                align: "left",
                                header: $translate.instant('unions.member.reasonNotApp'),
                                width: '120'
                            }
                        };
                        vm.tableMemberMainConfig.data.push(objAdd);
                    }
                } else {
                    vm.showTableMember = false;
                }
                vm.loadingByIdTable("tableMemberMain", "hidden", vm.tableMemberMainConfig.data);
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

        function getTable(table) {
            for (var i = 0; i < vm.tableMemberMainConfig.headerDefault.length; i++) {
                if (!vm.tableMemberMainConfig.headerDefault[i].disable) {
                    if (vm.tableMemberMainConfig.headerDefault[i].checked) {
                        table.column(i).visible(true);
                    } else {
                        table.column(i).visible(false);
                    }
                }
            }
        }

        $rootScope.$on('$loadSortingDatatable', function (event, object) {
            if (object.pageName === "pageMember") {
                vm.tableMemberMainConfig.sortName = object.sortName;
                vm.tableMemberMainConfig.sortType = object.sortType;
                vm.tableMemberMainConfig.sortIndex = object.sortIndex;
                vm.loadMember();
            }
        });
    }
})();