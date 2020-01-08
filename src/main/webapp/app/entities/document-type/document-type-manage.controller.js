
(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DocumentTypeManageController', DocumentTypeManageController);

    /** @ngInject **/
    function DocumentTypeManageController ($stateParams, previousState, $state, entity, DocumentType) {
        var vm = this;

        vm.documentType = entity;
        vm.clear = clear;
        vm.save = save;
        

        function clear () {
            $state.go(previousState.name || '^');
        }

        function save () {
            vm.isSaving = true;
            if (vm.documentType.id !== undefined) {
                DocumentType.update(vm.documentType, onSaveSuccess, onSaveError);
            } else {
                DocumentType.save(vm.documentType, onSaveSuccess, onSaveError);
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
