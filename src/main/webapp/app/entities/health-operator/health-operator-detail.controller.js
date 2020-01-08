(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('HealthOperatorDetailController', HealthOperatorDetailController);

    /** @ngInject **/
    function HealthOperatorDetailController($scope, $rootScope, $stateParams, previousState, entity, HealthOperator) {
        var vm = this;

        vm.healthOperator = entity;
        vm.previousState = previousState.name;

    }
})();
