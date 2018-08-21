/**
 * Created by VTN-PTPM-NV04 on 2/5/2018.
 */
(function () {
    'use strict';
    angular.module('app').controller('BaseController', BaseController);
    
    angular.module("app").directive("selectOptionRepeat", function selectOptionRepeat($timeout) {
        return function(scope, element, attrs) {
            if (scope.$last){
                // Here is where already executes the jquery
                $timeout(function() {
                    $(".selectBoxIt").selectBoxIt({
                        theme: "default",
                        //defaultText: "Make a selection...",
                        autoWidth: false
                    });
                });
            }
        }
    });

    function BaseController(vm, ProfileService, BaseService, $locale, $translate, $location, $rootScope, $localStorage, APP_REST_SERVICE, $filter) {
        vm.dateFormat = "dd/MM/yyyy";
        $locale.NUMBER_FORMATS.DECIMAL_SEP = ".";
        $locale.NUMBER_FORMATS.GROUP_SEP = ",";
        vm.getUrlImageByFileId = function(fileId) {
        	if(fileId != null) {
        		return APP_REST_SERVICE + "common/getFileById?fileId=" + fileId;
        	} else {
        		return './img/person.png';
        	}
        }
        
        vm.doSaveModal = function(callback) {
        	callback();
        }
        
        vm.openFormModal = function(title, includeHtml) {
        	$("#modalHead").html(title);
        	vm.includeHtmlModal = includeHtml;
        	$('#formModal').modal('show');
        }
        
        vm.closeFormModal = function() {
        	$('#formModal').modal('hide');
        }
    	
    	vm.showAlert = function (type, content) {
    		noty({text: content, layout: 'bottomRight', type: type});
            setTimeout(function () {
                vm.closeAlert(type);
            }, 6000);
        };
        vm.closeAlert = function (type) {
        	$.noty.closeAll();
        };
        
        vm.openFormConfirm = function(title, content, callback) {
        	$("#confirmHead").html(title);
        	$("#confirmBody").html(content);
        	$('#formConfirm').modal('show');
        	
        	vm.callFunctionConfirm = function(){
        		callback();
		    }
        }
        
        vm.closeFormConfirm = function() {
        	$('#formConfirm').modal('hide');
        }
        
        /**
         * Table check all button click handel
         * @param tableId
         */
        vm.hasCheckAll = false;
        vm.clickCheckAll = function(tableId) {
            var checkBoxAll = $("#" + tableId).parent().prev().find('table').find('input');
            var isChecked = (checkBoxAll).is(':checked');
            $("#" + tableId + " tbody tr").each(function () {
                var checkBox = $(this).find('input[class*="checkbox-in-table"]');
                $(checkBox).prop("checked", isChecked);
            });
            $("#" + tableId + " tbody tr:first-child input[class*='checkbox-in-table']").change();
            if (isChecked) {
                vm.hasCheckAll = true;
            }
            else {
                vm.hasCheckAll = false;
            }
        };
        
        vm.downloadFile = downloadFile;
        function downloadFile(data) {
            var url = URL.createObjectURL(data.response);
            var contentDisposition = data.headers["content-disposition"];
            var fileName = contentDisposition.split(";")[2].split("=")[1].split("\"").join("");
            var anchor = angular.element('<a/>');
            anchor.attr({
                href: url,
                target: '_self',
                download: fileName
            });

            angular.element(document.body).append(anchor);
            anchor [0].click();
        }

        vm.loadingByIdTable = function(id,action,data) {
        	var panel = $("#"+id).parents(".panel");
            if(action && action === "shown") {
            	 panel.append('<div class="panel-refresh-layer"><img src="img/loaders/default.gif"/></div>');
                 if(data.length == 0) {
                	 panel.find(".panel-refresh-layer").width(panel.width() + 18).height("208px");
                 } else {
                	 panel.find(".panel-refresh-layer").width(panel.width() + 18).height(panel.height());
                 }
            }
            if(action && action === "hidden") {
            	panel.find(".panel-refresh-layer").remove();
            }
        }
        
        vm.formatDateToString = function(date) {
        	if(date) {
        		var d = new Date(date);
        		var dateMonth = '';
        		if(((d.getMonth() + 1) + "").length == 1) {
            		dateMonth = '0' + (d.getMonth() + 1);
            	} else {
            		dateMonth = (d.getMonth() + 1) + '';
            	}
        		return d.getDate() + '/' + dateMonth + '/' + d.getFullYear();
        	} else {
        		return '';
        	}
        }
        
        vm.hasAuthority = function(authority) {
        	if($localStorage.user != null) {
        		var hasAuth = $localStorage.user.listRole && $localStorage.user.listRole.indexOf(authority) !== -1;
        		if (hasAuth) {
                    return true;
                } else {
                	return false;
                }
            } else {
            	return false;
        	}
        }

        vm.stringIsNotNullOrEmpty = function (value) {
            return typeof value !== 'undefined' && value !== null && value.trim() !== "";
        };
        
        vm.numberIsNotNull = function (value) {
            return typeof value !== 'undefined' && value !== null && value !== "";
        };

        vm.filterDate = function (value, format) {
            return $filter('date')(value, format);
        }
    }
})();

