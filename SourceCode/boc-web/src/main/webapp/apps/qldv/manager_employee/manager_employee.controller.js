// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('ManaEmployeeController', ManaEmployeeController);

    ManaEmployeeController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'ManageEmployeeService'];
    function ManaEmployeeController($scope, $rootScope, $controller, $state, $window, $element,
                                    $translate, $http, $timeout, $sessionStorage, $localStorage, ManageEmployeeService){
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
            //</editor-fold>

            //<editor-fold desc="Function">
            vm.getTable = getTable;
            vm.doSearch = doSearch;
            vm.getListEmployee = getListEmployee;
            vm.doRefresh = doRefresh;
            vm.doAddNew = doAddNew;
            //</editor-fold>

            //<editor-fold desc="Config Table">
            vm.tableHeadDefault = [
                {title: $translate.instant('global.index'), value: "tblIndex", checked: true, disable: true, isShow: true},
                {title: $translate.instant('global.table.action'), value: "tblAction", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.imageUser'), value: "tblImage", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.userName'), value: "userName", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.fullName'), value: "fullName", checked: true, disable: false, isShow: true},
                {title: $translate.instant('user.labelInput.codeEmployee'), value: "codeEmployee", checked: true, disable: false, isShow: true},
                {title: $translate.instant('user.labelInput.email'), value: "email", checked: true, disable: false, isShow: true},
                {title: $translate.instant('user.labelInput.phone'), value: "phone", checked: true, disable: false, isShow: true}
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
                sortName: 'userName',
                sortType: 'asc',
                propertyName: '',
                lastColumnWith: "10",
                headerDefault: vm.tableHeadDefault,
                hasPaging: true
            };
            //</editor-fold>

            vm.doSearch();
        })();


        function doSearch() {
            vm.tableMainConfig.currentPage = 1;
            vm.tableMainConfig.pageSize = 10;
            vm.getListEmployee();
        }

        function getListEmployee() {
            vm.loadingByIdTable("tableMain", "shown", vm.tableMainConfig.data);
            vm.objSearch.page = vm.tableMainConfig.currentPage;
            vm.objSearch.pageSize = vm.tableMainConfig.pageSize;
            vm.objSearch.sortName = vm.tableMainConfig.sortName;
            vm.objSearch.sortType = vm.tableMainConfig.sortType;

            ManageEmployeeService.search(vm.objSearch).$promise.then(function (resp) {
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(resp.data.draw);
                var lstData = resp.data.data;
                if(lstData.length > 0){
                    for (var i = 0; i < lstData.length; i++){
                        var item = lstData[i];
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + item.userId + '\')"><i class="fa fa-edit"></i></span>' +
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + item.userId + '\')"><i class="fa fa-remove"></i></span>';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.employeeId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100'
                            },
                            "imageUser": {
                                value: "",
                                align: "center",
                                header: $translate.instant('user.labelInput.imageUser'),
                                width: '80'
                            },
                            "userName": {
                                value: item.userName,
                                align: "left",
                                header: $translate.instant('user.labelInput.userName'),
                                width: '150'
                            },
                            "fullName": {
                                value: item.fullName,
                                align: "left",
                                header: $translate.instant('user.labelInput.fullName'),
                                width: '150'
                            },
                            "codeEmployee": {
                                value: item.codeEmployee,
                                align: "left",
                                header: $translate.instant('user.labelInput.codeEmployee'),
                                width: '120'
                            },
                            "email": {
                                value: nullToStringEmpty(item.email),
                                align: "left",
                                header: $translate.instant('user.labelInput.email'),
                                width: '200'
                            },
                            "phone": {
                                value: nullToStringEmpty(item.phone),
                                align: "right",
                                header: $translate.instant('user.labelInput.phone'),
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

        function doRefresh() {
            vm.objSearch.userName = "";
            vm.objSearch.fullName = "";
            vm.getListEmployee();
        }

        function doAddNew() {
            $state.go('employee-detail');
        }

        function getTable(table) {
            for (var i = 0; i < vm.tableMainConfig.headerDefault.length; i++) {
                if(!vm.tableMainConfig.headerDefault[i].disable){
                    if(vm.tableMainConfig.headerDefault[i].checked){
                        table.column(i).visible(true);
                    } else {
                        table.column(i).visible(false);
                    }
                }
            }
        }

        $rootScope.$on('$loadSortingDatatable', function(event, object){
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            if(object.pageName == 'pageUser') {
                getListDataEmployee();
            }
        });


    }
})();
