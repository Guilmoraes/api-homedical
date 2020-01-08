(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('CityDetailController', CityDetailController);

    /** @ngInject **/
    function CityDetailController($scope, $rootScope, $stateParams, previousState, entity, City, State) {
        var vm = this;

        vm.city = entity;
        vm.previousState = previousState.name;

    }
})();
