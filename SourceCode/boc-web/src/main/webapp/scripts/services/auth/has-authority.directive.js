(function() {
    'use strict';

    angular
        .module('app')
        .directive('hasAuthority', hasAuthority);

    hasAuthority.$inject = ['$localStorage'];

    function hasAuthority($localStorage) {
        var directive = {
            restrict: 'A',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs) {
            var authority = attrs.hasAuthority.replace(/\s+/g, '');

            var setVisible = function () {
                    element.removeClass('hidden');
                },
                setHidden = function () {
                    element.addClass('hidden');
                },
                defineVisibility = function (reset) {

                    if (reset) {
                        setVisible();
                    }
                    
                    if($localStorage.user != null) {
                		var hasAuthority = $localStorage.user.listRole && $localStorage.user.listRole.indexOf(authority) !== -1;
                		if (hasAuthority) {
                            setVisible();
                        } else {
                            setHidden();
                        }
                    } else {
                		setHidden();
                	}
                };

            if (authority.length > 0) {
                defineVisibility(true);

                scope.$watch(function() {
                	if($localStorage.user != null) {
                		return true;
                	} else {
                		return false;
                	}
                }, function() {
                    defineVisibility(true);
                });
            }
        }
    }
})();
