/**
 * Created by DongNV on 12/6/2017.
 */

(function () {
    'use strict';
    angular.module('app').directive('itsDatatable', itsDatatable);

    itsDatatable.$inject = ['$translate', '$rootScope'];
    function itsDatatable($translate, $rootScope) {
        return {
            restrict: 'A',
            replace: true,
            scope: {
                getListChecked: '&',
                pData: '@',
                fRowClick: '&',
                fGetTable: '&'
            },
            link: function (scope, element, attr) {
                //<editor-fold desc="Declare variable">
            	String.prototype.format = function () {
                    var args = [].slice.call(arguments);
                    return this.replace(/(\{\d+\})/g, function (a){
                        return args[+(a.substr(1,a.length-2))||0];
                    });
            	};
            	
                var checkBoxHtml = "<label class='md-check'> <input type='checkbox' class='checkbox-in-table' " +
                    "value='\{0}\'{1}><i class='blue'></i></label>";

                var checkBoxAllHtml = "<label class='md-check'> <input type='checkbox' class='" +
                    " check-box-all-table' onclick='angular.element(this).scope().clickCheckAll(\"{0}\")'><i" +
                    " class='blue'></i></label>";

                var table = null;
                var pageName = undefined;
                scope.dataSelected = [];
                //</editor-fold>

                /**
                 * Function create and configure table
                 * @param tableId
                 * @returns {jQuery}
                 */
                function initTable(tableId) {
                    var paramObj = angular.fromJson(attr.pData);
                    var tableName = paramObj.tableName === undefined ? "" : paramObj.tableName;
                    pageName = paramObj.pageName === undefined ? "" : paramObj.pageName;
                    var lstObj = paramObj.data;
                    var headerDefault = filterHeader(paramObj.headerDefault);
                    var pPropertyName = paramObj.propertyName;
                    var tblHeight = paramObj.tblHeight;

                    var hasPaging = paramObj.hasPaging === undefined ? false : paramObj.hasPaging;
                    
                    var totalRecord = paramObj.totalRecord === undefined ? 0 : paramObj.totalRecord;
                    var totalPage = paramObj.totalPage === undefined ? 0 : paramObj.totalPage;
                    var currentPage = paramObj.currentPage === undefined ? 1 : paramObj.currentPage;
                    var pageSize = paramObj.pageSize === undefined ? 10 : paramObj.pageSize;
                    scope.dataSelected = paramObj.dataSelected === undefined ? [] : paramObj.dataSelected;
                    
                    var lstMap = [];
                    var columnDefsDefault = [];
                    
                    var indexNumber = 0;
                    for (var i = 0; i < headerDefault.length; i++) {
						switch (headerDefault[i].value) {
						   	case "tblIndex":
						   		//Add stt
			                    lstMap.push(
			                        {data: null, title: $translate.instant('global.index'), width: "10", className: "data-table-index"}
			                    );
			                    columnDefsDefault.push({
			                        orderable: false,
			                        targets: indexNumber,
			                        createdCell: function (td, cellData, rowData, rIndex, cIndex) {
			                        	/*if(rowData.imageUser != undefined) {
			                        		$(td).css('padding-top', "24px");
			                        	}*/
			                        },
			                        render: function (data, type, row, meta) {
			                        	if(hasPaging) {
			                                return (currentPage - 1) * pageSize + meta.row + 1;
			                            } else {
			                                return meta.row + 1;
			                            }
			                        }
			                    });
			                    indexNumber++;
			                    break;
						   	case "tblCheckbox":
						   		//Add checkbox
						   		columnDefsDefault.push({
		                            orderable: false,
		                            targets: indexNumber,
		                            render: function (data, type, row, meta) {
		                            	var checked = "";
		                            	if(scope.dataSelected.length > 0) {
		                            		$.each(scope.dataSelected, function (i) {
		                                        if (row.checkbox.id == angular.fromJson(scope.dataSelected[i])){
		                                            checked = " checked";
		                                            return false;
		                                        } else {
		                                        	checked = " " + data;
		                                        }
		                                    });
		                            	} else {
		                                	checked = " " + data;
		                                }
		                                return checkBoxHtml.format(angular.toJson(row.checkbox.id), checked);
		                            }
		                        });
						   		indexNumber++;
						   		break;
						   	case "tblAction": 
						   		//Add action
						   		columnDefsDefault.push({
		                            orderable: false,
		                            targets: indexNumber,
		                            render: function (data, type, row, meta) {
		                            	return data;
		                            }
		                        });
						   		indexNumber++;
						   		break;
						   	case "tblImage": 
						   		//Add image
						   		columnDefsDefault.push({
		                            orderable: false,
		                            targets: indexNumber,
		                            render: function (data, type, row, meta) {
		                            	return data;
		                            }
		                        });
						   		indexNumber++;
						   		break;
						   	default: 
						   		break;
						}
					}
                    columnDefsDefault.push({
                        targets: "_all",
                        createdCell: function (td, cellData, rowData, rIndex, cIndex) {
                            angular.forEach(rowData, function (value, key) {
                                if (lstMap[td.cellIndex].title === value.header) {
                                    if (value.color != null && value.color !== "" && value.color != undefined) {
                                        $(td).css('color', value.color);
                                    }
                                    if (value.align != null && value.align !== "" && value.align != undefined) {
                                        $(td).css('text-align', value.align);
                                    }
                                    if (value.width != null && value.width !== "" && value.width != undefined) {
                                    	$(td).css('width', value.width + "px");
                                    	$(td).css('maxWidth', value.width + "px");
                                    	$(td).css('minWidth', value.width + "px");
                                    }
                                    if (value.paddingTop != null && value.paddingTop !== "" && value.paddingTop != undefined) {
                                        $(td).css('padding-top', value.paddingTop + "px");
                                    }
                                    $(td).addClass('truncate');
                                    return;
                                }
                            });
                        },
                        render: function (data, type, row, meta) {
                        	if (type == 'display') {
                    			return '<span title= \"' + data +'\" class="td-container">'+data+'</span>';
                        	} else {
                                return data;
                            } 
                        },
                        orderable: true
                    });
                    
                    angular.forEach(lstObj[0], function (value, key) {
                        var map = {};
                        map.data = key + "." + "value";
                        if(key == "checkbox") {
                        	map.title = checkBoxAllHtml.format(attr.id);
                        } else {
                        	map.title = value.header;
                        }
                        map.width = value.width;
                        map.class = 'header-no-wrap';
                        lstMap.push(map);
                    });

                    var init = $(tableId).DataTable({
                        data: lstObj,
                        searching: false,
                        dom: 'frtlip',
                        paging: hasPaging,
                        pagingType: "simple_numbers",
                        pageLength: pageSize,
                        ordering: true,
                        order: [[ (typeof paramObj.sortIndex === "undefined" ? 0 : paramObj.sortIndex), (typeof paramObj.sortType === "undefined" ? "asc" : paramObj.sortType) ]],
                        orderFixed: {
                        	pre: [0, 'asc']
                        },
                        scrollY: tblHeight,
                        responsive: true,
                        scrollX: true,
                        info: false,
                        language: {
                            sZeroRecords: $translate.instant('datatable.sZeroRecords'),
                            paginate: {
                                previous: $translate.instant('datatable.oPaginate.sPrevious'),
                                next: $translate.instant('datatable.oPaginate.sNext'),
                                first: $translate.instant('datatable.oPaginate.sFirst'),
                                last: $translate.instant('datatable.oPaginate.sLast')
                            },
                            lengthMenu: '<div class="custom-pagination-length">' +
                            $translate.instant('datatable.display') +
                            ' <select class="page-size" onchange="window.' + tableName + 'pageSizeChange(this, \'' + tableName + '\')">' +
                            '<option value="10">10</option>' +
                            '<option value="20">20</option>' +
                            '<option value="30">30</option>' +
                            '<option value="40">40</option>' +
                            '<option value="50">50</option>' +
                            '</select> / ' +
                            '<span>' +  (totalRecord + "").replace(/\B(?=(\d{3})+\b)/g, ",") + '</span> ' +
                            $translate.instant('datatable.records') +
                            '</div>' +
                            '<div class="custom-pagination">' +
                            '<button class="btn btn-sm btn-icon white prev" onclick="window.' + tableName + 'prevPage(\'' + tableName + '\')">' +
                            '<i class="fa fa-angle-left"></i>' +
                            '</button>' +
                            $translate.instant('datatable.page') +
                            ': <input class="form-control form-control-sm current-page" type="text" id="txtCurrentPage" value="' + currentPage + '" onpaste="return event.preventDefault();" onkeypress="return event.charCode >= 48 && event.charCode <=57" oninput="window.' + tableName + 'currentPageChange(this, \'' + tableName + '\')" /> / ' + (totalPage + "").replace(/\B(?=(\d{3})+\b)/g, ",") +
                            '<button class="btn btn-sm btn-icon white next" onclick="window.' + tableName + 'nextPage(\'' + tableName + '\')">' +
                            '<i class="fa fa-angle-right"></i>' +
                            '</button>' +
                            '</div>'
                        },
                        columns: lstObj.length === 0 ? headerDefault : lstMap,
                        autoWidth: false,
                        columnDefs: columnDefsDefault,
                        initComplete: function (settings, json) {
                        	
                        }
                    });

                    return init;
                }
                
                /**
                 * Click sort in header
                 */
            	$(element).on('order.dt', function(e) {
            		//e.stopImmediatePropagation();
            		if(table != null) {
            			var columns = table.settings().init().columns;
                    	var order = table.order();
                    	if(order.length > 0) {
                    		if(columns[order[0][0]]['mData'] != null) {
                    			var sortIndex = order[0][0];
                    			var sortName = columns[order[0][0]]['mData'].substring(0, columns[order[0][0]]['mData'].length - 6);
                    			var sortType = order[0][1];
                    			$rootScope.$broadcast('$loadSortingDatatable', {pageName: pageName, sortName: sortName, sortType: sortType, sortIndex: sortIndex});
                    		}
                    	}
            		}
                });

                function filterHeader(headerDefault) {
                    var arrHead = [];
                    for (var i = 0; i < headerDefault.length; i++){
                        if (typeof headerDefault[i].checked === "undefined"){
                            arrHead.push(headerDefault[i]);
                        }
                        else if (headerDefault[i].checked){
                            arrHead.push(headerDefault[i]);
                        }
                    }
                    return arrHead;
                }

                /**
                 * Watch data. Redraw table When data has been changed
                 */
                attr.$observe('pData', function () {
                    // if table is created then remove the table from the DOM
                    if ($.fn.DataTable.isDataTable((element))) {
                        table.destroy();
                        $('#' + attr.id).empty();
                    }

                    // if Data is not empty then create table
                    if (angular.fromJson(attr.pData).data !== null) {
                        table = initTable(element);
                        scope.fGetTable({table: table});
                    }

                    //resize column width of all table in view
                    $($.fn.dataTable.tables(true)).DataTable().columns.adjust().responsive.recalc();
                });

                /**
                 * Click row event handle
                 */
                $(element).on('dblclick', 'tr td:not(:last-child)', function (event) {
                    var data = table.row($(event.target).closest('tr')).data();
                    scope.fRowClick({data: data});
                });
                
                /**
                 * Click checkbox in each row
                 */
                $(element).on('change', 'tr td label input[class*="checkbox-in-table"]', function () {
                	var check = true;
                    var arr = [];
                    $(element).find('tbody').find('tr').each(function () {
                        var checkBox = $(this).find('input[class*="checkbox-in-table"]');
                        if ($(checkBox).is(':checked')) {
                            arr.push($(checkBox).val());
                        }
                        check = check && $(checkBox).is(':checked');
                    });
                    $(element).parent().prev().find('table').find('input').prop("checked", check);
                    scope.getListChecked({data: arr});
                });
                
                // Checkbox in first column
                /*$(element).on('click', 'tr th label input[class*="check-box-all-table"]', function () {
                    alert(111);
                	var check = true;
                    $(element).find('tbody').find('tr').each(function () {
                        var checkBox = $(this).find('input[class*="checkbox-in-table"]');
                        if ($(checkBox).is(':checked')) {
                            var objectCheck = angular.fromJson($(checkBox).val());
                            var isExist = false;
                            $.each(scope.dataSelected, function (i) {
                                if (JSON.stringify(objectCheck) === JSON.stringify(scope.dataSelected[i])){
                                    isExist = true;
                                    return false;
                                }
                            });
                            if (!isExist){
                                scope.dataSelected.push(objectCheck);
                            }
                        }
                        else{
                            var objectCheck = angular.fromJson($(checkBox).val());
                            $.each(scope.dataSelected, function (i) {
                                if (JSON.stringify(objectCheck) === JSON.stringify(scope.dataSelected[i])){
                                    scope.dataSelected.splice(i, 1);
                                    return false;
                                }
                            });
                        }
                        check = check && $(checkBox).is(':checked');
                    });
                    $(element).parent().prev().find('table').find('input').prop("checked", check);
                    scope.getListChecked({data: scope.dataSelected});
                });*/
            }
        }
    }
})();