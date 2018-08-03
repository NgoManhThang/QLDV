(function () {
    'use strict';

    angular.module('app').directive('egError', egError);

    egError.$inject = ['$timeout'];

    function egError() {

        var directive = {
            link: link,
            restrict: 'A',
            scope: {
                downloadErrorFile: '&egError'

            }
        };
        return directive;

        function link(scope, element) {
            var parentForm = element[0].form;
            if (parentForm) {
                element.on('click', function () {
                    return scope.downloadErrorFile();
                });
            }
        }
    }
})();