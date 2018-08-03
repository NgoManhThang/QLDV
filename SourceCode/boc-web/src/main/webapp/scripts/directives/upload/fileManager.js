(function () {
    'use strict';

    angular.module('app').factory('fileManager', fileManager);

    fileManager.$inject = ['$q', 'fileManagerClient', 'appInfo', '$http', '$rootScope', 'APP_REST_SERVICE'];

    function fileManager($q, fileManagerClient, appInfo, $http, $rootScope, APP_REST_SERVICE) {
        var service = {
            photos: [],
            load: load,
            upload: upload,
            download: download,
            downloadErrorFile: downloadErrorFile,
            remove: remove,
            photoExists: photoExists,
            status: {
                uploading: false,
                success: false,
                validationFail: false,
                fileNameError: ''
            },
            apiUrl: '',
            downloadFileTemp: downloadFileTemp
        };
        return service;

        function load() {
            appInfo.setInfo({busy: true, message: "loading photos"})

            service.photos.length = 0;
            return fileManagerClient.query()
                .$promise
                .then(function (result) {
                        return result.$promise;
                    },
                    function (result) {
                        return $q.reject(result);
                    })
                ['finally'](
                function () {

                });
        }

        function upload(photos, module) {
            service.status.success = false;
            service.status.uploading = true;
            service.status.validationFail = false;
            appInfo.setInfo({busy: true, message: "uploading file"});

            var formData = new FormData();
            //formData.append('MODULE', module);
            angular.forEach(photos, function (photo) {
                formData.append("file", photo);
            });

            return fileManagerClient.save(formData)
                .$promise
                .then(function (result) {
                        var responseCode = result.responseCode;
                        if (responseCode === 0) {
                            service.status.validationFail = false;
                            service.status.success = true;
                            $rootScope.showNotification(result.responseMessage, responseCode);
                        }
                        else if (responseCode === 1) {
                            service.status.validationFail = true;
                            service.status.fileNameError = result.data;
                        } else if (responseCode === 2) {
                            $rootScope.showNotification(result.responseMessage, responseCode);
                        } else {
                            $rootScope.showNotification(result.responseMessage, responseCode);
                        }
                        return result.$promise;
                    },
                    function (result) {
                        return $q.reject(result);
                    })
                ['finally'](
                function () {
                    service.status.uploading = false;
                });
        }

        function download(moduleParameter) {
            var serviceBase = "";
            $http({
                method: 'GET',
                url: APP_REST_SERVICE + 'import_money_punish/download_template_file',
                // params: {module: moduleParameter},
                responseType: 'arraybuffer'
            }).success(function (data, status, headers) {
                headers = headers();
                var filename = headers['x-filename'];
                var contentType = headers['content-type'];
                var linkElement = document.createElement('a');
                try {
                    var blob = new Blob([data], {type: contentType});
                    if (navigator.appVersion.toString().indexOf('.NET') > 0) {
                        window.navigator.msSaveBlob(blob, filename);
                    } else {
                        var url = window.URL.createObjectURL(blob);
                        linkElement.setAttribute('href', url);
                        linkElement.setAttribute("download", filename);
                        var clickEvent = new MouseEvent("click", {
                            "view": window,
                            "bubbles": true,
                            "cancelable": false
                        });
                        linkElement.dispatchEvent(clickEvent);
                    }
                } catch (ex) {
                }
            }).error(function (data) {
            });
        }

        function downloadErrorFile(fileNameError) {
            var serviceBase = "";
            $http({
                method: 'GET',
                url: serviceBase + 'api/imports/getFileError',
                params: {fileName: fileNameError},
                responseType: 'arraybuffer'
            }).success(function (data, status, headers) {
                headers = headers();
                var filename = headers['x-filename'];
                var contentType = headers['content-type'];
                var linkElement = document.createElement('a');
                try {
                    var blob = new Blob([data], {type: contentType});
                    if (navigator.appVersion.toString().indexOf('.NET') > 0) {
                        window.navigator.msSaveBlob(blob, filename);
                    } else {
                        var url = window.URL.createObjectURL(blob);
                        linkElement.setAttribute('href', url);
                        linkElement.setAttribute("download", filename);
                        var clickEvent = new MouseEvent("click", {
                            "view": window,
                            "bubbles": true,
                            "cancelable": false
                        });
                        linkElement.dispatchEvent(clickEvent);
                    }
                } catch (ex) {
                }
            }).error(function (data) {
            });
        }

        function remove(photo) {
            appInfo.setInfo({busy: true, message: "deleting photo " + photo.name});

            return fileManagerClient.remove({fileName: photo.name})
                .$promise
                .then(function (result) {
                        var i = service.photos.indexOf(photo);
                        service.photos.splice(i, 1);

                        appInfo.setInfo({message: "photos deleted"});

                        return result.$promise;
                    },
                    function (result) {
                        appInfo.setInfo({message: "something went wrong: " + result.data.message});
                        return $q.reject(result);
                    })
                ['finally'](
                function () {
                    appInfo.setInfo({busy: false});
                });
        }

        function photoExists(photoName) {
            var res = false
            service.photos.forEach(function (photo) {
                if (photo.name === photoName) {
                    res = true;
                }
            });

            return res;
        }

        function downloadFileTemp() {
            var anchor = angular.element('<a/>');
            anchor.attr({
                href: APP_REST_SERVICE + 'import_money_punish/download-test',
                target: '_self',
                download: "temp.pdf"
            });

            angular.element(document.body).append(anchor);
            anchor [0].click();
        }
    }
})();