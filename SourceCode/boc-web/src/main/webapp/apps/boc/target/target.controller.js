// code style: https://github.com/johnpapa/angular-styleguide 
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('TargetController', TargetController);
    
    TargetController.$inject = ['$scope', '$rootScope', '$controller', '$window', '$state', '$element', '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'TargetService'];
    function TargetController($scope, $rootScope, $controller, $window, $state, $element, $translate, $http, $timeout, $sessionStorage, $localStorage, TargetService){
        var vm = this;
        scopeHolder = $scope;
        
        var SUCCESS = "SUCCESS";
        var ERROR = "ERROR";
        var FILE_IS_NULL = "FILE_IS_NULL";
        var DATA_OVER = "DATA_OVER";
        var NODATA = "NODATA";
        var FILE_TYPE_INVALID = "FILE_TYPE_INVALID";
        var FILE_INVALID = "FILE_INVALID";
        
    	// Init controller
    	(function initController() {
    		
    		// instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);
            
            vm.dateFormat = "MM/yyyy";
            vm.dateOptions = {
                minMode: 'month',
                maxMode: 'year',
                datepickerMode: 'month'
            };
            vm.openFromDate = openFromDate;
            vm.openToDate = openToDate;
            
            $scope.clickCheckAll = vm.clickCheckAll;
            vm.getListChecked = getListChecked;
            vm.getTable = getTable;
            vm.rowTableMainClick = rowTableMainClick;
            vm.prevPage = prevPage;
            vm.nextPage = nextPage;
            vm.currentPageChange = currentPageChange;
            vm.pageSizeChange = pageSizeChange;
            $window.tableMainprevPage = vm.prevPage;
            $window.tableMainnextPage = vm.nextPage;
            $window.tableMainpageSizeChange = vm.pageSizeChange;
            $window.tableMaincurrentPageChange = vm.currentPageChange;
            
            vm.doDelete = doDelete;
            vm.doExport = doExport;
            vm.doImport = doImport;
            vm.doExitImport = doExitImport;
            vm.doImportTarget = doImportTarget;
            vm.downloadTemplate = downloadTemplate;
            vm.doSearch = doSearch;
            vm.doRefresh = doRefresh;
            vm.objSearch = {};
            vm.dataSelected = [];
            vm.files = [];
            
            vm.lsProvince = [];
            vm.lsDistrict = [];
            vm.listSelectedProvince = [];
            vm.listSelectedDistrict = [];
            vm.selectedProvince = selectedProvince;
            vm.selectedDistrict = selectedDistrict;
            
            vm.tableHeadDefault = [
               {title: $translate.instant('global.index'), value: "tblIndex", checked: true, disable: true, isShow: true},
               {title: "", value: "tblCheckbox", checked: true, disable: true, isShow: false},
               //{title: $translate.instant('global.table.action'), value: "tblAction", checked: true, disable: true, isShow: true},
               {title: $translate.instant('target.labelInput.monthTargets'), value: "targetMonth", checked: true, disable: true, isShow: true},
               {title: $translate.instant('target.labelInput.typeTargets'), value: "targetType", checked: true, disable: true, isShow: true},
               {title: $translate.instant('target.labelInput.regionCode'), value: "regionCode", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.regionName'), value: "regionName", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.target'), value: "targetNum", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.warning1'), value: "warning1", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.warning2'), value: "warning2", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.createdUserName'), value: "createdFullName", checked: true, disable: false, isShow: true},
               {title: $translate.instant('target.labelInput.createDate'), value: "createdDate", checked: true, disable: false, isShow: true}
            ];
            vm.tableMainConfig = {
    		   tableName: 'tableMain',
    		   pageName: 'pageTarget',
               data: [],
               totalRecord: 0,
               totalPage: 0,
               currentPage: 1,
               pageSize: 10,
               sortIndex: 2,
               sortName: 'targetMonth',
               sortType: 'asc',
               propertyName: '',
               lastColumnWith: "10",
               headerDefault: vm.tableHeadDefault,
               hasPaging: true,
               dataSelected: []
            };
            initComboboxSearch(function(){
            	var dateMonth = '';
            	if(((new Date().getMonth() + 1) + "").length == 1) {
            		dateMonth = '0' + (new Date().getMonth() + 1);
            	} else {
            		dateMonth = (new Date().getMonth() + 1) + '';
            	}
            	$("#idFromDateFrom").val(dateMonth + "/" + new Date().getFullYear());
                $("#idFromDateTo").val(dateMonth + "/" + new Date().getFullYear());
                vm.objSearch.monthYear = "T";
            	vm.doSearch();
            });
        })();
    	
    	function doRefresh() {
        	vm.dataSelected = [];
        	vm.objSearch = {};
        	vm.listSelectedProvince = [];
            vm.listSelectedDistrict = [];
            initComboboxSearch(function(){
            	var dateMonth = '';
            	if(((new Date().getMonth() + 1) + "").length == 1) {
            		dateMonth = '0' + (new Date().getMonth() + 1);
            	} else {
            		dateMonth = (new Date().getMonth() + 1) + '';
            	}
            	$("#idFromDateFrom").val(dateMonth + "/" + new Date().getFullYear());
                $("#idFromDateTo").val(dateMonth + "/" + new Date().getFullYear());
                vm.objSearch.monthYear = "T";
            	vm.doSearch();
            });
        }
    	
    	function initComboboxSearch(callback) {
    		TargetService.getTypeTarget().$promise.then(function (response) {
            	vm.lstTargetType = response.data;
            	if(vm.lstTargetType.length > 0) {
            		vm.objSearch.targetType = vm.lstTargetType[0].code + "";
            	}
            	callback();
            }, function (err) {
            	vm.showAlert("error", "Err!!!");
            });
    		TargetService.getListProvince().$promise.then(function (response) {
    			vm.lsProvince = response.data;
            }, function (err) {
            	vm.showAlert("error", "Err!!!");
            });
    		var objectSearch = {lstProvinceCodes: []};
    		TargetService.getListDistrictByProvinceCode(objectSearch).$promise.then(function (response) {
    			vm.lsDistrict = response.data;
            }, function (err) {
            	vm.showAlert("error", "Err!!!");
            });
    	}
    	
    	function openFromDate() {
            vm.openedFromDate = true;
        }

        function openToDate() {
            vm.openedToDate = true;
        }
        
        function doDelete() {
        	if(vm.dataSelected.length > 0) {
        		vm.openFormConfirm($translate.instant('target.message.confirmDelete'), $translate.instant('global.message.confirm.delete'), function() {
	        		var objectDelete = {listIdDelete: vm.dataSelected};
	            	TargetService.deleteTarget(objectDelete).$promise.then(function (response) {
	            		var data = response.data;
	            		if (data.key === "SUCCESS") {
	                        vm.showAlert("success", $translate.instant('target.message.deleteSuccess'));
	                        vm.doSearch();
	                        vm.dataSelected = [];
	                    } else {
	                        vm.showAlert("error", "Error!!!");
	                    }
	                }, function (err) {
	                	vm.showAlert("error", "Err!!!");
	                });
        		});
        	} else {
        		vm.showAlert("warning", "Bạn cần chọn bản ghi để xóa !");
        	}
        }
        
        function doExport() {
        	TargetService.exportTarget(vm.objSearch).$promise.then(function (response) {
                vm.downloadFile(response);
            }, function (error) {
            });
        }

		function doImport() {
			vm.files = [];
			$("#idFormUpload")[0].reset();
			$('#modal-import-target').modal('show');
		}
		
		function doImportTarget() {
            var formData = new FormData();
            if (vm.files.length === 0) {
                vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.fileNull'));
                return;
            } else {
            	formData.append('formDataJson', null);
                formData.append('files', vm.files);
                TargetService.importTarget(formData).$promise.then(function (response) {
                    var result = response.headers["x-message-code"];
                    if (FILE_INVALID === result) {
                        vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.fileInvalid'));
                        return;
                    }
                    if (FILE_TYPE_INVALID === result) {
                        vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.fileTypeInvalid'));
                        return;
                    }
                    if (FILE_IS_NULL === result) {
                        vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.fileNull'));
                        return;
                    }
                    if (DATA_OVER === result) {
                        vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.overData'));
                        return;
                    }
                    if (NODATA === result) {
                        vm.showAlert("warning", $translate.instant('target.message.statusAddNew.import.noData'));
                        return;
                    }
                    if (SUCCESS === result) {
                    	vm.downloadFile(response);
                        vm.showAlert("success", $translate.instant('target.message.statusAddNew.import.success'));
                    } else {
                    	vm.downloadFile(response);
                        vm.showAlert("error", $translate.instant('target.message.statusAddNew.import.error'));
                    }
                    $('#modal-import-target').modal('hide');
                }, function (error) {
                    vm.showAlert("error", $translate.instant('target.message.statusAddNew.import.error'));
                });
            }
        }

        function downloadTemplate() {
        	TargetService.downloadFileTemp().$promise.then(function (response) {
                vm.downloadFile(response);
            }, function (error) {
            });
        }

        function doExitImport() {
            $('#modal-import-target').modal('hide');
        }
    	
    	function doSearch() {
            vm.tableMainConfig.pageSize = 10;
            vm.tableMainConfig.currentPage = parseInt("1");
            getListDataEmployee();
        }
        
        function selectedProvince() {
            var objectSearch = {lstProvinceCodes: []};
            $.each(vm.listSelectedProvince, function (i) {
            	objectSearch.lstProvinceCodes.push(vm.listSelectedProvince[i].code);
            });
        	TargetService.getListDistrictByProvinceCode(objectSearch).$promise.then(function (response) {
    			vm.lsDistrict = response.data;
    			vm.listSelectedDistrict = [];
            }, function (err) {
            });
        }
        
        function selectedDistrict() {
        	
        }
        
        function getListChecked(data) {
            vm.dataSelected = data;
        }
        
        function getTable(table) {
        	for (var i = 0; i < vm.tableMainConfig.headerDefault.length; i++) {
            	if(!vm.tableMainConfig.headerDefault[i].disable){
            		if(vm.tableMainConfig.headerDefault[i].checked){
            			table.column(i).visible(true);
            		} else {
            			table.column(i).visible(false);
            		}
            	}
			}
        }
        
        function rowTableMainClick(data) {
            
        }
        
        function prevPage() {
            if (vm.tableMainConfig.currentPage > 1) {
                vm.tableMainConfig.currentPage--;
                getListDataEmployee();
            }
        }

        function nextPage() {
            if (vm.tableMainConfig.currentPage < vm.tableMainConfig.totalPage) {
                vm.tableMainConfig.currentPage++;
                getListDataEmployee();
            }
        }

        function currentPageChange(el) {
            if ($(el).val() === "" || $(el).val() === "0") {
                $(el).val(1);
            }
            if (parseInt($(el).val()) > parseInt(vm.tableMainConfig.totalPage)) {
                $(el).val(parseInt(vm.tableMainConfig.totalPage));
            }
            vm.tableMainConfig.currentPage = parseInt($(el).val());
            getListDataEmployee();
        }

        function pageSizeChange(el) {
            vm.tableMainConfig.currentPage = 1;
            vm.tableMainConfig.pageSize = parseInt($(el).val());
            getListDataEmployee();
        }
        
        $rootScope.$on('$loadSortingDatatable', function(event, object){
        	vm.tableMainConfig.sortName = object.sortName;
        	vm.tableMainConfig.sortType = object.sortType;
        	vm.tableMainConfig.sortIndex = object.sortIndex;
        	if(object.pageName == 'pageTarget') {
        		getListDataEmployee();
        	}
        });
        
        function convertDateMonthString(value) {
        	if(value == "" || value == null || value == undefined) {
        		return null;
        	} else {
        		var arr = (value + "").split("/");
            	return arr[1] + "" + arr[0];
        	}
        }
        function convertDateYearString(value) {
        	if(value == "" || value == null || value == undefined) {
        		return null;
        	} else {
        		var arr = (value + "").split("/");
            	return arr[1] + "";
        	}
        }
        
        function getListDataEmployee() {
        	if(vm.objSearch.toDate != undefined && vm.objSearch.fromDate != undefined) {
        		if(new Date(vm.objSearch.fromDate).getTime() > new Date(vm.objSearch.toDate).getTime()) {
        			vm.showAlert("warning", "Tháng bắt đầu không được lớn hơn tháng kết thúc !");
            		return;
        		}
        	}
        	if($("#idFromDateFrom").val() != null && $("#idFromDateFrom").val() != "") {
        		if(!$scope.fSearchData.fromDateFrom.$error.required && !$scope.fSearchData.fromDateFrom.$error.dateValid && $scope.fSearchData.fromDateFrom.$invalid) {
            		vm.showAlert("warning", "Tháng bắt đầu không hợp lệ !");
            		return;
            	}
        	}
        	if($("#idFromDateTo").val() != null && $("#idFromDateTo").val() != "") {
        		if(!$scope.fSearchData.fromDateTo.$error.required && !$scope.fSearchData.fromDateTo.$error.dateValid && $scope.fSearchData.fromDateTo.$invalid) {
            		vm.showAlert("warning", "Tháng kết thúc không hợp lệ !");
            		return;
            	}
        	}
        	vm.loadingByIdTable("tableMain", "shown", vm.tableMainConfig.data);
            vm.objSearch.page = vm.tableMainConfig.currentPage;
            vm.objSearch.pageSize = vm.tableMainConfig.pageSize;
            vm.objSearch.sortName = vm.tableMainConfig.sortName;
            vm.objSearch.sortType = vm.tableMainConfig.sortType;

            vm.objSearch.listProvinceCodes = [];
            vm.objSearch.listDistrictCodes = [];
            $.each(vm.listSelectedProvince, function (i) {
            	vm.objSearch.listProvinceCodes.push(vm.listSelectedProvince[i].code);
            });
            $.each(vm.listSelectedDistrict, function (i) {
            	vm.objSearch.listDistrictCodes.push(vm.listSelectedDistrict[i].code);
            });
            if(vm.objSearch.monthYear == "T") {
            	vm.objSearch.fromDateString = convertDateMonthString($("#idFromDateFrom").val());
                vm.objSearch.toDateString = convertDateMonthString($("#idFromDateTo").val());
            }
            if(vm.objSearch.monthYear == "N") {
            	vm.objSearch.fromDateString = convertDateYearString($("#idFromDateFrom").val());
                vm.objSearch.toDateString = convertDateYearString($("#idFromDateTo").val());
            }
            TargetService.search(vm.objSearch).$promise.then(function (response) {
                vm.tableMainConfig.dataSelected = vm.dataSelected;
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(response.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(response.data.draw);
                var lsData = [];
                if(response.data.data != undefined) {
                	lsData = response.data.data;
                }
                for (var i = 0; i < lsData.length; i++) {
                    var item = lsData[i];
                    
                    //var action = '<button title="' + $translate.instant('global.action.send') + '" class="btn btn-xs" onclick="window.sendData(\'' + item.targetId + '\')"><i class="fa fa-send"></i></button>';
                    
                    var objAdd = {
                		"checkbox": {
                            value: "",
                            id: item.targetId,
                            align: "center",
                            header: "",
                            width: '50'
                        },
                		/*"action": {
                            value: action,
                            id: item.targetId,
                            align: "center",
                            header: $translate.instant('global.table.action'),
                            width: '60'
                        },*/
                        "targetMonth": {
                            value: item.targetMonth,
                            align: "center",
                            header: $translate.instant('target.labelInput.monthTargets'),
                            width: '100'
                        },
                        "targetName": {
                            value: item.targetName,
                            align: "left",
                            header: $translate.instant('target.labelInput.typeTargets'),
                            width: '250'
                        },
                        "regionCode": {
                            value: item.regionCode,
                            align: "left",
                            header: $translate.instant('target.labelInput.regionCode'),
                            width: '120'
                        },
                        "regionName": {
                            value: item.regionName,
                            align: "left",
                            header: $translate.instant('target.labelInput.regionName'),
                            width: '120'
                        },
                        "targetNum": {
                            value: item.targetNum,
                            align: "right",
                            header: $translate.instant('target.labelInput.target'),
                            width: '80'
                        },
                        "warning1": {
                            value: nullToStringEmpty(item.warning1),
                            align: "right",
                            header: $translate.instant('target.labelInput.warning1'),
                            width: '80'
                        },
                        "warning2": {
                            value: nullToStringEmpty(item.warning2),
                            align: "right",
                            header: $translate.instant('target.labelInput.warning2'),
                            width: '80'
                        },
                        "createdFullName": {
                            value: item.createdFullName,
                            align: "left",
                            header: $translate.instant('target.labelInput.createdUserName'),
                            width: '120'
                        },
                        "createdDate": {
                            value: vm.formatDateToString(item.createdDate),
                            align: "center",
                            header: $translate.instant('target.labelInput.createDate'),
                            width: '100'
                        }
                    };
                    vm.tableMainConfig.data.push(objAdd);
                }
                vm.loadingByIdTable("tableMain", "hidden", vm.tableMainConfig.data);
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
        }
    }
})();
