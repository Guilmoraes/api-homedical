(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('StateDetailController', StateDetailController);

    /** @ngInject **/
    function StateDetailController($scope, $rootScope, $stateParams, previousState, entity, State, Country) {
        var vm = this;

        vm.state = entity;
        vm.previousState = previousState.name;

    }
})();
