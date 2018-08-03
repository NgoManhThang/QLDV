// code style: https://github.com/johnpapa/angular-styleguide 
var scopeHolder;
(function() {
    'use strict';
    angular.module('app').controller('MapsController', MapsController);
    
    MapsController.$inject = ['$scope', '$rootScope', '$controller', '$state', '$element', '$translate', '$http', '$timeout', '$sessionStorage', '$localStorage', 'MapsService', 'NgTableParams'];
    function MapsController($scope, $rootScope, $controller, $state, $element, $translate, $http, $timeout, $sessionStorage, $localStorage, MapsService, NgTableParams){
        var vm = this;
        scopeHolder = $scope;
        
        var defaultZoomMap = 6;
    	var defaultCenterMap = new google.maps.LatLng(16.2315000483959, 105.84750731250006);
        
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
            
            vm.viewImageFirstEmployee = './img/person.png';
            vm.viewImageSecondEmployee = './img/person.png';
            vm.tabChartCurent = '';
            vm.sysDate = new Date();
            vm.lstChart = [];
            vm.listChartOddEven = [];
            vm.listIdPaging = [];
            
            vm.provinceCodeChoose = '';
            vm.districtCodeChoose = '';
            vm.bocCodeChoose = '';
            
            vm.onClickExpandChart = onClickExpandChart;
            
            vm.valueForMapStatistics = $localStorage.valueForMapStatistics;
            if(vm.valueForMapStatistics == null || vm.valueForMapStatistics == undefined) {
            	vm.valueForMapStatistics = {
        			bocCode: null,
        			provinceCode: null,
        			provinceName: null,
                	districtCode: null,
                	districtName: null,
                	regionLevel: null
            	};
			}
            
    		initSearch(function(){
    			searchAll();
    		});
        })();
    	
    	function initSearch(callback) {
    		vm.zoomMapOption = defaultZoomMap;
    	    vm.centerMapOption = defaultCenterMap;
    	    vm.listPolygon = [];
    	    vm.listLabelForPolygon = [];
    	    vm.listPolygonProvinceByCode = [];
    	    vm.listPolygonDistrictByCode = [];
    	    
    	    vm.provinces = [];
    	    vm.provinceSelected = {};
    	    vm.targets = [];
    		vm.targetSelected = {};
    		
    		vm.bocTitle = [];
    		vm.bocData = [];
            vm.bocTitleHeader = [];
            
            vm.monthTitle = [];
            vm.topTenWorstBOC = [];
    		
    	    MapsService.getProvince().$promise.then(function (response) {
                vm.provinces = response.data;
                // Add items to the beginning of an array
        		var item = {};
        		item.provinceVname = 'Tất cả PC';
        		item.provinceCode = 'ALL';
        		var regionLevel = null;
                if(vm.provinces.length > 0) {
                	if(vm.provinces[0].regionLevel == 2) {
                		regionLevel = vm.provinces[0].regionLevel;
                	} else {
                		vm.provinces.unshift(item);
                	}
                }
    			vm.provinces = removeDuplicates(vm.provinces, "provinceCode");
    			if(vm.valueForMapStatistics.provinceCode != null) {
    				var provinceCode = vm.valueForMapStatistics.provinceCode;
    				for (var i = 0; i < vm.provinces.length; i++) {
						if(provinceCode == vm.provinces[i].provinceCode) {
							vm.provinceSelected.selected = vm.provinces[i];
							break;
						}
					}
    				regionLevel = 2;
    			} else {
    				vm.provinceSelected.selected = vm.provinces[0];
    			}
    			
    			MapsService.getBocCode().$promise.then(function (response) {
                    vm.targets = response.data;
                    vm.listDropdownTargets = removeDuplicatesArrayObject(response.data, "codeGroup");
                    if(vm.valueForMapStatistics.bocCode != null) {
        				var bocCode = vm.valueForMapStatistics.bocCode;
        				for (var i = 0; i < vm.targets.length; i++) {
							if(bocCode == vm.targets[i].code) {
								vm.targetSelected.selected = vm.targets[i];
								break;
							}
						}
        			} else {
        				vm.targetSelected.selected = vm.targets[0];
        			}
                    callback();
                }, function (err) {
                });
            }, function (err) {
            });
    	}
    	
    	vm.chooseBocData = function(obj) {
    		if(vm.provinceSelected.selected == undefined || vm.provinceSelected.selected.provinceCode == 'ALL') {
                getInfomationBocData('ALL', obj.codeGroup);
                //chart
                setParamForChart(null, null, obj.code);
    		} else {
                getInfomationBocData(vm.provinceSelected.selected.provinceCode, obj.codeGroup);
                //chart
                setParamForChart(vm.provinceSelected.selected.provinceCode, null, obj.code);
    		}
    		vm.groupNameShow = obj.decodeGroup;
    	}
    	
    	vm.viewMapProvince = function(obj) {
    		if(vm.provinceSelected.selected == undefined || vm.provinceSelected.selected.provinceCode == 'ALL') {
        		vm.provinceSelected.selected = {provinceCode:obj.regionCode,provinceVname:obj.regionName};
        		$localStorage.valueForMapStatistics = {
    				bocCode: vm.targetSelected.selected.code,
        			provinceCode: obj.regionCode,
        			provinceName: obj.regionName,
                	districtCode: null,
                	districtName: null,
                	regionLevel: 1
        		};
	        	$state.go('boc.statistics');
    		} else {
	        	$localStorage.valueForMapStatistics = {
    				bocCode: vm.targetSelected.selected.code,
        			provinceCode: vm.provinceSelected.selected.provinceCode,
        			provinceName: vm.provinceSelected.selected.provinceVname,
                	districtCode: obj.regionCode,
                	districtName: obj.regionName,
                	regionLevel: 2
        		};
	        	$state.go('boc.statistics');
    		}
    	}
    	
    	vm.someGroupTarget = function (item){
    		return item.decodeGroup;
        };
    	
    	vm.onChangeProvince = function() {
    		//Code here
    		searchAll();
    	}
    	
    	vm.onChangeTarget = function() {
    		//Code here
    		searchAll();
    	}
    	
    	vm.backPageShowInfo = function() {
    		$state.go('boc.statistics');
    	}
    	  
    	/*vm.resetSearchCondition = function() {
    		for (var i = 0; i < vm.listPolygon.length; i++) {
    			vm.listPolygon[i].setMap(null);
    		}
    		for (var j = 0; j < vm.listLabelForPolygon.length; j++) {
    			vm.listLabelForPolygon[j].set('text', '');
			}
    		initSearch(function(){
    			searchAll();
    		});
    	}*/
    	
    	function searchAll() {
    		vm.zoomMapOption = defaultZoomMap;
    	    vm.centerMapOption = defaultCenterMap;
    		for (var i = 0; i < vm.listPolygon.length; i++) {
    			vm.listPolygon[i].setMap(null);
    		}
    		vm.listPolygon = [];
    		for (var j = 0; j < vm.listLabelForPolygon.length; j++) {
    			vm.listLabelForPolygon[j].set('text', '');
    		}
    		vm.listLabelForPolygon = [];
    		if(vm.provinceSelected.selected == undefined || vm.provinceSelected.selected.provinceCode == 'ALL') {
				initializeListProvince(vm.targetSelected.selected.code);
				
				getInfomationWorstBOC('ALL', 'T');
                getInfomationBocData('ALL', vm.targetSelected.selected.codeGroup);
                //chart
                setParamForChart(null, null, vm.targetSelected.selected.code);
    		} else {
				initializeListDistrict(vm.provinceSelected.selected.provinceCode, vm.targetSelected.selected.code);
				
				getInfomationWorstBOC(vm.provinceSelected.selected.provinceCode, 'H');
                getInfomationBocData(vm.provinceSelected.selected.provinceCode, vm.targetSelected.selected.codeGroup);
                //chart
                setParamForChart(vm.provinceSelected.selected.provinceCode, null, vm.targetSelected.selected.code);
    		}
    		vm.groupNameShow = vm.targetSelected.selected.decodeGroup;
    	}
    	
    	// For maps
    	var mapStyles = [{
    		featureType: "all",
    		stylers: [{ visibility: "off" }]
        }];
    	
    	var backgroundColorMap = '#2e3e4e';//'#f5f5f5'
    	
    	vm.mapOptions = {
    		center: vm.centerMapOption,
    	    zoom: vm.zoomMapOption,
    		disableDefaultUI: true,
    		backgroundColor: backgroundColorMap,
    		styles: mapStyles
        };
    	
        vm.setZoomMessage = function (zoom) {
        };

        vm.onMapIdle = function() {
    	};
    	
    	//Draw custom control
        function CustomControl(controlDiv, map, typeLocation, id, name) {
    		// Set CSS for the control border.
            var controlUI = document.createElement('div');
            controlUI.id = id;
            controlUI.className = 'controlUI';
            controlUI.title = name;
            controlDiv.appendChild(controlUI);

            // Set CSS for the control interior.
            var controlText = document.createElement('div');
            controlText.className = 'truncate controlText';
            controlText.innerHTML = name;
            controlUI.appendChild(controlText);

    		// Setup the click event listeners
    		google.maps.event.addDomListener(controlUI, 'click', function() {
    			if(typeLocation == 'COUNTRY') {
    				showOneProvince(id, map);
    			} else if(typeLocation == 'DISTRICT') {
    				showOneDistrict(id, map);
    			}
    		});
    	};
    	function CustomControlTitle(controlDiv, name) {
    		// Set CSS for the control border.
            var controlUI = document.createElement('div');
            controlUI.className = 'controlUITitle';
            controlUI.title = name;
            controlDiv.appendChild(controlUI);

            // Set CSS for the control interior.
            var controlText = document.createElement('div');
            controlText.className = 'truncate controlTextTitle';
            controlText.innerHTML = name;
            controlUI.appendChild(controlText);
    	};
    	
    	function CustomControlInfoPanel(controlDiv, data) {
    		// Set CSS for the control border.
            var controlUI = document.createElement('div');
            controlUI.className = 'controlUIInfoPanel';
            controlUI.title = 'TÌNH HÌNH THỰC HIỆN CHỈ TIÊU';
            controlDiv.appendChild(controlUI);

            // Set CSS for the control interior.
            var controlText = document.createElement('div');
            controlText.innerHTML = '<div style="font-size: 13px;font-weight: 500;margin-bottom: 12px;">TÌNH HÌNH THỰC HIỆN CHỈ TIÊU</div>';
            controlText.className = 'customFontPanel';
            controlUI.appendChild(controlText);
            
            var controlText1 = document.createElement('div');
            controlText1.innerHTML = 'Số đơn vị đạt chỉ tiêu: ' + data.targetD + '/' + data.totalTargetD;
            controlText1.className = 'customFontPanel';
            controlUI.appendChild(controlText1);
            
            var controlText2 = document.createElement('div');
            controlText2.innerHTML = 'Số đơn vị không đạt chỉ tiêu M1: ' + data.targetM1 + '/' + data.totalTargetM1;
            controlText2.className = 'customFontPanel';
            controlUI.appendChild(controlText2);
            
            var controlText3 = document.createElement('div');
            controlText3.innerHTML = 'Số đơn vị không đạt chỉ tiêu M2: ' + data.targetM2 + '/' + data.totalTargetM2;
            controlText3.className = 'customFontPanel';
            controlUI.appendChild(controlText3);
            
            var controlText4 = document.createElement('div');
            controlText4.innerHTML = 'Số đơn vị không đạt chỉ tiêu M3: ' + data.targetM3 + '/' + data.totalTargetM3;
            controlText4.className = 'customFontPanel';
            controlUI.appendChild(controlText4);
    	}
        
        //Calculate center polygon
        function polygonCenter(poly) {
        	google.maps.Polygon.prototype.my_getBounds=function(){
        		var bounds = new google.maps.LatLngBounds();
        		this.getPath().forEach(function(element,index){bounds.extend(element)});
        		return bounds;
        	}
        	var lat = poly.my_getBounds().getCenter().lat();
        	var lng = poly.my_getBounds().getCenter().lng();
        	return (new google.maps.LatLng(lat, lng));
        };
        
        //Calculate coordinates polygon
        function polygonNorthOnlyLat(poly) {
        	google.maps.Polygon.prototype.my_getBounds=function(){
        		var bounds = new google.maps.LatLngBounds();
        		this.getPath().forEach(function(element,index){bounds.extend(element)});
        		return bounds;
        	}
        	var bounds = poly.my_getBounds();
        	var NE = bounds.getNorthEast();
        	var SW = bounds.getSouthWest();
        	// North West
        	var NW = new google.maps.LatLng(NE.lat(),SW.lng());
        	// South East
        	var SE = new google.maps.LatLng(SW.lat(),NE.lng());
        	
        	return NE.lat();
        };
        
        vm.colorForTableBocData = function(typeColor) {
        	var classColor = '';
        	switch(typeColor) {
	            case 'D':
	            	classColor = "color-green";
	                break;
	            case 'M1':
	            	classColor = "color-yellow";
	                break;
	            case 'M2':
	            	classColor = "color-orange";
	                break;
	            case 'M3':
	            	classColor = "color-red";
	                break;
	        }
        	return classColor;
        };
        
        function setColorForMap(typeColor) {
        	var colorArea = '#d5d5d5';
        	switch(typeColor) {
	            case 'D':
	            	colorArea = "#55ff00";
	                break;
	            case 'M1':
	            	colorArea = "#FFFF00";
	                break;
	            case 'M2':
	            	colorArea = "#ffa500";
	                break;
	            case 'M3':
	            	colorArea = "#FF0000";
	                break;
	        }
        	return colorArea;
        };
        
        function showOneDistrict(id, map) {
        	vm.districtCodeShowOneDistrict = id;
        	for (var i = 0; i < vm.listPolygonDistrictByCode.length; i++) {
    			if(id == vm.listPolygonDistrictByCode[i].districtCode) {
    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	                //fillColor: '#e5e5e5',
    	                strokeColor: '#0000FF',
    		            strokeWeight: 5,
    	            });
    				//FitBoundsOnePolygon(vm.listPolygonDistrictByCode[i].polygon, map);
    			} else {
    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	                strokeColor: '#475069',
    		            strokeWeight: 1,
    	            });
    			}
    		}
        };
        
        function showOneProvince(id, map) {
        	vm.provinceCodeShowOneProvince = id;
        	for (var i = 0; i < vm.listPolygonProvinceByCode.length; i++) {
    			if(id == vm.listPolygonProvinceByCode[i].provinceCode) {
    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	                //fillColor: '#e5e5e5',
    	                strokeColor: '#0000FF',
    		            strokeWeight: 5,
    	            });
    				//FitBoundsOnePolygon(vm.listPolygonProvinceByCode[i].polygon, map);
    			} else {
    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	                strokeColor: '#475069',
    		            strokeWeight: 1,
    	            });
    			}
    		}
        };
        
        //Fit Bounds for Multiple Polygons google Maps
        function FitBounds(shapes, map){
            var bounds= new google.maps.LatLngBounds();
            for (var i=0; i < shapes.length; i++){
            	var paths = shapes[i].getPaths();
                paths.forEach(function(path){
    	            var ar = path.getArray();
    	            for(var i=0, l = ar.length; i <l; i++){
    	            	bounds.extend(ar[i]);
    	            }
                })
             }
             map.fitBounds(bounds)
        }
      //Fit Bounds for one Polygons google Maps
        function FitBoundsOnePolygon(polygon, map){
            var bounds= new google.maps.LatLngBounds();
        	var paths = polygon.getPaths();
            paths.forEach(function(path){
                var ar = path.getArray();
                for(var i=0, l = ar.length; i <l; i++){
                	bounds.extend(ar[i]);
                }
            })
             map.fitBounds(bounds)
        }
        
    	function initializeListProvince(bocCode) {
    		var map = vm.myMap;
    		vm.listPolygonProvinceByCode = [];
    		
    		MapsService.getGeometryProvince({bocCode:bocCode}).$promise.then(function (response) {
    			var result = response.data;
    			//Set mapOptions
    	        map.setOptions({
    				disableDefaultUI: true,
    				backgroundColor: backgroundColorMap,
    				styles: mapStyles
    	        });
    			map.controls[google.maps.ControlPosition.RIGHT_CENTER].clear();
    			map.controls[google.maps.ControlPosition.TOP_LEFT].clear();
    			var customControlDiv = document.createElement('div');
    	    	customControlDiv.className = 'customControlDiv';
    			for (var i = 0; i < result.length; i++) {
                	drawOneCountry(map, result[i], result[i].levelAlarm);
                	if(result[i].districtCode !== 'K058008' && result[i].districtCode !== 'K077012') {
                		var customControl = new CustomControl(customControlDiv, map, 'COUNTRY', result[i].provinceCode, result[i].provinceVname);
                	}
    			}
    			
    			var customControlDivTitle = document.createElement('div');
    			customControlDivTitle.className = 'customControlDivTitle';
    			var customControlTitle = new CustomControlTitle(customControlDivTitle, 'Tỉnh');
    			
    			var customControlDivTotal = document.createElement('div');
    			customControlDivTotal.className = 'customControlDivTotal';
    			customControlDivTotal.appendChild(customControlDivTitle);
    			customControlDivTotal.appendChild(customControlDiv);
    			
    			//map.controls[google.maps.ControlPosition.RIGHT_CENTER].push(customControlDivTotal);
    			
    			MapsService.getStatisticsTargetsByBocCode({regionCode: null, bocCode: bocCode, regionLevel: 0}).$promise.then(function (response) {
    				var customControlDivInfoPanel = document.createElement('div');
        			customControlDivInfoPanel.className = 'customControlDivInfoPanel';
        			var customControlInfoPanel = new CustomControlInfoPanel(customControlDivInfoPanel, response.data[0]);
        			
        			map.controls[google.maps.ControlPosition.TOP_LEFT].push(customControlDivInfoPanel);
        		});
    			
    			FitBounds(vm.listPolygon, map);
    			
    	        /*vm.zoomMapOption = map.getZoom();
    	        vm.centerMapOption = map.getCenter();
    	        
    	        google.maps.event.addListener(map, 'zoom_changed', function() {
    			    if (map.getZoom() <= vm.zoomMapOption - 1) {
    			    	map.setOptions({draggable: false, center: vm.centerMapOption});
    			    } else {
    			    	map.setOptions({draggable: true});
    			    }
    			});
    	        google.maps.event.addListenerOnce(map, 'bounds_changed', function(event) {
    	            if ( this.getZoom() ){
    	                this.setZoom(this.getZoom());
    	            }
    	        });
    			google.maps.event.addListener(map, 'bounds_changed', function() {
    				if (map.getZoom() <= vm.zoomMapOption - 1) {
    					map.setZoom(vm.zoomMapOption);
    				}
    			});*/
            }, function (err) {
            });
        };
        function initializeListDistrict(provinceCode, bocCode) {
        	vm.districtCodeShowOneDistrict = null;
        	vm.listPolygonDistrictByCode = [];
    		var map = vm.myMap;
    		
    		MapsService.getGeometryDistrict({provinceCode: provinceCode, bocCode:bocCode}).$promise.then(function (response) {
                var result = response.data;
    			//Set mapOptions
    	        map.setOptions({
    				disableDefaultUI: true,
    				backgroundColor: backgroundColorMap,
    				styles: mapStyles
    			});
    			map.controls[google.maps.ControlPosition.RIGHT_CENTER].clear();
    			map.controls[google.maps.ControlPosition.TOP_LEFT].clear();
    			var customControlDiv = document.createElement('div');
    	    	customControlDiv.className = 'truncate customControlDiv';
    			for (var i = 0; i < result.length; i++) {
    				drawOneDistrict(map, result[i], result[i].levelAlarm);
                	var customControl = new CustomControl(customControlDiv, map, 'DISTRICT', result[i].districtCode, result[i].districtName);
    			}
    			var customControlDivTitle = document.createElement('div');
    			customControlDivTitle.className = 'customControlDivTitle';
    			var customControlTitle = new CustomControlTitle(customControlDivTitle, 'Huyện');
    			
    			var customControlDivTotal = document.createElement('div');
    			customControlDivTotal.className = 'customControlDivTotal';
    			customControlDivTotal.appendChild(customControlDivTitle);
    			customControlDivTotal.appendChild(customControlDiv);
    			
    			//map.controls[google.maps.ControlPosition.RIGHT_CENTER].push(customControlDivTotal);
    			
    			MapsService.getStatisticsTargetsByBocCode({regionCode: provinceCode, bocCode:bocCode, regionLevel: 1}).$promise.then(function (response) {
        			var customControlDivInfoPanel = document.createElement('div');
        			customControlDivInfoPanel.className = 'customControlDivInfoPanel';
        			var customControlInfoPanel = new CustomControlInfoPanel(customControlDivInfoPanel, response.data[0]);
        			
        			map.controls[google.maps.ControlPosition.TOP_LEFT].push(customControlDivInfoPanel);
        		});
    			
    			FitBounds(vm.listPolygon, map);
    			
    			//map.setOptions({draggable: false});
    			
    			// Set dragable zoom
    	        /*vm.zoomMapOption = map.getZoom();
    	        vm.centerMapOption = map.getCenter();
    	        google.maps.event.addListener(map, 'zoom_changed', function() {
    			    if (map.getZoom() <= vm.zoomMapOption - 1) {
    			    	map.setOptions({draggable: false, center: vm.centerMapOption});
    			    } else {
    			    	map.setOptions({draggable: true});
    			    }
    			});
    	        google.maps.event.addListenerOnce(map, 'bounds_changed', function(event) {
    	            if ( this.getZoom() ){
    	                this.setZoom(this.getZoom());
    	            }
    	        });
    			google.maps.event.addListener(map, 'bounds_changed', function() {
    				if (map.getZoom() <= vm.zoomMapOption) {
    					map.setZoom(vm.zoomMapOption);
    				}
    			});*/
           	});
        };
        function drawOneCountry(map, jsonData, typeColor) {
        	try {
        		var colorArea = setColorForMap(typeColor);
        		var geometryLatLng = JSON.parse(jsonData.geometry);
    	    	//COORDS
    	        var brussels = [];
    			for (var i = 0; i < geometryLatLng.length; i++) {
    				brussels.push(new google.maps.LatLng(geometryLatLng[i].lat, geometryLatLng[i].lng));
    			}
    	        var BrusselsHightlight;
    	        //DRAW THE POLYGON OR POLYLINE
    	        BrusselsHightlight = new google.maps.Polygon({
    	            paths: brussels,
    	            strokeColor: '#475069',
    	            strokeOpacity: 1,
    	            strokeWeight: 1,
    	            fillColor: colorArea,
    	            fillOpacity: 1
    	        });
    	        BrusselsHightlight.setMap(map);
    	        vm.listPolygon.push(BrusselsHightlight);
    	        vm.listPolygonProvinceByCode.push({
            		provinceCode : jsonData.provinceCode,
            		polygon : BrusselsHightlight
            	});
    			// STEP 3: Listen for clicks on the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'click', function (event) {
    	            //infowindow.open(map, BrusselsHightlight);
    	        	$localStorage.valueForMapStatistics = {
	    				bocCode: vm.targetSelected.selected.code,
	        			provinceCode: jsonData.provinceCode,
	        			provinceName: jsonData.provinceVname,
	                	districtCode: null,
	                	districtName: null,
	                	regionLevel: 1
	        		};
    	        	$state.go('boc.statistics');
    	        }); 
    	        // STEP 4: Listen for when the mouse hovers over the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'mouseover', function (event) {
    	            // Within the event listener, "this" refers to the polygon which
    	            // received the event.
    	            this.setOptions({
    	                strokeColor: '#475069',
    	                fillColor: '#e5e5e5',
    	                strokeWeight: 1
    	            });
    	            for (var i = 0; i < vm.listPolygonProvinceByCode.length; i++) {
    	    			if(vm.provinceCodeShowOneProvince == vm.listPolygonProvinceByCode[i].provinceCode) {
    	    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	    	                //fillColor: '#e5e5e5',
    	    	                strokeColor: '#0000FF',
    	    		            strokeWeight: 5,
    	    	            });
    	    				//FitBoundsOnePolygon(vm.listPolygonProvinceByCode[i].polygon, map);
    	    			} else {
    	    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	    	                strokeColor: '#475069',
    	    		            strokeWeight: 1,
    	    	            });
    	    			}
    	    		}
    	            infowindow.open(map, BrusselsHightlight);
    	        });
    	        // STEP 5: Listen for when the mouse stops hovering over the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'mouseout', function (event) {
    	            this.setOptions({
    	                strokeColor: '#475069',
    	                fillColor: colorArea,
    	                strokeWeight: 1
    	            });
    	            for (var i = 0; i < vm.listPolygonProvinceByCode.length; i++) {
    	    			if(vm.provinceCodeShowOneProvince == vm.listPolygonProvinceByCode[i].provinceCode) {
    	    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	    	                //fillColor: '#e5e5e5',
    	    	                strokeColor: '#0000FF',
    	    		            strokeWeight: 5,
    	    	            });
    	    				//FitBoundsOnePolygon(vm.listPolygonProvinceByCode[i].polygon, map);
    	    			} else {
    	    				vm.listPolygonProvinceByCode[i].polygon.setOptions({
    	    	                strokeColor: '#475069',
    	    		            strokeWeight: 1,
    	    	            });
    	    			}
    	    		}
    	            infowindow.close(map, BrusselsHightlight);
    	        });
    	        if(jsonData.actual == undefined) {
    	        	jsonData.actual = '-';
    	        } else {
	        		jsonData.actual = (parseFloat(jsonData.actual).toFixed(2)+"").replace(/\B(?=(\d{3})+\b)/g, ",");
    	        }
    	        if(jsonData.target == undefined) {
    	        	jsonData.target = '-';
    	        } else {
	        		jsonData.target = (parseFloat(jsonData.target).toFixed(2)+"").replace(/\B(?=(\d{3})+\b)/g, ",");
    	        }
    	        var contentString = '<div id="content">'+
    		        '<div style="font-weight: 600;font-size: 15px;color: #333;">'+
    		        jsonData.provinceVname +
    		        '</div>'+
    		        '<div style="font-weight: 500;font-size: 13px;color: #333;">Giá trị: '+
    		        jsonData.actual +
    		        '</div>'+
    		        '<div style="font-weight: 500;font-size: 13px;color: #333;">Mục tiêu: '+
    		        jsonData.target +
    		        '</div>'+
    	        '</div>';
    	        
    	        var northString = jsonData.northLoc.split(',');
    	        var northLoc = new google.maps.LatLng(northString[0], northString[1]);
    	        
    	        /*var paths = BrusselsHightlight.getPath().getArray();
    	        for (var i = 0; i < paths.length; i++) {
    				if(polygonNorthOnlyLat(BrusselsHightlight) == paths[i].lat()) {
    					northLoc = new google.maps.LatLng(paths[i].lat(), paths[i].lng());
    				}
    			}*/
    	        
    	        var infowindow = new google.maps.InfoWindow({
    	        	disableAutoPan: true,
    	            content: contentString,
    	            position: northLoc
    	        });
    	        infowindow.setZIndex(99999);
    	        
    	        var centerString = jsonData.centerLoc.split(',');
    	        var centerLoc = new google.maps.LatLng(centerString[0], centerString[1]);
    	        
    	        var mapLabel = new MapLabel({
    	            text: jsonData.provinceVname,
    	            position: centerLoc,
    	            map: map,
    	            fontSize: 9,
    	            strokeWeight: 0,
    	            zIndex: 10000,
    	            align: 'center',
    	            paneType: 'floatPane'
    	        });
    	        vm.listLabelForPolygon.push(mapLabel);
    	        
    	        google.maps.event.addListener(infowindow, 'domready', function(){
    	            $(".gm-style-iw").next("div").hide();
    	        });
        	}
        	catch(err) {
        	   console.log('parse json loi: ' + err);
        	}
        };
        
        function drawOneDistrict(map, jsonData, typeColor) {
        	try {
        		var colorArea = setColorForMap(typeColor);
        		var geometryLatLng = JSON.parse(jsonData.geometry);
    	    	//COORDS
    	        var brussels = [];
    			for (var i = 0; i < geometryLatLng.length; i++) {
    				brussels.push(new google.maps.LatLng(geometryLatLng[i].lat, geometryLatLng[i].lng));
    			}
    	        var BrusselsHightlight;
    	        //DRAW THE POLYGON OR POLYLINE
    	        BrusselsHightlight = new google.maps.Polygon({
    	            paths: brussels,
    	            strokeColor: '#475069',
    	            strokeOpacity: 1,
    	            strokeWeight: 1,
    	            fillColor: colorArea,
    	            fillOpacity: 1
    	        });
    	        BrusselsHightlight.setMap(map);
    	        vm.listPolygon.push(BrusselsHightlight);
    	        vm.listPolygonDistrictByCode.push({
    	        	districtCode : jsonData.districtCode,
            		polygon : BrusselsHightlight
            	});
    			// STEP 3: Listen for clicks on the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'click', function (event) {
	        		//infowindow.open(map, BrusselsHightlight);
    	        	$localStorage.valueForMapStatistics = {
	    				bocCode: vm.targetSelected.selected.code,
	        			provinceCode: jsonData.provinceCode,
	        			provinceName: jsonData.provinceVname,
	                	districtCode: jsonData.districtCode,
	                	districtName: jsonData.districtName,
	                	regionLevel: 2
	        		};
    	        	$state.go('boc.statistics');
    	        }); 
    	        // STEP 4: Listen for when the mouse hovers over the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'mouseover', function (event) {
    	            // Within the event listener, "this" refers to the polygon which
    	            // received the event.
    	            this.setOptions({
    	                strokeColor: '#475069',
    	                fillColor: '#e5e5e5',
    	                strokeWeight: 1
    	            });
    	            for (var i = 0; i < vm.listPolygonDistrictByCode.length; i++) {
    	    			if(vm.districtCodeShowOneDistrict == vm.listPolygonDistrictByCode[i].districtCode) {
    	    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	    	                //fillColor: '#e5e5e5',
    	    	                strokeColor: '#0000FF',
    	    		            strokeWeight: 5,
    	    	            });
    	    				//FitBoundsOnePolygon(vm.listPolygonDistrictByCode[i].polygon, map);
    	    			} else {
    	    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	    	                strokeColor: '#475069',
    	    		            strokeWeight: 1,
    	    	            });
    	    			}
    	    		}
    	            infowindow.open(map, BrusselsHightlight);
    	        });
    	        // STEP 5: Listen for when the mouse stops hovering over the polygon.
    	        google.maps.event.addListener(BrusselsHightlight, 'mouseout', function (event) {
    	            this.setOptions({
    	                strokeColor: '#475069',
    	                fillColor: colorArea,
    	                strokeWeight: 1
    	            });
    	            for (var i = 0; i < vm.listPolygonDistrictByCode.length; i++) {
    	    			if(vm.districtCodeShowOneDistrict == vm.listPolygonDistrictByCode[i].districtCode) {
    	    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	    	                //fillColor: '#e5e5e5',
    	    	                strokeColor: '#0000FF',
    	    		            strokeWeight: 5,
    	    	            });
    	    				//FitBoundsOnePolygon(vm.listPolygonDistrictByCode[i].polygon, map);
    	    			} else {
    	    				vm.listPolygonDistrictByCode[i].polygon.setOptions({
    	    	                strokeColor: '#475069',
    	    		            strokeWeight: 1,
    	    	            });
    	    			}
    	    		}
    	            infowindow.close(map, BrusselsHightlight);
    	        });
    	        
    	        if(jsonData.actual == undefined) {
    	        	jsonData.actual = '-';
    	        } else {
	        		jsonData.actual = (parseFloat(jsonData.actual).toFixed(2)+"").replace(/\B(?=(\d{3})+\b)/g, ",");
    	        }
    	        if(jsonData.target == undefined) {
    	        	jsonData.target = '-';
    	        } else {
	        		jsonData.target = (parseFloat(jsonData.target).toFixed(2)+"").replace(/\B(?=(\d{3})+\b)/g, ",");
    	        }
    	        var contentString = '<div id="content">'+
    		        '<div style="font-weight: 600;font-size: 15px;color: #333;">'+
    		        jsonData.districtName +
    		        '</div>'+
    		        '<div style="font-weight: 500;font-size: 13px;color: #333;">Giá trị: '+
    		        jsonData.actual +
    		        '</div>'+
    		        '<div style="font-weight: 500;font-size: 13px;color: #333;">Mục tiêu: '+
    		        jsonData.target +
    		        '</div>'+
    	        '</div>';
    	        
    	        //var northString = jsonData.northLoc.split(',');
    	        //var northLoc = new google.maps.LatLng(northString[0], northString[1]);
    	        var northLoc = null;
    	        var paths = BrusselsHightlight.getPath().getArray();
    	        for (var i = 0; i < paths.length; i++) {
    				if(polygonNorthOnlyLat(BrusselsHightlight) == paths[i].lat()) {
    					northLoc = new google.maps.LatLng(paths[i].lat(), paths[i].lng());
    				}
    			}
    	        var infowindow = new google.maps.InfoWindow({
    	        	disableAutoPan: true,
    	            content: contentString,
    	            position: northLoc
    	        });
    	        infowindow.setZIndex(99999);
    	        
    	        var centerString = jsonData.centerLoc.split(',');
    	        var centerLoc = new google.maps.LatLng(centerString[0], centerString[1]);
    	        
    	        var mapLabel = new MapLabel({
    	            text: jsonData.districtName,
    	            position: centerLoc,
    	            map: map,
    	            fontSize: 9,
    	            strokeWeight: 0,
    	            align: 'center',
    	            paneType: 'floatPane'
    	        });
    	        vm.listLabelForPolygon.push(mapLabel);
    	        
    	        google.maps.event.addListener(infowindow, 'domready', function(){
    	            $(".gm-style-iw").next("div").hide();
    	        });
        	}
        	catch(err) {
        	   console.log('parse json loi: ' + err);
        	}
        };
        
        function getInfomationWorstBOC(regionCode, typeArea) {
        	if(typeArea == 'T') {
        		vm.typeAreaName = $translate.instant('maps.labelInput.province');
        		vm.typeTitleTableAreaName = $translate.instant('maps.title.warningProvince');
        		vm.typeTitleAreaName = $translate.instant('maps.title.warningSeriousProvince');
        	}
        	if(typeArea == 'H') {
        		vm.typeAreaName = $translate.instant('maps.labelInput.district');
        		vm.typeTitleTableAreaName = $translate.instant('maps.title.warningDistrict');
        		vm.typeTitleAreaName = $translate.instant('maps.title.warningSeriousDistrict');
        	}
        	//Get table
        	MapsService.getMonthTitle().$promise.then(function (response) {
        		vm.monthTitle = response.data;
            }, function (err) {
            });
        	MapsService.getTopTenWorstBOC({regionCode:regionCode}).$promise.then(function (response) {
        		vm.topTenWorstBOC = response.data;
        		for (var i = 0; i < vm.topTenWorstBOC.length; i++) {
        			vm.topTenWorstBOC[i].failKPI0totalKPI0 = vm.topTenWorstBOC[i].failKPI0 + '/' + vm.topTenWorstBOC[i].totalKPI0;
        			vm.topTenWorstBOC[i].failKPI1totalKPI1 = vm.topTenWorstBOC[i].failKPI1 + '/' + vm.topTenWorstBOC[i].totalKPI1;
        			vm.topTenWorstBOC[i].failKPI2totalKPI2 = vm.topTenWorstBOC[i].failKPI2 + '/' + vm.topTenWorstBOC[i].totalKPI2;
        			vm.topTenWorstBOC[i].failKPI3totalKPI3 = vm.topTenWorstBOC[i].failKPI3 + '/' + vm.topTenWorstBOC[i].totalKPI3;
				}
        		vm.tableTopTenWorstBOC = new NgTableParams({count: vm.topTenWorstBOC.length}, { counts: [], dataset: vm.topTenWorstBOC});
        		//Get bad employee
        		if(vm.topTenWorstBOC.length > 1) {
        			if(vm.topTenWorstBOC[0].regionCode != undefined) {
        				MapsService.getBadEmployeeWorstBOC({regionCode:vm.topTenWorstBOC[0].regionCode}).$promise.then(function (response) {
        					if(response.data.length == 0) {
        						$("#idTableFrame2Maps").css('max-height', '61%');
        					}
        					if(response.data.length == 1) {
        						$("#idTableFrame2Maps").css('max-height', '49%');
        					}
        					if(response.data.length == 2) {
        						$("#idTableFrame2Maps").css('max-height', '39%');
        					}
        					if(response.data.length >= 1) {
        						vm.badEmployeeWorstBOCFirst = response.data[0];
                        		vm.viewImageFirstEmployee = vm.getUrlImageByFileId(vm.badEmployeeWorstBOCFirst.fileId);
        						vm.showImageFirstEmployee = true;
        					} else {
        						vm.showImageFirstEmployee = false;
        					}
        					if(response.data.length >= 2) {
        						vm.badEmployeeWorstBOCSecond = response.data[1];
                        		vm.viewImageSecondEmployee = vm.getUrlImageByFileId(vm.badEmployeeWorstBOCSecond.fileId);
        						vm.showImageSecondEmployee = true;
        					} else {
        						vm.showImageSecondEmployee = false;
        					}
                        }, function (err) {
                        });
        			}
        		}
            }, function (err) {
            });
        }
        
        vm.getColumnTable = function(index) {
        	if(vm.monthTitle.length > 0) {
        		return vm.monthTitle[index].monthName;
        	} else {
        		return '';
        	}
        }
        
        function getInfomationBocData(regionCode, bocCodeGroup) {
        	//Get table
        	vm.bocTitleHeader = [];
        	MapsService.getBocTitle({bocCodeGroup:bocCodeGroup}).$promise.then(function (response) {
        		vm.bocTitle = response.data;
        		if (vm.bocTitle.length > 0) {
               		for (var i = 0; i < 3 * vm.bocTitle.length; i++) {
               			vm.bocTitleHeader.push(i);
               		}
               	} 
            }, function (err) {
            });
        	vm.bocData = [];
        	MapsService.getBocData({regionCode:regionCode, bocCodeGroup:bocCodeGroup}).$promise.then(function (response) {
        		vm.bocData = response.data;
            }, function (err) {
            });
        }
        
        function setParamForChart(provinceCode, districtCode, bocCode) {
        	vm.provinceCodeChoose = provinceCode;
            vm.districtCodeChoose = districtCode;
            vm.bocCodeChoose = bocCode;
            vm.tabChartCurent = bocCode;
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
        
        vm.hideChart = function() {
        	vm.openTabPage('SHOW-MAPS-CHART', 'SHOW-MAPS');
        }
        
    	vm.openTabPage = function(pageId, tabId) {
    		$("#" + pageId + " .tab-content-page").removeClass("active");
    		$("#" + pageId + " #" + tabId).addClass("active");
    		if(tabId == 'SHOW-CHART') {
    			MapsService.getListBocModule().$promise.then(function (response) {
        			vm.listBocModule = removeDuplicatesArrayObject(response.data, "moduleGroup");
        			if(vm.listBocModule.length > 0) {
        				if(vm.tabChartCurent != '') {
            				vm.openTabChart('chart-boc-page', vm.tabChartCurent);
        				} else {
        					vm.tabChartCurent = vm.listBocModule[0].moduleGroup;
            				vm.openTabChart('chart-boc-page', vm.tabChartCurent);
        				}
        			}
                }, function (err) {
                });
    		}
    	}
    	
    	vm.nextTabChart = function(pageId) {
    		var curentTab = $("#" + pageId + " .tab button.active");
    		var listTab = $("#chart-boc-page .tab button[class^='btn']");
    		
    		for (var i = 0; i < listTab.length; i++){
    			if (listTab[i].className == $(curentTab).attr("class")){
    				if(i < listTab.length - 1) {
    					var nextTab = $(listTab[i + 1]);
    					vm.openTabChart('chart-boc-page', $(nextTab).attr("class").replace("btn-", ""));
    				} else {
    					vm.openTabChart('chart-boc-page', vm.tabChartCurent);
    				}
    				break;
    			}
    		}
    	}
    	
    	vm.openTabChart = function(pageId, tabId) {
    		if ($("#" + pageId + " .tab button.active").length > 0) {
    			$("#" + pageId + " .tab button.active").removeClass("active");
    			$("#" + pageId + " .tab-content").removeClass("active");
    			
    			$("#" + pageId + " .tab button.btn-" + tabId).addClass("active");
    			$("#" + pageId + " #" + tabId).addClass("active");
    			$("#" + pageId + " .tab button.btn-" + tabId).focus();
    			$("#" + pageId + " #" + tabId).focus();
    		} else {
    			$timeout(function() {
    				$("#" + pageId + " .tab button.active").removeClass("active");
        			$("#" + pageId + " .tab-content").removeClass("active");
        			
        			$("#" + pageId + " .tab button.btn-" + tabId).addClass("active");
        			$("#" + pageId + " #" + tabId).addClass("active");
        			$("#" + pageId + " .tab button.btn-" + tabId).focus();
        			$("#" + pageId + " #" + tabId).focus();
    		    }, 500);
    		}
			
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
                
                objSearch.provinceCode = vm.provinceCodeChoose;
                objSearch.districtCode = vm.districtCodeChoose;
                
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
