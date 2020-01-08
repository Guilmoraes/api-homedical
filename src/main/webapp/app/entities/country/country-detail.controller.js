(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('CountryDetailController', CountryDetailController);

    /** @ngInject **/
    function CountryDetailController($scope, $rootScope, $stateParams, previousState, entity, Country) {
        var vm = this;

        vm.country = entity;
        vm.previousState = previousState.name;

    }
})();
