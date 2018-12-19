// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('ListMemberController', ListMemberController);

    ListMemberController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance', '$window',
        '$translate', '$http', '$timeout', '$sessionStorage', '$stateParams', 'LookupService'];

    function ListMemberController($scope, $rootScope, $controller, $state, $uibModalInstance, $window,
                                  $translate, $http, $timeout, $sessionStorage, $stateParams, LookupService) {
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
            vm.detail = {};
            //endregion

            //region Function
            vm.getTable = getTable;
            vm.doSearch = doSearch;
            vm.getListData = getListData;
            vm.close = close;

            vm.printBarcode = printBarcode;
            $window.printBarcode = vm.printBarcode;
            //endregion

            //region Config Table
            vm.tableLstMemberHeadDefault = [
                {
                    title: $translate.instant('global.index'),
                    value: "tblIndex",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: "Ảnh cá nhân",
                    value: "tblImage",
                    checked: true,
                    disable: true,
                    isShow: true,
                    width: '200'
                },
                {
                    title: "Ảnh máy tính",
                    value: "tblImage",
                    checked: true,
                    disable: false,
                    isShow: true,
                    width: '200'
                },
                {
                    title: "Mã vạch cá nhân",
                    value: "tblImage",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: "Mã vạch máy tính",
                    value: "tblImage",
                    checked: true,
                    disable: true,
                    isShow: true
                },
                {
                    title: "Họ tên",
                    value: "fullName",
                    checked: true,
                    disable: false,
                    isShow: true
                },
                {
                    title: "Chứng minh ND",
                    value: "memberId",
                    checked: true,
                    disable: true,
                    isShow: true
                }
            ];
            vm.tableLstMemberMainConfig = {
                tableName: 'tableMain',
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
                headerDefault: vm.tableLstMemberHeadDefault,
                hasPaging: true
            };
            //endregion

            //region Function init
            vm.doSearch();
            //endregion
        })();
        
        function doSearch() {
            vm.detail.unionId = $stateParams.unionId;
            vm.getListData();
        }
        
        function getListData() {
            vm.loadingByIdTable("tableMainListMember", "shown", vm.tableLstMemberMainConfig.data);
            vm.detail.page = vm.tableLstMemberMainConfig.currentPage;
            vm.detail.pageSize = vm.tableLstMemberMainConfig.pageSize;
            vm.detail.sortName = vm.tableLstMemberMainConfig.sortName;
            vm.detail.sortType = vm.tableLstMemberMainConfig.sortType;

            LookupService.searchMember(vm.detail).$promise.then(function (resp) {
                vm.tableLstMemberMainConfig.data = [];
                vm.tableLstMemberMainConfig.totalRecord = parseInt(resp.data.recordsTotal);
                vm.tableLstMemberMainConfig.totalPage = parseInt(resp.data.draw);
                console.log(resp);
                var lstData = resp.data.data;
                if (lstData != null && lstData.length > 0) {
                    for (var i = 0; i < lstData.length; i++) {
                        var item = lstData[i];

                        var imageUser = '<img class="img-thumbnail" src="'+ vm.getUrlImageByFileId(item.fileIdCMT) +'" style="width: 150px; height: 150px;" />';
                        var imageLap = '<img class="img-thumbnail" src="'+ vm.getUrlImageByFileId(item.fileIdComputer) +'" style="width: 150px; height: 150px;" />';

                        var barCodeUser = '<img src="' + vm.getImageByBarcode(item.barCodeUser) + '" />' +
                            '<br>' + item.barCodeUser + '</br>' +
                            '<button class="btn btn-warning" onclick="window.printBarcode(\'' + encodeURIComponent(item.barCodeUser) + '\')">' + 'Print' + '</button>';

                        var barCodeLap = '<img src="' + vm.getImageByBarcode(item.barCodeComputer) + '" />' +
                            '<br>' + item.barCodeComputer + '</br>' +
                            '<button class="btn btn-warning" onclick="window.printBarcode(\'' + encodeURIComponent(item.barCodeComputer) + '\')">' + 'Print' + '</button>';
                        var objAdd = {
                            "imageUser": {
                                value: imageUser,
                                id: item.unionMemberId,
                                align: "center",
                                header: "Ảnh cá nhân",
                                width: '200'
                            },
                            "imageLap": {
                                value: imageLap,
                                align: "center",
                                header: "Ảnh máy tính",
                                width: '200'
                            },
                            "barCodeUser": {
                                value: barCodeUser,
                                align: "center",
                                header: "Mã vạch cá nhân",
                                width: '150'
                            },
                            "barCodeLap": {
                                value: barCodeLap,
                                align: "center",
                                header: "Mã vạch máy tính",
                                width: '120'
                            },
                            "fullName": {
                                value: item.fullName,
                                align: "left",
                                header: "Họ tên",
                                width: '200'
                            },
                            "memberId": {
                                value: item.memberId,
                                align: "left",
                                header: "Chứng minh nhân dân",
                                width: '120'
                            }
                        };
                        vm.tableLstMemberMainConfig.data.push(objAdd);
                    }
                }
                vm.loadingByIdTable("tableMainListMember", "hidden", vm.tableLstMemberMainConfig.data);
            }, function (error) {

            });
        }

        function printBarcode(barCode) {
            LookupService.getFilePathPdfPrinter({barCode : barCode}).$promise.then(function (response) {
                printJS(URL.createObjectURL(response.response));
            });
        }

        function close() {
            $stateParams.unionId = null;
            $uibModalInstance.close();
            $state.go('boc.lookup');
        }

        function getTable(table) {
            for (var i = 0; i < vm.tableLstMemberMainConfig.headerDefault.length; i++) {
                if (!vm.tableLstMemberMainConfig.headerDefault[i].disable) {
                    if (vm.tableLstMemberMainConfig.headerDefault[i].checked) {
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
            }
        });
    }
})();