/**
 * Created by VTN-PTPM-NV04 on 12/6/2017.
 */
(function() {
    'use strict';
    angular.module('app').directive('itsToolbar', itsToolbar);

    itsToolbar.$inject = [];
    function itsToolbar() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                toolConfig: '='
            },
            link: function ($scope, element) {
                // Su kien cho nut expand
                $scope.expandAction = function (component, type) {
                    $scope.$parent.vm.expand(component, type);
                }
                // Su kien cho nut previous
                $scope.previousAction = function (component) {
                    $scope.$parent.vm.previous(component);
                }
                // Su kien cho nut draw
                $scope.drawAction = function (component) {
                    $scope.$parent.vm.draw(component, $(element).find("input[id='inputDraw']").val());
                }
                // Su kien cho nut export
                $scope.exportAction = function (component) {
                    $scope.$parent.vm.export(component);
                }
                $scope.exportOneAction = function (component) {
                    $scope.$parent.vm.exportOne(component);
                }
                $scope.exportTwoAction = function (component) {
                    $scope.$parent.vm.exportTwo(component);
                }
                // Su kien change cho combo one
                $scope.comboOneChange = function (component) {
                    $scope.$parent.vm.comboOneChange(component, $(element).find("select[id='conboOne']").val());
                }
                // Su kien cho checkbox
                $scope.checkBoxChange = function (component) {
                    $scope.$parent.vm.checkBoxChange(component, $(element).find("input[id='toolCheckBox']").is(":checked"));
                }
            },
            templateUrl: "views/tmp/itsToolbar.html"
        }
    }
})();