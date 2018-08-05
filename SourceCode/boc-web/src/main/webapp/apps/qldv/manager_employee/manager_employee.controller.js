// code style: https://github.com/johnpapa/angular-styleguide
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('ManaEmployeeController', ManaEmployeeController);

    ManaEmployeeController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element',
        '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'ManaEmployeeService'];
    function ManaEmployeeController($scope, $rootScope, $controller, $state, $window, $element,
                                    $translate, $http, $timeout, $sessionStorage, $localStorage, ManaEmployeeService){
        var vm = this;
        scopeHolder = $scope;

        // Init controller
        (function initController() {

            // instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);

            vm.getTable = getTable;

            vm.tableHeadDefault = [
                {title: $translate.instant('global.index'), value: "tblIndex", checked: true, disable: true, isShow: true},
                //{title: "", value: "tblCheckbox", checked: true, disable: true, isShow: false},
                {title: $translate.instant('global.table.action'), value: "tblAction", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.imageUser'), value: "tblImage", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.userName'), value: "userName", checked: true, disable: true, isShow: true},
                {title: $translate.instant('user.labelInput.fullName'), value: "fullName", checked: true, disable: false, isShow: true},
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
        })();



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
