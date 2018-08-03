/**
 * Created by VTN-PTPM-NV04 on 12/6/2017.
 */
(function() {
    'use strict';
    angular.module('app').directive('markRequired', markRequired);

    markRequired.$inject = [];
    function markRequired() {
        return {
            restrict: 'A', //only want it triggered for attributes
            compile: function(element) {
                //could add a check to make sure it's an input element if need be
                element.append("<span class='something' style='color: red'>&nbsp;*&nbsp;</span>");
            }
        }
    }
})();