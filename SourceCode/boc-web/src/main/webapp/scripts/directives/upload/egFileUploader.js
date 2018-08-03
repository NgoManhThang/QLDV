(function () {
    'use strict';

    angular.module('app').directive('egFileUploader', egFileUploader);

    egFileUploader.$inject = ['appInfo', 'fileManager'];

    function egFileUploader(appInfo, fileManager) {

        var directive = {
            link: link,
            restrict: 'E',
            templateUrl: 'scripts/directives/upload/egFileUploader.html',
        };
        return directive;

        function link(scope, element, attrs) {
            scope.module = attrs['module'];
            scope.cancelroute = attrs['cancelroute'];
            scope.hasFiles = false;
            scope.photos = [];
            scope.upload = fileManager.upload;
            scope.download = fileManager.download;
            scope.downloadErrorFile = fileManager.downloadErrorFile;
            scope.appStatus = appInfo.status;
            scope.fileManagerStatus = fileManager.status;
            scope.downloadFileTemp = fileManager.downloadFileTemp;


            fileManager.status.validationFail = false;
            fileManager.status.fileNameError = '';
            fileManager.status.uploading = false;
        }
    }

})();