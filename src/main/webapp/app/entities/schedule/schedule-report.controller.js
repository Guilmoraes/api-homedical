(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleCompleteReportController', ScheduleCompleteReportController);

    /** @ngInject **/
    function ScheduleCompleteReportController($state, $scope, $rootScope, $stateParams, Schedule) {
        var vm = this;
        vm.report = undefined;

        if($stateParams.id == null) {
            $state.go("schedule.report")
            return;
        }

        Schedule.scheduleCompleteReport({
            id: $stateParams.id,
        }, onSuccess, onError);

        function onSuccess(data) {
            var schedule = data;
            console.log({data : data});
            var address = '';
            var client = '';
            if (schedule.patient != null) {
                client = schedule.patient.name;
                if (schedule.patient.address != null) {
                    address = returnEmptyOrValue(schedule.patient.address.street) + " " +
                        returnEmptyOrValue(schedule.patient.address.number);
                    address = returnEmptyOrValue(schedule.patient.address.zipcode) !== '' ? address + ' - ' + schedule.patient.address.zipcode : address;
                    address = returnEmptyOrValue(schedule.patient.address.cityName) !== '' ? address + ' / ' + schedule.patient.address.cityName : address;
                    address = returnEmptyOrValue(schedule.patient.address.stateName) !== '' ? address + ', ' + schedule.patient.address.stateName : address;
                }
            }
            vm.report = {
                client: client,
                address: address,
                employee: schedule.professional.name,
                checkin: schedule.start,
                checkout: schedule.finish,
                contact: schedule.professional.phone != null ? schedule.professional.phone.number : '',
                images: schedule.scheduleImages,
                caregiverSignature: findBySignatureType('CAREGIVER', schedule.signatures),
                professionalSignature: {
                    professionalName: schedule.professional.name,
                    document: schedule.professional.cpf,
                    signature: findBySignatureType('PROFESSIONAL', schedule.signatures)
                }
            };
            console.log({report : vm.report});

        }

        function onError(error) {
            console.log(error);
        }

        function findBySignatureType(type, array) {
            return array.filter(function (item) {
                return item.type === type;
            })[0];
        }

        function returnEmptyOrValue(text) {
            return text != null && text.length > 0 ? text : '';
        }

    }
})();
