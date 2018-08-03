/**
 * Created by VTN-PTPM-NV30 on 12/18/2017.
 */
/**
 * Created by DongNV on 12/6/2017.
 */

(function () {
    'use strict';
    angular.module('app').directive('fileModel', fileModel);

    fileModel.$inject = ['$parse'];

    function fileModel($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
                
                /*var parentForm = element[0].form;
                if (parentForm) {
                    element.on('click', function () {
                    	parentForm.reset();
                    });
                }*/
            }
        };
    }

})();