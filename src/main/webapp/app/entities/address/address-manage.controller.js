
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('AddressManageController', AddressManageController);

    /** @ngInject **/
    function AddressManageController ($stateParams, previousState, $state, entity, Address, City) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.save = save;
        
        City.query(function (result) { vm.cities = result.content});

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.address.id !== undefined) {
                Address.update(vm.address, onSaveSuccess, onSaveError);
            } else {
                Address.save(vm.address, onSaveSuccess, onSaveError);
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
