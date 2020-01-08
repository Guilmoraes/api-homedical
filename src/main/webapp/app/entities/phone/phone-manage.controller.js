
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('PhoneManageController', PhoneManageController);

    /** @ngInject **/
    function PhoneManageController ($stateParams, previousState, $state, entity, Phone) {
        var vm = this;

        vm.phone = entity;
        vm.clear = clear;
        vm.save = save;
        

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.phone.id !== undefined) {
                Phone.update(vm.phone, onSaveSuccess, onSaveError);
            } else {
                Phone.save(vm.phone, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }




    }
})();
