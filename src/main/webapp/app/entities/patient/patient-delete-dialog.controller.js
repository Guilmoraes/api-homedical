(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('PatientDeleteController', PatientDeleteController);

    /** @ngInject **/
    function PatientDeleteController($uibModalInstance, entity, Patient) {
        var vm = this;

        vm.patient = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Patient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
