(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('HealthOperatorDeleteController', HealthOperatorDeleteController);

    /** @ngInject **/
    function HealthOperatorDeleteController($uibModalInstance, entity, HealthOperator) {
        var vm = this;

        vm.healthOperator = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            HealthOperator.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
