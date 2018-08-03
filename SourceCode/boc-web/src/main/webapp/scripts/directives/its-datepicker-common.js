//code style: https://github.com/johnpapa/angular-styleguide
//Create by: Tung,Le Thanh - ITSOL.vn

(function() {
	'use strict';
	angular.module('app').directive('itsDatepicker', itsDatepicker);

	itsDatepicker.$inject = [];
	function itsDatepicker() {
		return {
			restrict : 'E',
			// replace: true,
			scope : {
				start : '=startVar',
				end : '=endVar',
				fOnClick : '&',
			},
			templateUrl : 'scripts/directives/its-datepicker-template.html',
			link : function(scope, element, attr) {

				scope.start = new Date();
				scope.end = new Date();
				scope.format = "dd/MM/yyyy";

				scope.minStartDate = 0;
				scope.maxStartDate = scope.end;
				scope.minEndDate = scope.start;
				scope.maxEndDate = scope.end;

				scope.$watch('start', function(start) {
					scope.minEndDate = start;
					scope.validStartGt = true;
					scope.validStart = true;
					if (start != null) {
						var st = Date.parse(start);

						var en = Date.parse(scope.end);

						if (isNaN(st)) {
							scope.validStart = false;
							return;
						} else {
							scope.validStart = true;
						}

						var dateStart = start.getDay() + "" + start.getMonth() + "" + start.getFullYear();
						var dateEnd = scope.end.getDay() + "" + scope.end.getMonth() + "" + scope.end.getFullYear();

						if(dateStart > dateEnd) {
							scope.validStartGt = false;
							return;
						}

					} else {
						scope.validStart = false;
					}

				});
				scope.$watch('end', function(end) {
					scope.maxStartDate = end;

					scope.validEndGt = true;
					scope.validEnd = true;
					if (end != null) {
						var st = Date.parse(scope.start);
						var en = Date.parse(end);

						if (isNaN(en)) {
							scope.validEnd = false;
						} else {
							scope.validEnd = true;
						}

						var dateStart = scope.start.getDay() + "" + scope.start.getMonth() + "" + scope.start.getFullYear();
						var dateEnd = end.getDay() + "" + end.getMonth() + "" + end.getFullYear();

						if(dateStart > dateEnd) {
							scope.validEndGt = false;
							return;
						}
					} else {
						scope.validEnd = false;
					}
				});

				scope.openStart = function($event) {
					scope.startOpened = true;
				};

				scope.openEnd = function($event) {
					scope.endOpened = true;
				};

				scope.changeStart = function(start) {

				};
				scope.changeStart();

				scope.dateOptions = {
					'year-format' : "'yy'",
					'starting-day' : 1
				};
			}
		}



	}


})();