(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DutyManageController', DutyManageController);

    /** @ngInject **/
    function DutyManageController($stateParams, previousState, $state, entity, Duty, Professional) {
        var vm = this;

        vm.duty = entity;
        vm.clear = clear;
        vm.save = save;

        Professional.query(function (result) {
            vm.professionals = result.content;
        });

        function clear() {
            $state.go(previousState.name || '^');
        }

        function save() {
            vm.isSaving = true;
            if (vm.duty.id !== undefined) {
                Duty.update(vm.duty, onSaveSuccess, onSaveError);
            } else {
                Duty.save(vm.duty, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;


        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
