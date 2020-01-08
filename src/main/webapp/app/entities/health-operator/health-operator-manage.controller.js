(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('HealthOperatorManageController', HealthOperatorManageController);

    /** @ngInject **/
    function HealthOperatorManageController($stateParams, previousState, $state, entity, HealthOperator) {
        var vm = this;

        vm.healthOperator = entity;
        vm.clear = clear;
        vm.save = save;


        function clear() {
            $state.go(previousState.name || '^');
        }

        function save() {
            vm.isSaving = true;
            if (vm.healthOperator.id !== undefined) {
                HealthOperator.update(vm.healthOperator, onSaveSuccess, onSaveError);
            } else {
                HealthOperator.save(vm.healthOperator, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
