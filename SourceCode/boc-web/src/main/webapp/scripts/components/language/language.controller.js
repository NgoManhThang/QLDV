(function() {
    'use strict';

    angular
        .module('app')
        .controller('ItsLanguageController', ItsLanguageController);

    ItsLanguageController.$inject = ['$translate', 'ItsLanguageService', 'tmhDynamicLocale'];

    function ItsLanguageController ($translate, ItsLanguageService, tmhDynamicLocale) {
        var vm = this;

        vm.changeLanguage = changeLanguage;
        vm.currentLang = currentLang;
        vm.languages = null;

        ItsLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        function changeLanguage (languageKey) {
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
            location.reload();
        }
        function currentLang () {
        	var currentLang = $translate.proposedLanguage() || $translate.use();
        	return currentLang;
        }
    }
})();
