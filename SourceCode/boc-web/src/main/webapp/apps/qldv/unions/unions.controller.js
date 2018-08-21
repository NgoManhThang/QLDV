// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('UnionsController', UnionsController);

    UnionsController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'UnionsService', 'QldvCommonService', 'PartnerService'];

    function UnionsController($scope, $rootScope, $controller, $state, $window, $element,
                              $translate, $http, $timeout, $sessionStorage, $localStorage, UnionsService, QldvCommonService, PartnerService) {
        var vm = this;
        scopeHolder = $scope;

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);

            //<editor-fold desc="Init variable">
            vm.objSearch = {
                lstPartnerId: [],
                lstUnionType: [],
                lstUnionStatus: []
            };
            vm.loadCombo = {};
            vm.listSelectedPartner = [];
            vm.lstPartner = [];
            vm.listSelectedUnionType = [];
            vm.lstUnionType = [];
            vm.listSelectedStatus = [];
            vm.lstStatus = [];
            vm.objDate = {
                fromDateFrom: null,
                fromDateTo: null,
                toDateFrom: null,
                toDateTo: null
            };
            //</editor-fold>

            //<editor-fold desc="Function">
            vm.getTable = getTable;
            vm.loadDataCombo = loadDataCombo;
            vm.doSearch = doSearch;
            vm.getListData = getListData;
            vm.setParamSearch = setParamSearch;
            vm.doAddNew = doAddNew;

            vm.editData = editData;
            $window.editData = vm.editData;

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
                    title: $translate.instant('unions.table.partnerName'),
                    value: "partnerName",
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
                },
                {
                    title: $translate.instant('unions.table.representUnion'),
                    value: "representUnion",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.representCompany'),
                    value: "representCompany",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('unions.table.status'),
                    value: "status",
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
            vm.loadDataCombo();
            vm.doSearch();
            //</editor-fold>

        })();

        function loadDataCombo() {
            vm.lstUnionType = [];
            vm.lstUnionTypeTemp = [];
            vm.lstStatus = [];
            vm.lstStatusTemp = [];
            QldvCommonService.search({codeGroup: 'STATUS_UNIONS,UNIONS_TYPE'}).$promise.then(function (resp) {
                $.each(resp, function (i, obj) {
                    if (obj.codeGroup === 'STATUS_UNIONS') {
                        vm.lstStatusTemp.push(obj);
                    } else {
                        vm.lstUnionTypeTemp.push(obj);
                    }
                });
                vm.lstUnionType = vm.lstUnionTypeTemp;
                vm.lstStatus = vm.lstStatusTemp;
            }, function (err) {

            });

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
                vm.lstPartner = lstData;
            }, function (err) {

            })
        }

        function doSearch() {
            vm.setParamSearch();
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

            UnionsService.search(vm.objSearch).$promise.then(function (resp) {
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(resp.data.draw);
                var lstData = resp.data.data;
                if (lstData.length > 0) {
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-edit"></i></span>' +
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + encodeURIComponent(item) + '\')"><i class="fa fa-remove"></i></span>';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.unionId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100'
                            },
                            "partnerName": {
                                value: item.partnerName,
                                align: "left",
                                header: $translate.instant('unions.table.partnerName'),
                                width: '150'
                            },
                            "unionName": {
                                value: item.unionName,
                                align: "left",
                                header: $translate.instant('unions.table.unionName'),
                                width: '150'
                            },
                            "vietnameseNumber": {
                                value: item.vietnameseNumber,
                                align: "right",
                                header: $translate.instant('unions.table.vietnameeseNumber'),
                                width: '150'
                            },
                            "foreignerNumber": {
                                value: item.foreignerNumber,
                                align: "right",
                                header: $translate.instant('unions.table.foreignerNumber'),
                                width: '170'
                            },
                            "fromDate": {
                                value: item.fromDate,
                                align: "center",
                                header: $translate.instant('unions.table.fromDate'),
                                width: '120'
                            },
                            "toDate": {
                                value: item.toDate,
                                align: "center",
                                header: $translate.instant('unions.table.toDate'),
                                width: '120'
                            },
                            "representUnion": {
                                value: item.representUnion,
                                align: "left",
                                header: $translate.instant('unions.table.representUnion'),
                                width: '200'
                            },
                            "representCompany": {
                                value: item.representCompany,
                                align: "left",
                                header: $translate.instant('unions.table.representCompany'),
                                width: '200'
                            },
                            "status": {
                                value: item.status,
                                align: "left",
                                header: $translate.instant('unions.table.status'),
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

        function setParamSearch() {
            vm.objSearch.lstPartnerId = [];
            vm.objSearch.lstUnionType = [];
            vm.objSearch.lstUnionStatus = [];

            vm.objSearch.fromDateFrom = vm.filterDate(vm.objDate.fromDateFrom, 'yyyyMMdd');
            vm.objSearch.fromDateTo = vm.filterDate(vm.objDate.fromDateTo, 'yyyyMMdd');
            vm.objSearch.toDateFrom = vm.filterDate(vm.objDate.toDateFrom, 'yyyyMMdd');
            vm.objSearch.toDateTo = vm.filterDate(vm.objDate.toDateTo, 'yyyyMMdd');

            if(vm.listSelectedPartner.length > 0){
                $.each(vm.listSelectedPartner, function (i, obj) {
                    vm.objSearch.lstPartnerId.push(obj.partnerId);
                });
            }

            if(vm.listSelectedUnionType.length > 0){
                $.each(vm.listSelectedUnionType, function (i, obj) {
                    vm.objSearch.lstUnionType.push(obj.code);
                });
            }

            if(vm.listSelectedStatus.length > 0){
                $.each(vm.listSelectedStatus, function (i, obj) {
                    vm.objSearch.lstUnionStatus.push(obj.code);
                });
            }
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
            $state.go('partner-detail', {partnerId: data.partnerId + ""});
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
