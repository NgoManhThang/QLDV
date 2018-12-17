// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('LookupController', LookupController);

    LookupController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'PartnerService', 'QldvCommonService'];

    function LookupController($scope, $rootScope, $controller, $state, $window, $element,
                               $translate, $http, $timeout, $sessionStorage, $localStorage, PartnerService, QldvCommonService) {
        var vm = this;
        scopeHolder = $scope;
        var NO_CAN_DELETE = "NO_CAN_DELETE";

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);

            //<editor-fold desc="Init variable">
            vm.listSelectedPartner = [];
            vm.lstPartner = [];

            vm.objSearch = {};
            vm.lstPartnerType = [];
            vm.lstStatus = [];
            //</editor-fold>

            //<editor-fold desc="Function">
            vm.getTable = getTable;
            vm.loadDataCombo = loadDataCombo;
            vm.doSearch = doSearch;
            vm.getListData = getListData;
            vm.doAddNew = doAddNew;

            vm.editData = editData;
            vm.deleteData = deleteData;
            $window.editData = vm.editData;
            $window.deleteData = vm.deleteData;

            vm.prevPage = prevPage;
            vm.nextPage = nextPage;
            vm.currentPageChange = currentPageChange;
            vm.pageSizeChange = pageSizeChange;
            $window.tableMainprevPage = vm.prevPage;
            $window.tableMainnextPage = vm.nextPage;
            $window.tableMainpageSizeChange = vm.pageSizeChange;
            $window.tableMaincurrentPageChange = vm.currentPageChange;

            vm.openPopupDate = openPopupDate;
            //</editor-fold>

            //<editor-fold desc="Config Table">
            vm.tableHeadDefault = [
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
                    title: $translate.instant('unions.table.unionName'),
                    value: "unionName",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.partnerName'),
                    value: "partnerName",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.vietnameeseNumber'),
                    value: "vietnameeseNumber",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.foreignerNumber'),
                    value: "foreignerNumber",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.fromDate'),
                    value: "fromDate",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.toDate'),
                    value: "toDate",
                    checked: true,
                    disable: false,
                    isShow: true
                }
            ];
            vm.tableMainConfig = {
                tableName: 'tableMain',
                pageName: 'pageUser',
                data: [],
                totalRecord: 0,
                totalPage: 0,
                currentPage: 1,
                pageSize: 5,
                sortIndex: 3,
                sortName: 'unionName',
                sortType: 'asc',
                propertyName: '',
                lastColumnWith: "10",
                headerDefault: vm.tableHeadDefault,
                hasPaging: true
            };
            //</editor-fold>

            //<editor-fold desc="Init function">
            // vm.loadDataCombo();
            // vm.doSearch();
            //</editor-fold>

        })();

        function loadDataCombo() {
            vm.lstPartnerType = [];
            vm.lstStatus = [];
            QldvCommonService.search({codeGroup: 'PARTNER_TYPE,STATUS_COMMON'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'PARTNER_TYPE') {
                        vm.lstPartnerType.push(obj);
                    } else {
                        vm.lstStatus.push(obj);
                    }
                });
            }, function (err) {

            });
        }
        
        function doSearch() {
            vm.tableMainConfig.pageSize = 5;
            vm.tableMainConfig.currentPage = 1;
            vm.getListData();
        }
        
        function getListData() {
            vm.loadingByIdTable("tableMain", "shown", vm.tableMainConfig.data);
            vm.objSearch.page = vm.tableMainConfig.currentPage;
            vm.objSearch.pageSize = vm.tableMainConfig.pageSize;
            vm.objSearch.sortName = vm.tableMainConfig.sortName;
            vm.objSearch.sortType = vm.tableMainConfig.sortType;

            PartnerService.search(vm.objSearch).$promise.then(function (resp) {
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(resp.data.draw);
                console.log(resp);
                var lstData = resp.data.data;
                if (lstData.length > 0) {
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-edit"></i></span>' +
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-remove"></i></span>';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.partnerId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100'
                            },
                            "partnerCode": {
                                value: item.partnerCode,
                                align: "left",
                                header: $translate.instant('partner.label.partnerCode'),
                                width: '150'
                            },
                            "partnerName": {
                                value: item.partnerName,
                                align: "left",
                                header: $translate.instant('partner.label.partnerName'),
                                width: '150'
                            },
                            "partnerType": {
                                value: item.partnerTypeName,
                                align: "left",
                                header: $translate.instant('partner.label.partnerType'),
                                width: '120'
                            },
                            "status": {
                                value: item.statusName,
                                align: "left",
                                header: $translate.instant('partner.label.status'),
                                width: '200'
                            },
                            "representName": {
                                value: item.representName,
                                align: "left",
                                header: $translate.instant('partner.label.representName'),
                                width: '120'
                            },
                            "phoneRepresent": {
                                value: item.phoneRepresent,
                                align: "right",
                                header: $translate.instant('partner.label.phoneRepresent'),
                                width: '120'
                            }
                        };
                        vm.tableMainConfig.data.push(objAdd);
                    }
                }
                vm.loadingByIdTable("tableMain", "hidden", vm.tableMainConfig.data);
            }, function (error) {

            });
        }

        function getTable(table) {
            for (var i = 0; i < vm.tableMainConfig.headerDefault.length; i++) {
                if (!vm.tableMainConfig.headerDefault[i].disable) {
                    if (vm.tableMainConfig.headerDefault[i].checked) {
                        table.column(i).visible(true);
                    } else {
                        table.column(i).visible(false);
                    }
                }
            }
        }
        
        function doAddNew() {
            $state.go('partner-detail');
        }

        function editData(temp) {
            var data = JSON.parse(decodeURIComponent(temp));
            $state.go('partner-detail', {partnerId: data.partnerId+""});
        }

        function deleteData(temp) {
            var data = JSON.parse(decodeURIComponent(temp));
            vm.openFormConfirm($translate.instant('global.message.confirm.deleteTitle'), $translate.instant('global.message.confirm.delete'), function () {
                PartnerService.delete({partnerId: data.partnerId}).$promise.then(function (response) {
                    var data = response.data;
                    if (data.key === "SUCCESS") {
                        vm.showAlert("success", $translate.instant('global.message.success'));
                        $('#formConfirm').modal('hide');
                        vm.doSearch();
                    } else if (data.key === NO_CAN_DELETE) {
                        vm.showAlert("warning", $translate.instant('global.message.delete.noCanDelete'));
                    } else {
                        vm.showAlert("danger", $translate.instant('global.message.error'));
                    }
                }, function (dataError) {
                    vm.showAlert("error", "Err!!!");
                });
            });
        }

        function prevPage() {
            if (vm.tableMainConfig.currentPage > 1) {
                vm.tableMainConfig.currentPage--;
                vm.getListData();
            }
        }

        function nextPage() {
            if (vm.tableMainConfig.currentPage < vm.tableMainConfig.totalPage) {
                vm.tableMainConfig.currentPage++;
                vm.getListData();
            }
        }

        function currentPageChange(el) {
            if ($(el).val() === "" || $(el).val() === "0") {
                $(el).val(1);
            }
            if (parseInt($(el).val()) > parseInt(vm.tableMainConfig.totalPage)) {
                $(el).val(parseInt(vm.tableMainConfig.totalPage));
            }
            vm.tableMainConfig.currentPage = parseInt($(el).val());
            vm.getListData();
        }

        function pageSizeChange(el) {
            vm.tableMainConfig.currentPage = 1;
            vm.tableMainConfig.pageSize = parseInt($(el).val());
            vm.getListData();
        }

        function openPopupDate(type) {
            if (type === "fromDateFrom") {
                vm.openedFromDateFrom = true;
            }
            else if (type === "toDateFrom") {
                vm.openedToDateFrom = true;
            }
            else if (type === "fromDateTo") {
                vm.openedFromDateTo = true;
            }
            else if (type === "toDateTo") {
                vm.openedToDateTo = true;
            }
        }

        $rootScope.$on('$loadSortingDatatable', function (event, object) {
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            vm.getListData();
        });

        $rootScope.$on("loadDataPartner", function () {
            vm.doSearch();
        });
    }
})();
