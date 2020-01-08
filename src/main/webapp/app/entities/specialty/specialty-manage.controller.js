
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('SpecialtyManageController', SpecialtyManageController);

    /** @ngInject **/
    function SpecialtyManageController ($stateParams, previousState, $state, entity, Specialty) {
        var vm = this;

        vm.specialty = entity;
        vm.clear = clear;
        vm.save = save;


        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.specialty.id !== undefined) {
                Specialty.update(vm.specialty, onSaveSuccess, onSaveError);
            } else {
                Specialty.save(vm.specialty, onSaveSuccess, onSaveError);
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
