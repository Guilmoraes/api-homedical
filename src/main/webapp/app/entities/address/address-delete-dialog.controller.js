(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('AddressDeleteController',AddressDeleteController);

    /** @ngInject **/
    function AddressDeleteController($uibModalInstance, entity, Address) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Address.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
