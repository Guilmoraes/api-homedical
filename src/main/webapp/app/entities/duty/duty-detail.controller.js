(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DutyDetailController', DutyDetailController);

    /** @ngInject **/
    function DutyDetailController($scope, $rootScope, $stateParams, previousState, entity, Duty, Professional) {
        var vm = this;

        vm.duty = entity;
        vm.previousState = previousState.name;

    }
})();
