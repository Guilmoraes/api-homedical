(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('NotificationDeleteController',NotificationDeleteController);

    /** @ngInject **/
    function NotificationDeleteController($uibModalInstance, entity, Notification) {
        var vm = this;

        vm.notification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Notification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
