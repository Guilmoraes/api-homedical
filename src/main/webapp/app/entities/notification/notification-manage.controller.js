
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('NotificationManageController', NotificationManageController);

    /** @ngInject **/
    function NotificationManageController ($stateParams, previousState, $state, entity, Notification) {
        var vm = this;

        vm.notification = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.notification.id !== undefined) {
                Notification.update(vm.notification, onSaveSuccess, onSaveError);
            } else {
                Notification.save(vm.notification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.executionDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
