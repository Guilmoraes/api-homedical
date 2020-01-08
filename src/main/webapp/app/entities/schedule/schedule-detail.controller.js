(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleDetailController', ScheduleDetailController);

    /** @ngInject **/
    function ScheduleDetailController($scope, $rootScope, $stateParams, previousState, entity, Schedule) {
        var vm = this;

        vm.schedule = entity;
        vm.previousState = previousState.name;

    }
})();
