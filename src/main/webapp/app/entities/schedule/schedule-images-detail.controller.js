(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleImageDetailController', ScheduleImageDetailController);

    /** @ngInject **/
    function ScheduleImageDetailController($state, $scope, $rootScope, $stateParams) {
        var vm = this;

        if($stateParams.schedule == null) {
            $state.go('schedule.pending')
        }

        vm.schedule = $stateParams.schedule;
        vm.signatureProfessional = undefined;
        vm.signatureCaregiver = undefined;
        vm.hasScheduleImages = vm.schedule.scheduleImages != null && vm.schedule.scheduleImages.length > 0;
        vm.hasSignatures = vm.schedule.signatures != null && vm.schedule.signatures.length > 0;

        if(vm.schedule.signatures != null && vm.schedule.signatures.length > 0){
            vm.signatureProfessional = findBySignatureType('PROFESSIONAL', vm.schedule.signatures)
            vm.signatureCaregiver = findBySignatureType('CAREGIVER', vm.schedule.signatures);
        }

        function findBySignatureType(type, array) {
            return array.filter(function(item) {
                return item.type === type;
            })[0];
        }

    }
})();
