
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('CountryManageController', CountryManageController);

    /** @ngInject **/
    function CountryManageController ($stateParams, previousState, $state, entity, Country) {
        var vm = this;

        vm.country = entity;
        vm.clear = clear;
        vm.save = save;
        

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.country.id !== undefined) {
                Country.update(vm.country, onSaveSuccess, onSaveError);
            } else {
                Country.save(vm.country, onSaveSuccess, onSaveError);
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
