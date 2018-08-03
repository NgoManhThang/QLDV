//code style: https://github.com/johnpapa/angular-styleguide
//Create by: Tung,Le Thanh - ITSOL.vn

angular.module('ui.multiselect', [])

  //from bootstrap-ui typeahead parser
  .factory('optionParser', ['$parse', function ($parse) {

    //                      00000111000000000000022200000000000000003333333333333330000000000044000
    var TYPEAHEAD_REGEXP = /^\s*(.*?)(?:\s+as\s+(.*?))?\s+for\s+(?:([\$\w][\$\w\d]*))\s+in\s+(.*)$/;

    return {
      parse: function (input) {

        var match = input.match(TYPEAHEAD_REGEXP), modelMapper, viewMapper, source;
        if (!match) {
          throw new Error(
            "Expected typeahead specification in form of '_modelValue_ (as _label_)? for _item_ in _collection_'" +
              " but got '" + input + "'.");
        }

        return {
          itemName: match[3],
          source: $parse(match[4]),
          viewMapper: $parse(match[2] || match[1]),
          modelMapper: $parse(match[1])
        };
      }
    };
  }])

  .directive('multiselect', ['$parse', '$document', '$compile', 'optionParser' ,'$translate',

    function ($parse, $document, $compile, optionParser, $translate) {
      return {
        restrict: 'E',
        require: 'ngModel',
        link: function (originalScope, element, attrs, modelCtrl) {
          var exp = attrs.options,
            parsedResult = optionParser.parse(exp),
            isMultiple = attrs.multiple ? true : false,
            required = false,
            scope = originalScope.$new(),
            changeHandler = attrs.change || anguler.noop;

          scope.asTree = attrs.asTree;
          scope.items = [];
          scope.header = $translate.instant(attrs.tile);
          scope.multiple = isMultiple;
          scope.disabled = false;
          scope.index = attrs.index;

          originalScope.$on('$destroy', function () {
            scope.$destroy();
          });

          var disable = attrs.disable;

          if(disable) {

          } else {

          }

          var popUpEl = angular.element('<multiselect-popup></multiselect-popup>');

          //required validator
          if (attrs.required || attrs.ngRequired) {
            required = true;
          }
          attrs.$observe('required', function(newVal) {
            required = newVal;
          });

          //watch disabled state
          scope.$watch(function () {

        	  return $parse(attrs.disabled)(originalScope);
          }, function (newVal) {
            scope.disabled = newVal;
          });

          //watch single/multiple state for dynamically change single to multiple
          scope.$watch(function () {
            return $parse(attrs.multiple)(originalScope);
          }, function (newVal) {
            isMultiple = newVal || false;
          });

          //watch option changes for options that are populated dynamically
          scope.$watch(function () {
            return parsedResult.source(originalScope);
          }, function (newVal) {
            if (angular.isDefined(newVal)){
                parseModel();
            }
          });

          //watch model change
          scope.$watch(function () {
        	  getHeaderText($translate.instant('global.common.toolbar-selected'));
            return modelCtrl.$modelValue;
          }, function (newVal, oldVal) {
            //when directive initialize, newVal usually undefined. Also, if model value already set in the controller
            //for preselected list then we need to mark checked in our scope item. But we don't want to do this every time
            //model changes. We need to do this only if it is done outside directive scope, from controller, for example.
            if (angular.isDefined(newVal)) {
              markChecked(newVal);
              scope.$eval(changeHandler);
            }
            getHeaderText($translate.instant('global.common.toolbar-selected'));
            modelCtrl.$setValidity('required', scope.valid());
          }, true);

          function parseModel() {
            scope.items.length = 0;
            var model = parsedResult.source(originalScope);
            for (var i = 0; i < model.length; i++) {

              var local = {};
              local[parsedResult.itemName] = model[i];
              scope.items.push({
                label: parsedResult.viewMapper(local),
                model: model[i],
                checked: false
              });
            }
          }

          parseModel();

          element.append($compile(popUpEl)(scope));

          function getHeaderText(text) {
            if (!modelCtrl.$modelValue || !modelCtrl.$modelValue.length) return scope.header = $translate.instant(attrs.tile);
            if (isMultiple) {
              scope.header = "(" + modelCtrl.$modelValue.length + ") ";
              for (var i = 0; i < modelCtrl.$modelValue.length; i++){
                if (i === 0){
                  if (typeof modelCtrl.$modelValue[i].decode === "undefined"){
                      scope.header += modelCtrl.$modelValue[i].description;
                  }
                  else {
                      scope.header += modelCtrl.$modelValue[i].decode;
                  }
                }
                else{
                    if (typeof modelCtrl.$modelValue[i].decode === "undefined"){
                        scope.header += "; " + modelCtrl.$modelValue[i].description;
                    }
                    else {
                        scope.header += "; " + modelCtrl.$modelValue[i].decode;
                    }
                }
              }
            } else {
              var local = {};
              local[parsedResult.itemName] = modelCtrl.$modelValue;
              scope.header = parsedResult.viewMapper(local);
            }
          }

          scope.valid = function validModel() {
            if(!required) return true;
            var value = modelCtrl.$modelValue;
            return (angular.isArray(value) && value.length > 0) || (!angular.isArray(value) && value != null);
          };

          function selectSingle(item) {
            if (item.checked) {
              scope.uncheckAll();
            } else {
              scope.uncheckAll();
              item.checked = !item.checked;
            }
            setModelValue(false);
          }

          function selectMultiple(item) {
            item.checked = !item.checked;
            setModelValue(true);
          }

          function setModelValue(isMultiple) {
            var value;

            if (isMultiple) {
              value = [];
              angular.forEach(scope.items, function (item) {
                if (item.checked) value.push(item.model);
              })
            } else {
              angular.forEach(scope.items, function (item) {
                if (item.checked) {
                  value = item.model;
                  return false;
                }
              })
            }
            modelCtrl.$setViewValue(value);
          }

          function markChecked(newVal) {
            if (!angular.isArray(newVal)) {
              angular.forEach(scope.items, function (item) {
                if (angular.equals(item.model, newVal)) {
                  item.checked = true;
                  return false;
                }
              });
            } else {
              angular.forEach(newVal, function (i) {
                angular.forEach(scope.items, function (item) {
                  if (angular.equals(item.model, i)) {
                    item.checked = true;
                  }
                });
              });
            }
          }

          scope.checkAll = function () {
            if (!isMultiple) return;
            angular.forEach(scope.items, function (item) {
              item.checked = true;
            });
            setModelValue(true);
          };

          scope.uncheckAll = function () {
            angular.forEach(scope.items, function (item) {
              item.checked = false;
            });
            setModelValue(true);
          };

          scope.select = function (item) {
            if (isMultiple === false) {
              selectSingle(item);
              scope.toggleSelected();
            } else {
              selectMultiple(item);
            }
          }
        }
      };
    }])

  .directive('multiselectPopup', ['$document', function ($document, $http, $templateCache) {
    return {
      restrict: 'AE',
      scope: false,
      replace: true,
      templateUrl: 'scripts/directives/multiselect.tmpl.html',
      link: function (scope, element, attrs) {
        scope.isVisible = false;
          scope.searchText={
              label: ""
          };

        scope.resetSearchBox = function () {
            scope.searchText.label = "";
        };

        scope.toggleSelected = function () {
          if (element.hasClass('open')) {
            element.removeClass('open');
            $document.unbind('click', clickHandler);
          } else {
            element.addClass('open');
            $document.bind('click', clickHandler);
            var searchBox = element.find('input')[0];
            searchBox.focus();

            var removeSpan = element.find('span[data-ng-click="resetSearchBox();"]')[0];
            if ($(removeSpan).attr("data-attr-reset") === "true"){
              setTimeout(function () {
                  removeSpan.click();
                  $(removeSpan).attr("data-attr-reset", false);
              }, 50);
            }
          }
        };

        function clickHandler(event) {
          if (elementMatchesAnyInArray(event.target, element.find(event.target.tagName)))
            return;
          element.removeClass('open');
          $document.unbind('click', clickHandler);
          scope.$digest();
        }

        var elementMatchesAnyInArray = function (element, elementArray) {
          for (var i = 0; i < elementArray.length; i++)
            if (element == elementArray[i])
              return true;
          return false;
        }
      }
    }
  }]);