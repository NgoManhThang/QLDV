/**
 * Created by VTN-PTPM-NV04 on 12/6/2017.
 */
(function() {
    'use strict';
    angular.module('app').directive('itsTableConfig', itsTableConfig);

    itsTableConfig.$inject = [];
    function itsTableConfig() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                tableConfig: '='
            },
            link: function ($scope, element) {
                $scope.checkAll = function () {
                    for (var i = 0; i < $scope.tableConfig.headerDefault.length; i++){
                        var item = $scope.tableConfig.headerDefault[i];
                        if (!item.disable){
                            item.checked = true;
                        }
                    }
                };

                $scope.unCheckAll = function () {
                    for (var i = 0; i < $scope.tableConfig.headerDefault.length; i++){
                        var item = $scope.tableConfig.headerDefault[i];
                        if (!item.disable){
                            item.checked = false;
                        }
                    }
                };

                $scope.selectItem = function (item) {
                    if (!item.disable){
                        for (var i = 0; i < $scope.tableConfig.headerDefault.length; i++){
                            var _item = $scope.tableConfig.headerDefault[i];
                            if (_item.value === item.value){
                                _item.checked = !_item.checked;
                                break;
                            }
                        }
                    }
                };

                $(document).on('click', '.its-table-config .dropdown-menu', function (e) {
                    e.stopPropagation();
                });
            },
            templateUrl: "views/tmp/table-config.html"
        }
    }
})();