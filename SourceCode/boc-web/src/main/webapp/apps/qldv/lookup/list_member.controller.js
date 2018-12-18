// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function () {
    'use strict';
    angular.module('app').controller('ListMemberController', ListMemberController);

    ListMemberController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$uibModalInstance', '$window',
        '$translate', '$http', '$timeout', '$sessionStorage', '$stateParams'];

    function ListMemberController($scope, $rootScope, $controller, $state, $uibModalInstance, $window,
                                  $translate, $http, $timeout, $sessionStorage, $stateParams) {
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
            console.log($stateParams.unionId);

            //endregion

            //region Function
            vm.getTable = getTable;
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
            //endregion
        })();

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
                vm.loadMember();
            }
        });
    }
})();