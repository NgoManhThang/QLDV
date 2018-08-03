// code style: https://github.com/johnpapa/angular-styleguide 
var scopeHolder;
(function() {
	'use strict';
	angular.module('app')
			.controller('DashboardController', DashboardController);

	DashboardController.$inject = [ '$scope', '$rootScope', '$controller',
			'$state', '$element', '$translate', '$sessionStorage',
			'$localStorage', 'DashboardService' ];
	function DashboardController($scope, $rootScope, $controller, $state,
			$element, $translate, $sessionStorage, $localStorage,
			DashboardService) {
		var vm = this;
		scopeHolder = $scope;

		(function initController() {
			// instantiate base controller
			$controller('BaseController', {
				vm : vm
			});
			$rootScope.$broadcast('$elementLoadJavascript', $element);
			clickDemo();
		})();

		function clickDemo() {
			var map; // Global declaration of the map
			var iw = new google.maps.InfoWindow(); // Global declaration of the infowindow
			var lat_longs = new Array();
			var markers = new Array();
			var drawingManager;
			function initialize() {
				var myLatlng = new google.maps.LatLng(16.2315000483959, 105.84750731250006);
				var myOptions = {
					zoom : 5,
					center : myLatlng,
					mapTypeId : google.maps.MapTypeId.ROADMAP
				}
				map = new google.maps.Map(
						document.getElementById("map_canvas"), myOptions);
				drawingManager = new google.maps.drawing.DrawingManager(
						{
							drawingMode : google.maps.drawing.OverlayType.POLYGON,
							drawingControl : true,
							drawingControlOptions : {
								position : google.maps.ControlPosition.TOP_CENTER,
								drawingModes : [ google.maps.drawing.OverlayType.POLYGON ]
							},
							polygonOptions : {
								editable : true
							}
						});
				drawingManager.setMap(map);

				google.maps.event.addListener(drawingManager,
						"overlaycomplete", function(event) {
							var newShape = event.overlay;
							newShape.type = event.type;
						});

				google.maps.event.addListener(drawingManager,
						"overlaycomplete", function(event) {
							overlayClickListener(event.overlay);
							var polygonBounds = event.overlay.getPath();
							var coordinates = [];
							for(var i = 0 ; i < polygonBounds.length ; i++){
							    coordinates.push({"lat": polygonBounds.getAt(i).lat(), "lng": polygonBounds.getAt(i).lng()});
							} 
							$('#textareavertices').val(JSON.stringify(coordinates));
						});
			}
			function overlayClickListener(overlay) {
				google.maps.event.addListener(overlay, "mouseup", function(
						event) {
					var polygonBounds = overlay.getPath();
					var coordinates = [];
					for(var i = 0 ; i < polygonBounds.length ; i++){
					    coordinates.push({"lat": polygonBounds.getAt(i).lat(), "lng": polygonBounds.getAt(i).lng()});
					} 
					$('#textareavertices').val(JSON.stringify(coordinates));
				});
			}
			initialize();
			$(function(){
			    $('#reset').click(function(){
			    	initialize();
			    	$('#textareavertices').val("");
			    });
			});
		}
	}
})();
