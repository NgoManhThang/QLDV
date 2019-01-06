// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('ManaEmployeeController', ManaEmployeeController);

    ManaEmployeeController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'ManageEmployeeService'];

    function ManaEmployeeController($scope, $rootScope, $controller, $state, $window, $element,
                                    $translate, $http, $timeout, $sessionStorage, $localStorage, ManageEmployeeService) {
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
            vm.objSearch = {};
            //</editor-fold>

            //<editor-fold desc="Function">
            vm.getTable = getTable;
            vm.doSearch = doSearch;
            vm.getListEmployee = getListEmployee;
            vm.doRefresh = doRefresh;
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
                    isShow: $localStorage.user.position === '0'
                },
                {
                    title: $translate.instant('user.labelInput.imageUser'),
                    value: "tblImage",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('user.labelInput.userName'),
                    value: "userName",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: $translate.instant('user.labelInput.fullName'),
                    value: "fullName",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('user.labelInput.codeEmployee'),
                    value: "codeEmployee",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('user.labelInput.email'),
                    value: "email",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: $translate.instant('user.labelInput.phone'),
                    value: "phone",
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
                if (lstData.length > 0) {
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];
                        var action = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + item.employeeId + '\')"><i class="fa fa-edit"></i></span>' +
                            '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + encodeURIComponent(JSON.stringify(item)) + '\')"><i class="fa fa-remove"></i></span>';

                        var image = '<img class="img-circle" src="'+ vm.getUrlImageByFileId(item.fileId) +'" style="width: 50px; height: 50px;" />';

                        var objAdd = {
                            "action": {
                                value: action,
                                id: item.employeeId,
                                align: "center",
                                header: $translate.instant('global.table.action'),
                                width: '100',
                                isShow: $localStorage.user.position === '0'
                            },
                            "imageUser":{
                                value: image,
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

        function editData(id) {
            $state.go('employee-detail', {employeeId: id});
            console.log(id);
        }

        function deleteData(dataTem) {
            var data = JSON.parse(decodeURIComponent(dataTem));
            vm.openFormConfirm($translate.instant('global.message.confirm.deleteTitle'), $translate.instant('global.message.confirm.delete'), function () {
                ManageEmployeeService.delete({employeeId: data.employeeId}).$promise.then(function (response) {
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

        function prevPage() {
            if (vm.tableMainConfig.currentPage > 1) {
                vm.tableMainConfig.currentPage--;
                vm.getListEmployee();
            }
        }

        function nextPage() {
            if (vm.tableMainConfig.currentPage < vm.tableMainConfig.totalPage) {
                vm.tableMainConfig.currentPage++;
                vm.getListEmployee();
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
            vm.getListEmployee();
        }

        function pageSizeChange(el) {
            vm.tableMainConfig.currentPage = 1;
            vm.tableMainConfig.pageSize = parseInt($(el).val());
            vm.getListEmployee();
        }

        $rootScope.$on('$loadSortingDatatable', function (event, object) {
            vm.tableMainConfig.sortName = object.sortName;
            vm.tableMainConfig.sortType = object.sortType;
            vm.tableMainConfig.sortIndex = object.sortIndex;
            if (object.pageName == 'pageUser') {
                getListDataEmployee();
            }
        });

        $rootScope.$on("loadDataEmployee", function () {
            vm.doSearch();
        });


    }
})();
