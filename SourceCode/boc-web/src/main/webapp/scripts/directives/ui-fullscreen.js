(function() {
  'use strict';
  angular
    .module('app')
    .directive('uiFullscreen', uiFullscreen)
  	.directive('uiFullDiv', uiFullDiv);
  
    uiFullscreen.$inject = ['$ocLazyLoad', '$document'];
    function uiFullscreen($ocLazyLoad, $document) {
      var directive = {
          restrict: 'AC',
          link: link
      };
      return directive;
      
      function link(scope, el, attr) {
          el.addClass('hide');
          $ocLazyLoad.load('./libs/jquery/screenfull/dist/screenfull.min.js').then(function(){
            if (screenfull.enabled) {
              el.removeClass('hide');
            } else{
              return;
            }
            el.bind('click', function(){
              var target;
              attr.target && ( target = angular.element(attr.target)[0] );
              screenfull.toggle(target);
            });

            var body = angular.element($document[0].body);
            $document.on(screenfull.raw.fullscreenchange, function () {
              if(screenfull.isFullscreen){
                body.addClass('fullscreen');
              }else{
                body.removeClass('fullscreen');
              }
            });
          });
      }
    }
    
    uiFullDiv.$inject = ['$document', '$window'];
    function uiFullDiv($document, $window) {
      var directive = {
          restrict: 'AC',
          link: link
      };
      return directive;
      
      function link(scope, el, attr) {
          
    	  	el.bind('click', function(){
    	  		// Normal
    	  		if (el.closest('.box').parent().hasClass('is-full-div')) {
        	  		// Hide all
    	            $('.box').parent().show();
    	            el.closest('.box').parent().addClass("col-sm-6");
    	  			el.closest('.box').parent().removeClass("col-sm-12");
    	  			el.closest('.box').parent().removeClass("is-full-div");
    	  			el.closest('.box').parent()[0].style.zIndex = "0";
    	  		} else {
        	  		// Hide all
    	            $('.box').parent().hide();
    	            el.closest('.box').parent().show();
    	            el.closest('.box').parent().removeClass("col-sm-6");
    	  			el.closest('.box').parent().addClass("col-sm-12");
    	  			el.closest('.box').parent().addClass("is-full-div");
    	  			el.closest('.box').parent()[0].style.zIndex = "999";
    	  			// Calculate heigh
    	  			console.log($(window).height());
    	  		}
        });
      }
    }
})();
