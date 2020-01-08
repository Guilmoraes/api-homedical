(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleDeleteController', ScheduleDeleteController);

    /** @ngInject **/
    function ScheduleDeleteController($uibModalInstance, entity, Schedule) {
        var vm = this;

        vm.schedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            Schedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
