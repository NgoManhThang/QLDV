// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('PartnerController', PartnerController);

    PartnerController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'PartnerService', 'QldvCommonService'];

    function PartnerController($scope, $rootScope, $controller, $state, $window, $element,
                               $translate, $http, $timeout, $sessionStorage, $localStorage, PartnerService, QldvCommonService) {
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
                pageSize: 10,
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
            vm.loadDataCombo();
            vm.doSearch();
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
            vm.tableMainConfig.pageSize = 10;
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
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + item.employeeId + '\')"><i class="fa fa-edit"></i></span>' +
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

        $rootScope.$on('$loadSortingDatatable', function (event, object) {
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            vm.getListData();
        });

        $rootScope.$on("loadDataEmployee", function () {
            vm.doSearch();
        });


    }
})();
