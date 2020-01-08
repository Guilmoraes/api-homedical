
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('StateManageController', StateManageController);

    /** @ngInject **/
    function StateManageController ($stateParams, previousState, $state, entity, State, Country) {
        var vm = this;

        vm.state = entity;
        vm.clear = clear;
        vm.save = save;
        
        Country.query(function (result) { vm.countries = result.content});

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.state.id !== undefined) {
                State.update(vm.state, onSaveSuccess, onSaveError);
            } else {
                State.save(vm.state, onSaveSuccess, onSaveError);
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
