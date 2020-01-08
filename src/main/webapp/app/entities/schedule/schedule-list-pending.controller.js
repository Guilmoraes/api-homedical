(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('SchedulePendingController', SchedulePendingController);

    /** @ngInject **/
    function SchedulePendingController($state, Schedule, $translate, noty, AlertService) {

        var vm = this;

        vm.schedules = [];
        vm.approved = approved;
        vm.refused = refused;
        vm.goToImage = goToImage;

        loadAll();

        function loadAll() {
            Schedule.schedulesPending(onSuccess, onError);

            function onSuccess(data) {
                vm.schedules = data;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function approved(data) {
            data.status = "APPROVED";
            Schedule.scheduleApproved(data, onSaveSuccess, onSaveError);
            vm.schedules.splice(vm.schedules.indexOf(data), 1);
        }

        function goToImage(schedule) {
            $state.go('schedule.image', {schedule: schedule})
        }

        function refused(data) {
            data.status = "REFUSED";
            Schedule.scheduleApproved(data, onSaveSuccess, onSaveError);
            vm.schedules.splice(vm.schedules.indexOf(data), 1);
        }

        function onSaveSuccess(data) {
            noty.success($translate.instant("homedicalApp.schedule.message.updateStatusSchedule.success", null, "", ""));
            vm.validation = false;
        }

        function onSaveError() {
            noty.error($translate.instant("homedicalApp.schedule.message.updateStatusSchedule.error", null, "", ""));
            vm.validation = false;
        }
    }
})();
