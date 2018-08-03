//code style: https://github.com/johnpapa/angular-styleguide
//Create by: Tung,Le Thanh - ITSOL.vn

var appModule = angular.module('ivh.dropdown', ['ivh.treeview']);

appModule.directive('tltDropdown', ['$parse', '$document', '$compile', 'optionParser', '$translate', 'ivhTreeviewBfs', 'ivhTreeviewMgr',
    function ($parse, $document, $compile, optionParser, $translate, ivhTreeviewBfs, ivhTreeviewMgr) {

    return {
        restrict: 'E',
        scope: {
            ivhModel: '=',
            isMultipleChoose: '=',
            isSelectParent: '='
        },
        require: 'ngModel',
        templateUrl: 'scripts/directives/tlt-drop-down.html',
        replace: true,
        link: function (scope, element, attr, modelCtrl) {
            scope.selectedText = attr.placeholder;
            scope.onClickShowHideIVHDropdown = function (e, scope) {
                if (e.target.parentElement.classList[0] === "dropDown" && e.target.parentElement.parentElement.children.length === 2 && e.target.parentElement.parentElement.children[1].classList[0] === "dropDownContent") {
                    e.target.parentElement.parentElement.children[1].classList.toggle("show");
                }
                else if (e.target.parentElement.classList[0] === "truncate" && e.target.parentElement.parentElement.parentElement.children.length === 2 && e.target.parentElement.parentElement.parentElement.children[1].classList[0] === "dropDownContent") {
                    e.target.parentElement.parentElement.parentElement.children[1].classList.toggle("show");
                }
                else if (e.target.parentElement.classList[0] === "dropDownButton" && e.target.parentElement.parentElement.parentElement.children.length === 2 && e.target.parentElement.parentElement.parentElement.children[1].classList[0] === "dropDownContent") {
                    e.target.parentElement.parentElement.parentElement.children[1].classList.toggle("show");
                }
                else if (e.target.parentElement.classList[0] === "dropDownButtonIcon" && e.target.parentElement.parentElement.parentElement.parentElement.children.length === 2 && e.target.parentElement.parentElement.parentElement.parentElement.children[1].classList[0] === "dropDownContent") {
                    e.target.parentElement.parentElement.parentElement.parentElement.children[1].classList.toggle("show");
                }
            };
            
            Array.prototype.unique = function() {
                var a = this.concat();
                for(var i=0; i<a.length; ++i) {
                    for(var j=i+1; j<a.length; ++j) {
                        if(a[i] === a[j])
                            a.splice(j--, 1);
                    }
                }
                return a;
            };

            scope.changeCallback = function (n) {
            	if(scope.isSelectParent) {
            		ivhTreeviewBfs(scope.ivhModel, function(node, parents) {
                    	if(n === node && node.selected) {
    	                    angular.forEach(parents, function(p) {
    	                    	p.selected = true;
    	                    });
    	                    return false;
                    	}
                    });
                }
            	
            	scope.selectedService = getSelectedFormIVHTreeModel(scope.ivhModel);
            	if (scope.selectedService.length > 0) {
            		scope.selectedText = "(" + scope.selectedService.length + ") ";
            	} else {
            		scope.selectedText = attr.placeholder;
            	}
            	if(scope.isMultipleChoose) {
            		ivhTreeviewBfs(scope.ivhModel, function(node, parents) {
                    	if(node.selected) {
                    		scope.selectedText += node.label + "; ";
                    	}
                    });
            		if (scope.selectedService.length > 0) {
            			scope.selectedText = scope.selectedText.substring(0, scope.selectedText.length - 2);
            		}
            	} else {
            		ivhTreeviewBfs(scope.ivhModel, function(node, parents) {
                    	if(node.selected) {
                    		scope.selectedText = node.label;
                    	}
                    });
            	}
            };

            $document.bind('click', function (event) {
                var isClickedElementChildOfPopup = element.find(event.target).length > 0;
                if (!isClickedElementChildOfPopup && element[0].children[1].classList.contains("show")) {
                    element[0].children[1].classList.toggle("show");
                }
                if(isClickedElementChildOfPopup && !scope.isMultipleChoose && element.find(event.target)[0].checked) {
                	var lsParentNode = $(element).children(".dropDownContent").children(".ivhTree").children("ul.ivh-treeview").find("li");
                    for (var i = 0; i < lsParentNode.length; i++) {
                        var lsCheckbox = $(lsParentNode[i]).find("input[type='checkbox']");
                        for (var j = 0; j < lsCheckbox.length; j++) {
                            if ($(lsCheckbox[j]).is(":checked")) {
                                $(lsCheckbox[j]).click();
                            }
                        }
                    }
                    element.find(event.target).click();
                }
            });
            
            var setZindex = function () {
                var zIndexDropDownButton = parseInt(element[0].children[0].style.zIndex === '' ? 0 : element[0].children[0].style.zIndex);
                element[0].children[0].style.zIndex = zIndexDropDownButton;
                element[0].children[0].children[0].style.zIndex = zIndexDropDownButton + 9999;
                element[0].children[1].style.zIndex = zIndexDropDownButton + 10000;
            };

            setZindex();

            var getSelectedFormIVHTreeModel = function (ivhModel) {
                var selectedItems = [];

                ivhTreeviewBfs(ivhModel, function(node) {
	        		if(node.selected) {
	        			selectedItems.push(node.code);
	        		}
        		});
                
                selectedItems = selectedItems.unique();
                modelCtrl.$setViewValue(selectedItems);
                return selectedItems;
            };

            scope.selectAll = function () {
            	ivhTreeviewMgr.selectAll(scope.ivhModel);
            	scope.changeCallback();
            };

            scope.deselectAll = function () {
            	ivhTreeviewMgr.deselectAll(scope.ivhModel);
            	scope.changeCallback();
            };

            scope.$watch('ivhModel', function (newValue, oldValue) {
                scope.selectedText = attr.placeholder;
                scope.changeCallback();
            });
        }
    };
}]);