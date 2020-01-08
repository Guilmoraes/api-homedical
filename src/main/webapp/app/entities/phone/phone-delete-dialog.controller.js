(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('PhoneDeleteController',PhoneDeleteController);

    /** @ngInject **/
    function PhoneDeleteController($uibModalInstance, entity, Phone) {
        var vm = this;

        vm.phone = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Phone.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
