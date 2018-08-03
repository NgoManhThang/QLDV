/**
 * Created by DongNV on 12/6/2017.
 */

(function () {
    'use strict';
    angular.module('app').directive('itsGridTree', itsGridTree);

    itsGridTree.$inject = ['$translate', '$rootScope'];
    function itsGridTree($translate, $rootScope) {
        return {
            restrict: 'A',
            replace: true,
            scope: {
                pData: '@',
                fRowClick: '&',
                fGetTable: '&'
            },
            link: function (scope, element, attr) {
                //<editor-fold desc="Declare variable">
                var table = null;
                var tableName = "";
                //</editor-fold>

                /**
                 * Function create and configure table
                 * @param tableId
                 * @returns {jQuery}
                 */
                function initTable(tableId) {
                    var paramObj = angular.fromJson(attr.pData);
                    tableName = paramObj.tableName === undefined ? "" : paramObj.tableName;
                    var lstObj = paramObj.data;
                    var headerDefault = filterHeader(paramObj.headerDefault);
                    var tblHeight = paramObj.tblHeight;

                    var hasPaging = paramObj.hasPaging === undefined ? false : paramObj.hasPaging;
                    var totalRecord = paramObj.totalRecord === undefined ? 0 : paramObj.totalRecord;
                    var totalPage = paramObj.totalPage === undefined ? 0 : paramObj.totalPage;
                    var currentPage = paramObj.currentPage === undefined ? 1 : paramObj.currentPage;
                    var pageSize = paramObj.pageSize === undefined ? 10 : paramObj.pageSize;

                    var lstMap = [];
                    angular.forEach(lstObj[0], function (value, key) {
                        var map = {};
                        map.data = key + "." + "value";
                        map.title = value.header;
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
                            ': <input class="form-control form-control-sm current-page" type="text" id="txtCurrentPage" value="' + currentPage + '" onpaste="return event.preventDefault();" onkeypress="return event.charCode >= 48 && event.charCode <=57" onchange="window.' + tableName + 'currentPageChange(this, \'' + tableName + '\')" /> / ' + (totalPage + "").replace(/\B(?=(\d{3})+\b)/g, ",") +
                            '<button class="btn btn-sm btn-icon white next" onclick="window.' + tableName + 'nextPage(\'' + tableName + '\')">' +
                            '<i class="fa fa-angle-right"></i>' +
                            '</button>' +
                            '</div>'
                        },
                        columns: lstObj.length === 0 ? headerDefault : lstMap,
                        autoWidth: false,
                        createdRow: function (row, data, dataIndex) {
                            $(row).addClass("treegrid-" + (dataIndex + 1));
                            $(row).addClass(data.action.parent);
                        },
                        columnDefs: [
                            {
                                targets: "_all",
                                createdCell: function (td, cellData, rowData, rIndex, cIndex) {
                                    angular.forEach(rowData, function (value, key) {
                                        if (lstMap[td.cellIndex].title === value.header) {
                                            if (value.color != null && value.color !== "") {
                                                $(td).css('color', value.color);
                                            }
                                            if (value.align != null && value.align !== "") {
                                                $(td).css('text-align', value.align);
                                            }
                                            return;
                                        }
                                    });
                                },
                                orderable: true
                            }
                        ],
                        initComplete: function (settings, json) {
                            $(".grid-tree").treegrid({
                                initialState: "collapsed"
                            });
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
                                $rootScope.$broadcast(tableName + 'SortingDatatable', {sortName: sortName, sortType: sortType, sortIndex: sortIndex});
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
            }
        }
    }
})();