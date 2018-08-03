(function () {
    'use strict';

    angular.module('app').directive('egUpload', egUpload);

    egUpload.$inject = ['$timeout'];

    function egUpload($timeout) {

        var directive = {
            link: link,
            restrict: 'A',
            scope: {
                upload: '&egUpload'
            }
        };
        return directive;

        function link(scope, element) {
            var parentForm = element[0].form;
            if (parentForm) {
                element.on('click', function () {
                    return scope.upload().then(function () {
                        $timeout(function () {
                            parentForm.reset();
                        });
                    });
                });
            }
        }
    }
})();