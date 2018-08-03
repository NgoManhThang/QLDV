// code style: https://github.com/johnpapa/angular-styleguide 
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('UserController', UserController);
    
    UserController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$window', '$element', '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'UserService'];
    function UserController($scope, $rootScope, $controller, $state, $window, $element, $translate, $http, $timeout, $sessionStorage, $localStorage, UserService){
        var vm = this;
        scopeHolder = $scope;
        
    	// Init controller
    	(function initController() {
    		
    		// instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);
            
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
            
            vm.editData = editData;
            vm.deleteData = deleteData;
            $window.editData = vm.editData;
            $window.deleteData = vm.deleteData;
            
            vm.doDeleteImage = doDeleteImage;
            vm.doSaveData = doSaveData;
            vm.doSearch = doSearch;
            vm.doAddNew = doAddNew;
            vm.doRefresh = doRefresh;
            vm.objSearch = {};
            vm.objBocUser = {};
            
            vm.openDateStartWorking = openDateStartWorking;
            
            vm.tableHeadDefault = [
               {title: $translate.instant('global.index'), value: "tblIndex", checked: true, disable: true, isShow: true},
               //{title: "", value: "tblCheckbox", checked: true, disable: true, isShow: false},
               {title: $translate.instant('global.table.action'), value: "tblAction", checked: true, disable: true, isShow: true},
               {title: $translate.instant('user.labelInput.imageUser'), value: "tblImage", checked: true, disable: true, isShow: true},
               {title: $translate.instant('user.labelInput.userName'), value: "userName", checked: true, disable: true, isShow: true},
               {title: $translate.instant('user.labelInput.fullName'), value: "fullName", checked: true, disable: false, isShow: true},
               {title: $translate.instant('user.labelInput.email'), value: "email", checked: true, disable: false, isShow: true},
               {title: $translate.instant('user.labelInput.phone'), value: "phone", checked: true, disable: false, isShow: true}
            ];
            vm.tableMainConfig = {
    		   tableName: 'tableMain',
    		   pageName: 'pageUser',
               data: [],
               totalRecord: 0,
               totalPage: 0,
               currentPage: 1,
               pageSize: 10,
               sortIndex: 3,
               sortName: 'userName',
               sortType: 'asc',
               propertyName: '',
               lastColumnWith: "10",
               headerDefault: vm.tableHeadDefault,
               hasPaging: true
            };
            initCombobox();
            vm.doSearch();
        })();
    	
    	function changedPosition(value) {
    		console.log(value);
    	}
    	
    	function changedRoleUser() {
    		
        };
        
        function changedUnitUser() {
    		
        };
        
        function changedRoleTargetUser() {
    		
        };
        
        function initCombobox() {
        	//position
            vm.dataPositionUser = [];
            vm.changedPosition = changedPosition;
            UserService.getListBocConstant({constantType:"POSITION"}).$promise.then(function (response) {
            	var allCbo = {constantCode: null, constantName: $translate.instant('global.common.toolbar-all')};
            	vm.dataPositionUser = response.data;
            	vm.dataPositionUser.unshift(allCbo);
            	vm.objBocUser.position = null;
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
            
            //company
            vm.dataPositionUser = [];
            UserService.getListBocConstant({constantType:"COMPANY"}).$promise.then(function (response) {
            	var allCbo = {constantCode: null, constantName: $translate.instant('global.common.toolbar-all')};
            	vm.dataCompanyUser = response.data;
            	vm.dataCompanyUser.unshift(allCbo);
            	vm.objBocUser.company = null;
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
            
            //tree role
            vm.dataRoleUser = [];
            vm.dataTreeRoleUser = [];
            vm.selectedRoleUser = [];
            vm.changedRoleUser = changedRoleUser;
            UserService.getListBocRole().$promise.then(function (response) {
            	vm.dataRoleUser = response.data;
                vm.dataTreeRoleUser = convertListToTree(vm.dataRoleUser, 'roleId', 'parentRoleId', 'children');
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
            //tree unit
            vm.dataUnitUser = [];
            vm.dataTreeUnitUser = [];
            vm.selectedUnitUser = [];
            vm.changedUnitUser = changedUnitUser;
            UserService.getListBocUnit().$promise.then(function (response) {
            	vm.dataUnitUser = response.data;
                vm.dataTreeUnitUser = convertListToTree(vm.dataUnitUser, 'unitId', 'parentUnitId', 'children');
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
            //tree role target
            vm.dataRoleTargetUser = [];
            vm.dataTreeRoleTargetUser = [];
            vm.selectedRoleTargetUser = [];
            vm.changedRoleTargetUser = changedRoleTargetUser;
            UserService.getListBocRoleTarget().$promise.then(function (response) {
            	vm.dataRoleTargetUser = response.data;
                vm.dataTreeRoleTargetUser = convertListToTree(vm.dataRoleTargetUser, 'roleTargetId', 'parentRoleTargetId', 'children');
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
        }
    	
    	function doSearch() {
            vm.tableMainConfig.data = [];
            vm.tableMainConfig.pageSize = 10;
            vm.tableMainConfig.currentPage = parseInt("1");
            getListDataEmployee();
        }
    	
    	function openDateStartWorking() {
            vm.openedDateStartWorking = true;
        }
    	
    	function doDeleteImage() {
    		vm.objBocUser.fileId = null;
    		vm.avatarImg = undefined;
    		vm.isHasImageLoad = false;
    		vm.urlImageUser = vm.getUrlImageByFileId(null);
    	}
        
        function doAddNew() {
        	vm.openFormModal($translate.instant('user.title.addDetailUser'), 'apps/boc/user/userDetail.html');
        	vm.isSaveData = true;
        	vm.objBocUser = {};
        	vm.avatarImg = undefined;
        	vm.isHasImageLoad = false;
        	$("#idImageAvatar").attr("src", vm.getUrlImageByFileId(null));
        	vm.urlImageUser = vm.getUrlImageByFileId(null);
        	vm.selectedRoleUser = [];
        	vm.selectedUnitUser = [];
        	vm.selectedRoleTargetUser = [];
        	initCombobox();
        }
        
        function doRefresh() {
        	vm.objSearch = {};
        	vm.objBocUser = {};
        	vm.avatarImg = undefined;
        	vm.selectedRoleUser = [];
        	vm.selectedUnitUser = [];
        	vm.selectedRoleTargetUser = [];
        	vm.doSearch();
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
        	getDetail(data.action.id);
        	vm.isSaveData = false;
        	vm.avatarImg = undefined;
        	vm.openFormModal($translate.instant('user.title.viewDetailUser'), 'apps/boc/user/userDetail.html');
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
        	if(object.pageName == 'pageUser') {
        		getListDataEmployee();
        	}
        });
        
        function getListDataEmployee() {
        	vm.loadingByIdTable("tableMain", "shown", vm.tableMainConfig.data);
        	
            vm.objSearch.page = vm.tableMainConfig.currentPage;
            vm.objSearch.pageSize = vm.tableMainConfig.pageSize;
            vm.objSearch.sortName = vm.tableMainConfig.sortName;
            vm.objSearch.sortType = vm.tableMainConfig.sortType;

            UserService.search(vm.objSearch).$promise.then(function (response) {
                vm.tableMainConfig.data = [];
                vm.tableMainConfig.totalRecord = parseInt(response.data.recordsTotal);
                vm.tableMainConfig.totalPage = parseInt(response.data.draw);
                var lsData = [];
                if(response.data.data != undefined) {
                	lsData = response.data.data;
                }
                var userInfo = $localStorage.user;
                for (var i = 0; i < lsData.length; i++) {
                    var item = lsData[i];

                    var actionEdit = '';
                    var actionDelete = '';
                    if(vm.hasAuthority("MANAGER_USER_EDIT")) {
                    	actionEdit = '<span title="' + $translate.instant('global.action.edit') + '" class="btn-icon-table" onclick="window.editData(\'' + item.userId + '\')"><i class="fa fa-edit"></i></span>';
                    }
                    if(vm.hasAuthority("MANAGER_USER_DELETE")) {
                    	actionDelete = '<span title="' + $translate.instant('global.action.delete') + '" class="btn-icon-table" onclick="window.deleteData(\'' + item.userId + '\')"><i class="fa fa-remove"></i></span>';
                    }
                    var action = actionEdit + '&nbsp;&nbsp;' + actionDelete;
                    var image = '<img class="img-circle" isShowTitleTable src="'+ vm.getUrlImageByFileId(item.fileId) +'" style="width: 50px; height: 50px;" />';
                    
                    var objAdd = {
                		/*"checkbox": {
                            value: "",
                            id: item.userId,
                            align: "center",
                            header: "",
                            width: '60'
                        },*/
                        "action": {
                            value: action,
                            id: item.userId,
                            align: "center",
                            header: $translate.instant('global.table.action'),
                            width: '80',
                            paddingTop: '20'
                        },
                        "imageUser": {
                            value: image,
                            align: "center",
                            header: $translate.instant('user.labelInput.imageUser'),
                            width: '80'
                        },
                        "userName": {
                            value: item.userName,
                            align: "left",
                            header: $translate.instant('user.labelInput.userName'),
                            width: '120',
                            paddingTop: '24'
                        },
                        "fullName": {
                            value: item.fullName,
                            align: "left",
                            header: $translate.instant('user.labelInput.fullName'),
                            width: '150',
                            paddingTop: '24'
                        },
                        "email": {
                            value: nullToStringEmpty(item.email),
                            align: "left",
                            header: $translate.instant('user.labelInput.email'),
                            width: '230',
                            paddingTop: '24'
                        },
                        "phoneNumber": {
                            value: nullToStringEmpty(item.phoneNumber),
                            align: "right",
                            header: $translate.instant('user.labelInput.phone'),
                            width: '150',
                            paddingTop: '24'
                        }
                    };
                    vm.tableMainConfig.data.push(objAdd);
                }
                vm.loadingByIdTable("tableMain", "hidden", vm.tableMainConfig.data);
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
        }
        
        function doSaveData() {
            if (vm.objBocUser.userName === "" || vm.objBocUser.userName === null || typeof vm.objBocUser.userName === "undefined") {
                vm.showAlert("warning", "Tên đăng nhập bắt buộc nhập");
                return;
            } else {
            	if(!checkAlphaNumericOnly(vm.objBocUser.userName)) {
            		vm.showAlert("warning", "Tên đăng nhập không chứa ký tự đặc biệt");
                    return;
            	}
            }
            if (vm.objBocUser.fullName === "" || vm.objBocUser.fullName === null || typeof vm.objBocUser.fullName === "undefined") {
                vm.showAlert("warning", "Họ tên bắt buộc nhập");
                return;
            }
            if (vm.objBocUser.phoneNumber === "" || vm.objBocUser.phoneNumber === null || typeof vm.objBocUser.phoneNumber === "undefined") {
            	vm.objBocUser.phoneNumber = "";
            	//vm.showAlert("warning", "Số điện thoại bắt buộc nhập");
                //return;
            } else {
            	var regexPhoneNumber = /^\s*(?:\+?(\d{1,3}))?([-. (]*(\d{3})[-. )]*)?((\d{3})[-. ]*(\d{2,4})(?:[-.x ]*(\d+))?)\s*$/gm;
                if(!regexPhoneNumber.test(vm.objBocUser.phoneNumber)) {
                	vm.showAlert("warning", "Số điện thoại không đúng định dạng");
                    return;
                }
            }
            if (vm.objBocUser.email === "" || vm.objBocUser.email === null || typeof vm.objBocUser.email === "undefined") {
                vm.showAlert("warning", "Email bắt buộc nhập");
                return;
            } else {
            	var validateEmail = vm.objBocUser.email.toLowerCase();
                if (!/^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z]{2,5}$/.test(validateEmail)
                    && !/^[a-z]+[a-z0-9._]+@[a-z]+\.[a-z]{2,7}\.[a-z]{2,5}$/.test(validateEmail)) {
                    vm.showAlert("warning", "Email không đúng định dạng");
                    return;
                }
            }
            if ($("#errorDateStartWorking").val() == "true") {
            	vm.showAlert("warning", "Ngày bổ nhiệm không đúng định dạng");
                return;
            }
            if(vm.objBocUser.userId === "" || vm.objBocUser.userId === null || typeof vm.objBocUser.userId === "undefined") {
            	if (vm.objBocUser.password === "" || vm.objBocUser.password === null || typeof vm.objBocUser.password === "undefined") {
                    vm.showAlert("warning", "Mật khẩu bắt buộc nhập");
                    return;
                } else if(vm.objBocUser.password.length < 6) {
                	vm.showAlert("warning", "Mật khẩu phải ít nhất 6 kí tự");
                    return;
                }
                if (vm.objBocUser.rePassword === "" || vm.objBocUser.rePassword === null || typeof vm.objBocUser.rePassword === "undefined") {
                    vm.showAlert("warning", "Chưa nhập lại mật khẩu");
                    return;
                } else {
                	if(vm.objBocUser.password != vm.objBocUser.rePassword) {
                		vm.showAlert("warning", "Mật khẩu không khớp với nhau");
                        return;
                	}
                }
            } else {
            	if (vm.objBocUser.password === "" || vm.objBocUser.password === null || typeof vm.objBocUser.password === "undefined") {
                    
                } else if(vm.objBocUser.password.length < 6) {
                	vm.showAlert("warning", "Mật khẩu phải ít nhất 6 kí tự");
                    return;
                }
                if (vm.objBocUser.rePassword === "" || vm.objBocUser.rePassword === null || typeof vm.objBocUser.rePassword === "undefined") {
                	if (vm.objBocUser.password === "" || vm.objBocUser.password === null || typeof vm.objBocUser.password === "undefined") {
                        
                    } else {
                    	vm.showAlert("warning", "Chưa nhập lại mật khẩu");
                        return;
                    }
                } else {
                	if(vm.objBocUser.password != vm.objBocUser.rePassword) {
                		vm.showAlert("warning", "Mật khẩu không khớp với nhau");
                        return;
                	}
                }
            }
            if(vm.objBocUser.company == "CNTT" && vm.selectedUnitUser.length > 0) {
            	vm.showAlert("warning", "Trung tâm CNTT không có đơn vị");
                return;
            }
            var file = vm.avatarImg;
            if(file != undefined) {
            	if(file.size > 10485760){
                    vm.showAlert("warning", "File" + file.name + "dung lượng quá lớn, tối đa là 10MB !");
                    return;
    			}
            }
            vm.objBocUser.workingDateString = $("#idWorkingDate").val();
            vm.objBocUser.userName = vm.objBocUser.userName.trim();
            vm.objBocUser.fullName = vm.objBocUser.fullName.trim();
            vm.objBocUser.phoneNumber = vm.objBocUser.phoneNumber.trim();
            vm.objBocUser.email = vm.objBocUser.email.trim();
            
            vm.objBocUser.listRole = vm.selectedRoleUser;
            vm.objBocUser.listUnit = vm.selectedUnitUser;
            vm.objBocUser.listRoleTarget = vm.selectedRoleTargetUser;
            
            var formData = new FormData();
            formData.append('formDataJson', JSON.stringify(vm.objBocUser));
            formData.append('files', file);
            
            if(vm.objBocUser.userId === "" || vm.objBocUser.userId === null || typeof vm.objBocUser.userId === "undefined") {
            	//Them
            	UserService.add(formData).$promise.then(function (response) {
                    var data = response.data;
                    if (data.key === "SUCCESS") {
                        vm.showAlert("success", $translate.instant('user.message.saveSuccess'));
                        vm.doSearch();
                        vm.closeFormModal();
                        vm.objBocUser = {};
                        $("#idFSaveData")[0].reset();
                        initCombobox();
                    } else if (data.key === "DUPLICATE") {
                        if (data.message === "USERNAME") {
                            vm.showAlert("warning", $translate.instant('user.message.duplicateUserName'));
                        }
                        if (data.message === "UNIT") {
                        	if(vm.objBocUser.company == "CNTT") {
                        		vm.showAlert("warning", $translate.instant('user.message.duplicateUnitCNTT'));
                        	} else {
                        		vm.showAlert("warning", $translate.instant('user.message.duplicateUnit'));
                        	}
                        }
                    } else {
                        vm.showAlert("error", "Error!!!");
                    }
                }, function (dataError) {
                    vm.showAlert("error", "Err!!!");
                });
            } else {
            	//Sua
            	UserService.edit(formData).$promise.then(function (response) {
                    var data = response.data;
                    if (data.key === "SUCCESS") {
                        vm.showAlert("success", $translate.instant('user.message.saveSuccess'));
                        vm.doSearch();
                        vm.closeFormModal();
                        vm.objBocUser = {};
                        $("#idFSaveData")[0].reset();
                        initCombobox();
                    } else if (data.key === "DUPLICATE") {
                        if (data.message === "USERNAME") {
                            vm.showAlert("warning", $translate.instant('user.message.duplicateUserName'));
                        }
                        if (data.message === "UNIT") {
                        	if(vm.objBocUser.company == "CNTT") {
                        		vm.showAlert("warning", $translate.instant('user.message.duplicateUnitCNTT'));
                        	} else {
                        		vm.showAlert("warning", $translate.instant('user.message.duplicateUnit'));
                        	}
                        }
                    } else {
                        vm.showAlert("error", "Error!!!");
                    }
                }, function (dataError) {
                    vm.showAlert("error", "Err!!!");
                });
            }
        }
        
        function getDetail(userId) {
        	UserService.getDetail({userId: userId}).$promise.then(function (response) {
                vm.objBocUser = response.data;
                if(vm.objBocUser.fileId != null) {
                	vm.isHasImageLoad = true;
                } else {
                	vm.isHasImageLoad = false;
                }
                vm.urlImageUser = vm.getUrlImageByFileId(vm.objBocUser.fileId);
                //set role
                var selRole = [];
                if(vm.objBocUser.listRoleString != null) {
                	selRole = vm.objBocUser.listRoleString.split(";");
                }
                vm.dataTreeRoleUser = setValueForTree(selRole, vm.dataRoleUser, 'roleId', 'parentRoleId', 'children');
                vm.selectedRoleUser = selRole;
                //set unit
                var selUnit = [];
                if(vm.objBocUser.listUnitString != null) {
                	selUnit = vm.objBocUser.listUnitString.split(";");
                }
                vm.dataTreeUnitUser = setValueForTree(selUnit, vm.dataUnitUser, 'unitId', 'parentUnitId', 'children');
            	vm.selectedUnitUser = selUnit;
            	//set role target
                var selRoleTarget = [];
                if(vm.objBocUser.listRoleTargetString != null) {
                	selRoleTarget = vm.objBocUser.listRoleTargetString.split(";");
                }
                vm.dataTreeRoleTargetUser = setValueForTree(selRoleTarget, vm.dataRoleTargetUser, 'roleTargetId', 'parentRoleTargetId', 'children');
                vm.selectedRoleTargetUser = selRoleTarget;
            }, function (dataError) {
                vm.showAlert("error", "Err!!!");
            });
        }
        
        function editData(userId) {
        	getDetail(userId);
        	vm.avatarImg = undefined;
        	vm.isSaveData = true;
        	vm.openFormModal($translate.instant('user.title.editDetailUser'), 'apps/boc/user/userDetail.html');
        }

        function deleteData(userId) {
        	vm.openFormConfirm($translate.instant('user.message.confirmDelete'), $translate.instant('global.message.confirm.delete'), function() {
        		UserService.deleteUser({userId: userId}).$promise.then(function (response) {
            		var data = response.data;
            		if (data.key === "SUCCESS") {
                        vm.showAlert("success", $translate.instant('user.message.deleteSuccess'));
                        vm.doSearch();
                    } else {
                        vm.showAlert("error", "Error!!!");
                    }
                }, function (dataError) {
                    vm.showAlert("error", "Err!!!");
                });
        	});
        }
    }
})();