function setValueForTree(arr, data, idAttr, parentAttr, childrenAttr) {
	var dataTree = [];
	if(arr != null) {
    	for (var i = 0; i < data.length; i++) {
			if(arr.indexOf(data[i][idAttr] + "") > -1) {
				data[i].selected = true;
			} else {
				data[i].selected = false;
			}
		}
    	dataTree = convertListToTree(data, idAttr, parentAttr, childrenAttr);
    } else {
    	for (var j = 0; j < data.length; j++) {
    		data[j].selected = false;
		}
    	dataTree = convertListToTree(data, idAttr, parentAttr, childrenAttr);
    }
	return dataTree;
}

function convertListToTree(list, idAttr, parentAttr, childrenAttr) {
    if (!idAttr) idAttr = 'id';
    if (!parentAttr) parentAttr = 'parent';
    if (!childrenAttr) childrenAttr = 'children';

    var treeList = [];
    var lookup = {};
    list.forEach(function(obj) {
        lookup[obj[idAttr]] = obj;
        obj[childrenAttr] = [];
    });
    list.forEach(function(obj) {
        if (obj[parentAttr] != null) {
            lookup[obj[parentAttr]][childrenAttr].push(obj);
        } else {
            treeList.push(obj);
        }
    });
    return treeList;
};

function nullToStringEmpty(value) {
	if(value == null || value == undefined) {
		return "";
	} else {
		return value;
	}
}

function forceNumericOnly(e) {
    //var regex = /[0-9]|\./;
    //var regex = /[0-9]/;
	var regex = /[0-9\(\)\+\-]/;
    if (!regex.test(e.key) && e.key !== " " && e.key !== "Backspace"
        && e.key !== "Delete" && e.key !== "Shift" && e.key !== "Tab"
        && e.key !== "(" && e.key !== ")" && e.key !== "+"){
        e.preventDefault();
    }
}

function checkAlphaNumericOnly(value) {
	var regex = new RegExp("^[a-zA-Z0-9]+$");
	return regex.test(value);
}

function forceAlphaNumericOnly(e) {
    var regex = new RegExp("^[a-zA-Z0-9]+$");
    if (!regex.test(e.key) && e.key !== "Backspace" && e.key !== "Delete" && e.key !== "Shift" && e.key !== "Tab"){
        e.preventDefault();
    }
}

function changeToFile(el) {
    $(el).prev().click();
}
function showImage(el) {
    $(el).next().attr("src", URL.createObjectURL($(el).prop('files')[0]));
}

String.prototype.getFileExtension = function () {
    return this.substring(this.lastIndexOf('.') + 1, this.length) || this;
};