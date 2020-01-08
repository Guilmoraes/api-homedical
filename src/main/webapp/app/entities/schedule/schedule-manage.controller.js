(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleReportProfessionalController', ScheduleReportProfessionalController);

    /** @ngInject **/
    function ScheduleReportProfessionalController($stateParams, previousState, $state, entity, Schedule) {
        var vm = this;

        vm.schedule = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;


        function clear() {
            $state.go(previousState.name || '^');
        }

        function save() {
            vm.isSaving = true;
            if (vm.schedule.id !== undefined) {
                Schedule.update(vm.schedule, onSaveSuccess, onSaveError);
            } else {
                Schedule.save(vm.schedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.finish = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
