// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('UnionsController', UnionsController);

    UnionsController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'UnionsService', 'QldvCommonService'];

    function UnionsController($scope, $rootScope, $controller, $state, $window, $element,
                               $translate, $http, $timeout, $sessionStorage, $localStorage, UnionsService, QldvCommonService) {
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
            $window.editData = vm.editData;

            vm.prevPage = prevPage;
            vm.nextPage = nextPage;
            vm.currentPageChange = currentPageChange;
            vm.pageSizeChange = pageSizeChange;
            $window.tableMainprevPage = vm.prevPage;
            $window.tableMainnextPage = vm.nextPage;
            $window.tableMainpageSizeChange = vm.pageSizeChange;
            $window.tableMaincurrentPageChange = vm.currentPageChange;
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
                    title: $translate.instant('partner.label.partnerCode'),
                    value: "partnerCode",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('partner.label.partnerName'),
                    value: "partnerName",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('partner.label.partnerType'),
                    value: "partnerType",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('partner.label.status'),
                    value: "status",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('partner.label.representName'),
                    value: "representName",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('partner.label.phoneRepresent'),
                    value: "phoneRepresent",
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
                sortName: 'partnerCode',
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
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + encodeURIComponent(item) + '\')"><i class="fa fa-remove"></i></span>';

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
