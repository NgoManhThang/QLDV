// code style: https://github.com/johnpapa/angular-styleguide 
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('StatisticsController', StatisticsController);
    
    StatisticsController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$element', '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'MapsService', 'NgTableParams'];
    function StatisticsController($scope, $rootScope, $controller, $state, $element, $translate, $http, $timeout, $sessionStorage, $localStorage, MapsService, NgTableParams){
        var vm = this;
        scopeHolder = $scope;
        
    	// Init controller
    	(function initController() {
    		
    		// instantiate base controller
            $controller('BaseController', {
                vm: vm
            });
            $rootScope.$broadcast('$elementLoadJavascript', $element);
            
            Chart.defaults.global.defaultFontColor = 'rgba(255, 255, 255, 0.87)';
            
            vm.chartOptionsBar = {
    			scales: {
    				yAxes: [
    					{
    						id: 'y-axis-1',
    						type: 'linear',
    						display: true,
    						position: 'left'
    					}
    				],
    				xAxes: [
				        {
				        	//barPercentage: 0.5,
				        	ticks: {
			                    autoSkip: false,
			                    maxRotation: 90,
			                    minRotation: 90
			                }
        	        	}
    				]
    			},
    			legend: {
    				display: true,
    				position: "bottom"
    			},
    			elements: {
    				line : {
    					tension: 0
    				}
    			}
    		}
            vm.chartOptionsLine = {
        			scales: {
        				yAxes: [
        					{
        						id: 'y-axis-1',
        						type: 'linear',
        						display: true,
        						position: 'left'
        					}
        				],
        				xAxes: [
    				        {
    				        	//barPercentage: 0.5,
    				        	ticks: {
    			                    autoSkip: false,
    			                    maxRotation: 90,
    			                    minRotation: 90
    			                }
            	        	}
        				]
        			},
        			legend: {
        				display: true,
        				position: "bottom"
        			},
        			elements: {
        				line : {
        					tension: 0
        				}
        			}
        		}
    		vm.chartOptionsPie = {
    			legend: {
    				display: true,
    				position: "left"
    			}
    		}
            
            vm.colorsChartOptionsBar = [
 	 			{backgroundColor: '#1caf9a', borderColor: '#1caf9a'},
 	 			{backgroundColor: '#c70505', borderColor: '#c70505'},
 	 			{backgroundColor: '#ea06af', borderColor: '#ea06af'},
 	 			{backgroundColor: '#078c11', borderColor: '#078c11'},
 	 			{backgroundColor: '#09a1b1', borderColor: '#09a1b1'},
 	 			{backgroundColor: '#0f0cf3', borderColor: '#0f0cf3'}
 	 		];
            
            vm.colorsChartOptions = [
	 			{backgroundColor: 'transparent', borderColor: '#1caf9a'},
	 			{backgroundColor: 'transparent', borderColor: '#c70505'},
	 			{backgroundColor: 'transparent', borderColor: '#ea06af'},
	 			{backgroundColor: 'transparent', borderColor: '#078c11'},
	 			{backgroundColor: 'transparent', borderColor: '#09a1b1'},
	 			{backgroundColor: 'transparent', borderColor: '#0f0cf3'}
	 		];
            
            vm.sysDate = new Date();
            vm.lstChart = [];
            vm.listChartOddEven = [];
            vm.listIdPaging = [];
            
            vm.onClickExpandChart = onClickExpandChart;
            
            vm.titleStatistics = 'EVNSPC';
            
            vm.provinceCode = null;
            vm.provinceName = null;
            vm.districtCode = null;
            vm.districtName = null;
            vm.bocCode = null;
            vm.regionLevel = null;
            if($localStorage.valueForMapStatistics != null && $localStorage.valueForMapStatistics != undefined) {
            	vm.provinceCode = $localStorage.valueForMapStatistics.provinceCode;
            	vm.provinceName = $localStorage.valueForMapStatistics.provinceName;
                vm.districtCode = $localStorage.valueForMapStatistics.districtCode;
                vm.districtName = $localStorage.valueForMapStatistics.districtName;
                vm.bocCode = $localStorage.valueForMapStatistics.bocCode;
                vm.regionLevel = $localStorage.valueForMapStatistics.regionLevel;
            }
            
            vm.openTabPageStatistics = openTabPageStatistics;
            
            if(vm.regionLevel == 0) {
            	initPage(vm.provinceCode, vm.regionLevel, vm.bocCode);
            	vm.openTabPageStatistics('SHOW-INFO-RIGHT-CHART', 'SHOW-INFO-RIGHT');
            } else if(vm.regionLevel == 1) {
            	initPage(vm.provinceCode, vm.regionLevel, vm.bocCode);
            	vm.titleStatistics = vm.provinceName;
            	vm.openTabPageStatistics('SHOW-INFO-RIGHT-CHART', 'SHOW-CHART');
            } else if(vm.regionLevel == 2) {
            	initPage(vm.districtCode, vm.regionLevel, vm.bocCode);
            	vm.titleStatistics = vm.districtName;
            	vm.openTabPageStatistics('SHOW-INFO-RIGHT-CHART', 'SHOW-CHART');
            } else {
            	initPage(vm.provinceCode, 0, vm.bocCode);
            	vm.openTabPageStatistics('SHOW-INFO-RIGHT-CHART', 'SHOW-INFO-RIGHT');
            }
        })();
    	
    	function initPage(regionCode, regionLevel, bocCode) {
    		MapsService.getStatisticsTargets({regionCode: regionCode, regionLevel: regionLevel, bocCode: bocCode}).$promise.then(function (response) {
    			vm.statisticsTargets = response.data[0];
            }, function (err) {
            });
            
            MapsService.getListTargetsStatistics({regionCode: regionCode, regionLevel: regionLevel, bocCode: bocCode}).$promise.then(function (response) {
                vm.tableParamsTarget = new NgTableParams({count: response.data.length}, { counts: [], dataset: response.data});
            }, function (err) {
            });
            
            MapsService.getListStatisticsInfoEmployee().$promise.then(function (response) {
                vm.listStatisticsInfoEmployee = response.data;
                vm.listStatisticsInfoEmployeeOddEven = groupArrayLstEmployee(response.data);
            }, function (err) {
            });
    	}
    	
    	vm.viewMaps = function(obj) {
    		$localStorage.valueForMapStatistics = {
				bocCode: obj.code,
    			provinceCode: vm.provinceCode,
            	districtCode: vm.districtCode,
            	regionLevel: vm.regionLevel
    		};
    		$state.go('boc.maps');
    	}
    	
    	function groupArrayLstEmployee(arr) {
        	var result = [];
        	for (var i = 0; i < arr.length; i++) {
    			if(i % 2 == 0) {
    				var data = [];
    				data.push(arr[i]);
    				data.push(arr[i+1]);
    				result.push(data);
    			}
    		}
        	return result;
        }
    	
    	function openTabPageStatistics(pageId, tabId) {
    		$("#" + pageId + " .tab-content-page").removeClass("active");
    		$("#" + pageId + " #" + tabId).addClass("active");
    		if(tabId == 'SHOW-CHART') {
    			MapsService.getListBocModule().$promise.then(function (response) {
        			vm.listBocModule = removeDuplicatesArrayObject(response.data, "moduleGroup");
        			if(vm.listBocModule.length > 0) {
        				vm.openTabChart('chart-boc-page', vm.listBocModule[0].moduleGroup);
        			}
                }, function (err) {
                });
    		}
    	}
    	
    	vm.pagination = function (chartGroup, page){
    		var lengthPaging = Math.ceil(vm.lstChart.length/4);
			for (var i = 0; i < lengthPaging; i++) {
				$("#SHOW-CHART #" + chartGroup).find("div.page-" + (i+1)).hide();
			}
			$("#SHOW-CHART #" + chartGroup).find("div.page-" + page).show();
			
			$("#SHOW-CHART #" + chartGroup).find(".chart-pagination").find("button").removeClass("active");
			$("#SHOW-CHART #" + chartGroup).find(".chart-pagination").find("button.page-" + page).addClass("active");
    	}
        
        function pagingChart(pageId, len) {
    		vm.listIdPaging = [];
    		var numPage = Math.ceil(len/4);
    		for (var i = numPage; i > 0; i--) {
    			if(i==1) {
    				vm.listIdPaging.push({id: pageId+"-PAGING-"+i, number: i});
    			} else {
    				vm.listIdPaging.push({id: pageId+"-PAGING-"+i, number: i});
    			}
    		}
    	}
        
        function groupArrayLstChart(arr) {
        	var paging = 1;
        	var dataPaging = [];
        	for (var j = 0; j < arr.length; j++) {
    			if(j % 4 == 0) {
    				if(arr[j] !== undefined) {
    					arr[j].paging = paging;
    					dataPaging.push(arr[j]);
    				} else {
    					dataPaging.push({paging: paging});
    				}
    				if(arr[j+1] !== undefined) {
    					arr[j + 1].paging = paging;
    					dataPaging.push(arr[j+1]);
    				} else {
    					dataPaging.push({paging: paging});
    				}
    				if(arr[j+2] !== undefined) {
    					arr[j + 2].paging = paging;
    					dataPaging.push(arr[j+2]);
    				} else {
    					dataPaging.push({paging: paging});
    				}
    				if(arr[j+3] !== undefined) {
    					arr[j + 3].paging = paging;
    					dataPaging.push(arr[j+3]);
    				} else {
    					dataPaging.push({paging: paging});
    				}
    				paging++;
    			}
    		}
        	var result = [];
        	for (var i = 0; i < dataPaging.length; i++) {
    			if(i % 2 == 0) {
    				var data = [];
    				data.push(dataPaging[i]);
    				data.push(dataPaging[i+1]);
    				data.push({isPaging : 'NO',paging : dataPaging[i].paging});
    				result.push(data);
    			}
    		}
        	return result;
        }
        
        vm.chartClick = function() {
        	
        }
        
        vm.backMaps = function() {
    		if(vm.regionLevel == 1) {
            	$localStorage.valueForMapStatistics = {
					bocCode: vm.bocCode,
	    			provinceCode: 'ALL',
	            	districtCode: null,
	            	regionLevel: 0
	    		};
            } else if(vm.regionLevel == 2) {
            	$localStorage.valueForMapStatistics = {
        			bocCode: vm.bocCode,
	    			provinceCode: vm.provinceCode,
	            	districtCode: null,
	            	regionLevel: 1
	    		};
            }
    		$state.go('boc.maps');
        }
        
    	vm.nextTabChart = function(pageId) {
    		var curentTab = $("#" + pageId + " .tab button.active");
    		var listTab = $("#chart-boc-page .tab button[class^='btn']");
    		
    		for (var i = 0; i < listTab.length; i++){
    			if (listTab[i].className == $(curentTab).attr("class")){
					var nextTab = $(listTab[i + 1]);
					vm.openTabChart('chart-boc-page', $(nextTab).attr("class").replace("btn-", ""));
    				break;
    			}
    		}
    	}
    	
    	vm.openTabChart = function(pageId, tabId) {
			$("#" + pageId + " .tab button.active").removeClass("active");
			$("#" + pageId + " .tab-content").removeClass("active");
			
			$("#" + pageId + " .tab button.btn-" + tabId).addClass("active");
			$("#" + pageId + " #" + tabId).addClass("active");
			$("#" + pageId + " .tab button.btn-" + tabId).focus();
			$("#" + pageId + " #" + tabId).focus();
			
			MapsService.getListBocModule().$promise.then(function (response) {
				var listDraw = [];
				for (var i = 0; i < response.data.length; i++) {
					if(response.data[i].moduleGroup == tabId) {
						listDraw.push(response.data[i]);
					}
				}
				buildChart(listDraw);
	    		vm.listChartOddEven = groupArrayLstChart(vm.lstChart);
	    		pagingChart(tabId, vm.lstChart.length);
            }, function (err) {
            });
    	}
    	
    	function buildChart(lsModul) {
    		vm.lstChart = [];
            for (var i = 0; i < lsModul.length; i++){
                var item = lsModul[i];
                var objChart = {
                    chartId: "chart" + (i + 1),
                    chartTitle: item.moduleName,
                    chartModule: item,
                    objSearch: item.jsonParam !== null ? angular.fromJson(item.jsonParam) : null,
                    chartData: [],
                    chartLabels: [],
                    chartSeries: [],
                    chartTypeConfig: '',
                    chartOptions: {}
                };
                vm.lstChart.push(objChart);
            }

            for (var i = 0; i < vm.lstChart.length; i++){
            	var item = vm.lstChart[i];
                var objSearch = item.objSearch === null ? new Object() : item.objSearch;
                
                objSearch.provinceCode = vm.provinceCode;
                objSearch.districtCode = vm.districtCode;
                
                var lsChartModule = [];
                lsChartModule.push(vm.lstChart[i].chartModule);
                objSearch.lstKpiCode = lsChartModule;
                
                objSearch.bocCode = vm.lstChart[i].chartModule.moduleGroup;
                objSearch.service = vm.lstChart[i].chartModule.service;
                
        		if(objSearch.service == "CHART_XT") {
        			var now = new Date(vm.sysDate);
            		var monthValueFrom = (now.getMonth() + 1);
            		if(monthValueFrom.toString().length == 1) {
            			monthValueFrom = '0' + (monthValueFrom + 1);
            		} else {
            			monthValueFrom = monthValueFrom + 1;
            		}
            		var monthValueTo = (now.getMonth() + 1);
            		if(monthValueTo.toString().length == 1) {
            			monthValueTo = '0' + monthValueTo;
            		}
            		objSearch.fromDate = (now.getFullYear() - 1) + '' + monthValueFrom + '' + now.getDate();
            		objSearch.toDate = now.getFullYear() + '' + monthValueTo + '' + now.getDate();
    			}
    			if(objSearch.service == "CHART_LK") {
    				var now = new Date(vm.sysDate);
            		var monthValueTo = (now.getMonth() + 1);
            		if(monthValueTo.toString().length == 1) {
            			monthValueTo = '0' + monthValueTo;
            		}
            		objSearch.fromDate = now.getFullYear() + '01' + now.getDate();
            		objSearch.toDate = now.getFullYear() + '' + monthValueTo + '' + now.getDate();
    			}
				if(objSearch.service == "CHART_REGION") {
					var now = new Date(vm.sysDate);
	        		var monthValueFrom = (now.getMonth() + 1);
	        		if(monthValueFrom.toString().length == 1) {
	        			monthValueFrom = '0' + (monthValueFrom + 1);
	        		} else {
	        			monthValueFrom = monthValueFrom + 1;
	        		}
	        		var monthValueTo = (now.getMonth() + 1);
	        		if(monthValueTo.toString().length == 1) {
	        			monthValueTo = '0' + monthValueTo;
	        		}
	        		objSearch.fromDate = (now.getFullYear() - 1) + '' + monthValueFrom + '' + now.getDate();
	        		objSearch.toDate = now.getFullYear() + '' + monthValueTo + '' + now.getDate();
				}
        		loadDataChart(i, objSearch);
            }
        }
    	
    	function loadDataChart(index, objSearch) {
    		/*var data1 = [];
    		var data2 = [];
    		var label = [];
    		for (var i = 0; i < 10; i++) {
    			data1.push(randomXToY(10, 30));
    			data2.push(randomXToY(10, 30));
    			label.push('0'+i+1+'/01');
			}
    		vm.lstChart[index].chartData.push(data1);
    		vm.lstChart[index].chartData.push(data2);
    		vm.lstChart[index].chartLabels = label;
    		vm.lstChart[index].chartSeries = ["Thực hiện", "Mục tiêu"];
    		
    		vm.lstChart[index].chartOptions = vm.chartOptionsLine;
			vm.lstChart[index].chartTypeConfig = 'chart-bar';*/
    		MapsService.getBocDataChart(objSearch).$promise.then(function (response) {
    			if(objSearch.service == "CHART_XT") {
    				var data1 = response.data[0].actual.split(";");
            		var data2 = response.data[0].target.split(";");
            		var label = response.data[0].displayDate.split(";");
            		vm.lstChart[index].chartData.push(convertValueForChart(data1));
            		vm.lstChart[index].chartData.push(convertValueForChart(data2));
            		vm.lstChart[index].chartLabels = label;
            		vm.lstChart[index].chartSeries = ["Thực hiện", "Mục tiêu"];
            		
            		vm.lstChart[index].chartOptions = vm.chartOptionsLine;
        			vm.lstChart[index].chartTypeConfig = 'chart-line';
    			}
    			if(objSearch.service == "CHART_LK") {
    				var data1 = response.data[0].actual.split(";");
            		var data2 = response.data[0].target.split(";");
            		var label = response.data[0].displayDate.split(";");
            		vm.lstChart[index].chartData.push(convertValueForChart(data1));
            		vm.lstChart[index].chartData.push(convertValueForChart(data2));
            		vm.lstChart[index].chartLabels = label;
            		vm.lstChart[index].chartSeries = ["Thực hiện", "Mục tiêu"];
            		
            		vm.lstChart[index].chartOptions = vm.chartOptionsLine;
        			vm.lstChart[index].chartTypeConfig = 'chart-line';
    			}
				if(objSearch.service == "CHART_REGION") {
					var data1 = response.data[0].actual.split(";");
	        		var label = response.data[0].regionName.split(";");
	        		vm.lstChart[index].chartData.push(convertValueForChart(data1));
	        		vm.lstChart[index].chartLabels = label;
	        		vm.lstChart[index].chartSeries = ["Thực hiện"];
	        		
	        		vm.lstChart[index].chartOptions = vm.chartOptionsBar;
	    			vm.lstChart[index].chartTypeConfig = 'chart-bar';
				}
            }, function (err) {
            });
    		/*MapsService.getBocDataChart().$promise.then(function (response) {
    			for(var i = 0; i < data.length; i++){
    				vm.lstChart[index].chartLabels.push(data[i].reasonName);
    				vm.lstChart[index].chartData.push(data[i].quanity);
    			}
    			vm.lstChart[index].chartLabels = setPercentLabelPie(vm.lstChart[index].chartLabels, vm.lstChart[index].chartData);
    			vm.lstChart[index].chartOptions = vm.chartOptionsPie;
    			vm.lstChart[index].chartTypeConfig = 'chart-pie';
            }, function (err) {
            });*/
        }
    	
    	function convertValueForChart(array) {
    		var result = [];
    		for (var i = 0; i < array.length; i++) {
    			var value = array[i];
    			if(value.trim() == "-9999") {
    				result.push(0);
        		} else if(value.trim().charAt(1) == ".") {
        			result.push("0" + value);
        		} else {
        			result.push(value);
        		}
			}
    		return result;
    	}
    	
    	function randomXToY(minVal,maxVal) {
    		var randVal = minVal+(Math.random()*(maxVal-minVal));
    		return Math.round(randVal);
    	}
    	
    	function removeDuplicates(originalArray, prop) {
            var newArray = [];
            var lookupObject  = {};

            for(var i in originalArray) {
               lookupObject[originalArray[i][prop]] = originalArray[i];
            }

            for(i in lookupObject) {
                newArray.push(lookupObject[i]);
            }
             return newArray;
        }
    	
    	function removeDuplicatesArrayObject(originalArray, prop) {
	   	    var newArray = [];
	   	    var lookupObject  = {};
	   	    for(var i in originalArray) {
	   	        lookupObject[originalArray[i][prop]] = originalArray[i];
	   	    }
	   	    for(i in lookupObject) {
	   	    	//if(lookupObject[i][prop] !== null) {
	   	    	if(lookupObject[i][prop] !== undefined) {
	   	    		newArray.push(lookupObject[i]);
	   	    	}
	   	    }
	   	    return newArray;
	   	}
	   	
	   	function setPercentLabelPie(arrText, arrValue) {
	   		var total = 0;
	   		$.each(arrValue,function() {
	   		    total += parseFloat(this);
	   		});
	   		for (var j = 0; j < arrText.length; j++) {
	   			var valuePercent = '';
	   			if(arrValue[j] == 0) {
	   				valuePercent = ' (0.00%)';
	   			} else {
	   				valuePercent = ' (' + (arrValue[j] / total * 100).toFixed(2) + '%)';
	   			}
	   			arrText[j] = arrText[j] + valuePercent;
	   		}
	   		return arrText;
	   	}
	   	
	   	function numberToPercent(arr) {
	   		for (var i = 0; i < arr.length; i++) {
	   			arr[i] = (arr[i]*100).toFixed(2);
	   		}
	   		return arr;
	   	}
	   	
	   	function toFixedNumber(arr) {
	   		for (var i = 0; i < arr.length; i++) {
	   			arr[i] = (arr[i]*1).toFixed(2);
	   		}
	   		return arr;
	   	}
	   	
	   	function onClickExpandChart($event) {
	   		panel_fullscreen($($event.currentTarget).parents(".panel"));
	        return false;
	   	}
	   	
	   	function panel_fullscreen(panel){    
	   	    
	   	    if(panel.hasClass("panel-fullscreened")){
	   	        panel.removeClass("panel-fullscreened").unwrap();
	   	        panel.find(".panel-body,.chart-holder").css("height","");
	   	        panel.find(".panel-fullscreen .fa").removeClass("fa-compress").addClass("fa-expand");        
	   	        
	   	        $(window).resize();
	   	    }else{
	   	        var head    = panel.find(".panel-heading");
	   	        var body    = panel.find(".panel-body");
	   	        var footer  = panel.find(".panel-footer");
	   	        var hplus   = 30;
	   	        
	   	        if(body.hasClass("panel-body-table") || body.hasClass("padding-0")){
	   	            hplus = 0;
	   	        }
	   	        if(head.length > 0){
	   	            hplus += head.height()+21;
	   	        } 
	   	        if(footer.length > 0){
	   	            hplus += footer.height()+21;
	   	        } 

	   	        panel.find(".panel-body,.chart-holder").height($(window).height() - hplus);
	   	        
	   	        
	   	        panel.addClass("panel-fullscreened").wrap('<div class="panel-fullscreen-wrap"></div>');        
	   	        panel.find(".panel-fullscreen .fa").removeClass("fa-expand").addClass("fa-compress");
	   	        
	   	        $(window).resize();
	   	    }
	   	}
    }
})();
