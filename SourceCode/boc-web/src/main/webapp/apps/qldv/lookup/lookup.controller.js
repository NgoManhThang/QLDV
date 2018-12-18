// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('LookupController', LookupController);

    LookupController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'PartnerService', 'QldvCommonService', 'LookupService'];

    function LookupController($scope, $rootScope, $controller, $state, $window, $element,
                              $translate, $http, $timeout, $sessionStorage, $localStorage, PartnerService, QldvCommonService, LookupService) {
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
            // New used
            vm.listSelectedPartner = [];
            vm.lstPartner = [];
            vm.loadCombo = {};
            vm.objSearch = {
                lstPartnerId: [],
                lstUnionType: [],
                lstUnionStatus: []
            };

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


            vm.viewListMember = viewListMember;
            $window.viewListMember = vm.viewListMember;

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
            vm.loadDataCombo();
            vm.doSearch();
            //</editor-fold>

        })();

        function loadDataCombo() {
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

            LookupService.search(vm.objSearch).$promise.then(function (resp) {
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(resp.data.draw);
                console.log(resp);
                var lstData = resp.data.data;
                if (lstData != null && lstData.length > 0) {
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];
                        var action = '<span title="Xem chi tiết" class="btn-icon-table" onclick="window.viewListMember(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-eye"></i></span>';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.unionId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100'
                            },
                            "unionName": {
                                value: item.unionName,
                                align: "left",
                                header: $translate.instant('unions.table.unionName'),
                                width: '150'
                            },
                            "partnerName": {
                                value: item.partnerName,
                                align: "left",
                                header: $translate.instant('unions.table.partnerName'),
                                width: '150'
                            },
                            "vietnameeseNumber": {
                                value: item.vietnameseNumber,
                                align: "left",
                                header: $translate.instant('unions.table.vietnameeseNumber'),
                                width: '120'
                            },
                            "foreignerNumber": {
                                value: item.foreignerNumber,
                                align: "left",
                                header: $translate.instant('unions.table.foreignerNumber'),
                                width: '200'
                            },
                            "fromDate": {
                                value: item.fromDate,
                                align: "left",
                                header: $translate.instant('unions.table.fromDate'),
                                width: '120'
                            },
                            "toDate": {
                                value: item.toDate,
                                align: "right",
                                header: $translate.instant('unions.table.toDate'),
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
        
        function setParamSearch() {
            vm.objSearch.lstPartnerId = [];

            vm.objSearch.fromDateFrom = vm.filterDate(vm.objDate.fromDateFrom, 'yyyyMMdd');
            vm.objSearch.fromDateTo = vm.filterDate(vm.objDate.fromDateTo, 'yyyyMMdd');
            vm.objSearch.toDateFrom = vm.filterDate(vm.objDate.toDateFrom, 'yyyyMMdd');
            vm.objSearch.toDateTo = vm.filterDate(vm.objDate.toDateTo, 'yyyyMMdd');

            if(vm.listSelectedPartner.length > 0){
                $.each(vm.listSelectedPartner, function (i, obj) {
                    vm.objSearch.lstPartnerId.push(obj.partnerId);
                });
            }
        }

        function viewListMember(dataTemp) {
            var data = JSON.parse(decodeURIComponent(dataTemp));
            console.log(data);
            $state.go('lst-member', {unionId: data.unionId});

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
