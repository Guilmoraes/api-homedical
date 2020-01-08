(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DocumentTypeDeleteController',DocumentTypeDeleteController);

    /** @ngInject **/
    function DocumentTypeDeleteController($uibModalInstance, entity, DocumentType) {
        var vm = this;

        vm.documentType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DocumentType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
