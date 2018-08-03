(function () {
    'use strict';

    angular.module('app').directive('egDownload', egDownload);

    egDownload.$inject = ['$timeout'];

    function egDownload() {

        var directive = {
            link: link,
            restrict: 'A',
            scope: {
                download: '&egDownload'

            }
        };
        return directive;

        function link(scope, element) {
            var parentForm = element[0].form;
            if (parentForm) {
                element.on('click', function () {
                    return scope.download();
                });
            }
        }
    }
})();