(function() {
  'use strict';
  angular
    .module('app')
    .directive('itsAsideMenu', itsAsideMenu);

  	itsAsideMenu.$inject = [];
    function itsAsideMenu() {
    		var directive = {};
        directive.restrict = 'E';
        directive.template = "<li ui-sref-active=\"active\"><a ui-sref=\"app.inbox.list\"><span class=\"nav-text\">Inbox</span></a></li>";

////        directive.scope = {
////           sinhvien : "=ten"
////        }
//		 
////        directive.compile = function(element, attributes) {
////           element.css("border", "1px solid #cccccc");
////
////           var hamLienKet = function($scope, element, attributes) {
////              element.html("Sinh vien: <b>"+$scope.sinhvien.ten +"</b> , MSSV: <b>"+$scope.sinhvien.mssv+"</b><br/>");
////              element.css("background-color", "#ff00ff");
////           }
////
////           return hamLienKet;
////        }

        return directive;
    }
    
    
})();
