
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('CityManageController', CityManageController);

    /** @ngInject **/
    function CityManageController ($stateParams, previousState, $state, entity, City, State) {
        var vm = this;

        vm.city = entity;
        vm.clear = clear;
        vm.save = save;
        
        State.query(function (result) { vm.states = result.content});

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.city.id !== undefined) {
                City.update(vm.city, onSaveSuccess, onSaveError);
            } else {
                City.save(vm.city, onSaveSuccess, onSaveError);
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
