/**
 * Created by VTN-PTPM-NV04 on 12/6/2017.
 */
(function() {
    'use strict';
    angular.module('app').directive('itsChart', itsChart);

    itsChart.$inject = [];
    function itsChart() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                chartConfig: '='
            },
            link: function ($scope, element) {
                //console.log(element)
            },
            templateUrl: "views/tmp/itsChart.html"
        }
    }
})();