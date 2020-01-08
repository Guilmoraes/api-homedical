(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ScheduleReportController', ScheduleReportController);

    /** @ngInject **/
    function ScheduleReportController(Schedule, Professional, paginationConstants, pagingParams, $state) {
        var vm = this;

        vm.professionals = [];
        vm.schedules = [];
        vm.professionalsSelected = undefined;
        vm.totalReports = undefined;
        vm.professionalSelected = professionalSelected;
        vm.search = search;
        vm.verification = false;
        vm.totalPrice = undefined;

        search();

        vm.loadProfessional = function (query, callback) {
            Professional.search({firstName: query}, function (result) {
                vm.professionals = vm.professionals.concat(result);
                callback(result);
            },
            function () {
                callback();
            });
        };

        function professionalSelected($id, $model) {
            vm.professionalsSelected = $model;
        }

        function search() {
            if (vm.professionalsSelected) {
                Schedule.filterForDate({
                        page: pagingParams.page - 1,
                        size: vm.itemsPerPage,
                        id: vm.professionalsSelected.id,
                        start: vm.start,
                        finish: vm.finish
                    }, function (result, callback) {
                        vm.schedules = [];
                        vm.totalReports = result.totalElements;
                        vm.schedules = result.content;
                        callback(result.content);
                        getTotalPrice(result.content);
                        vm.verification = true;
                    },
                    function () {
                        callback();
                    });

            }
        }

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.goToCompleteReport = function (id) {
            $state.go('schedule-complete-report', {id: id})
        }

        vm.changed = function () {
            if (vm.finish === undefined) {
                return;
            }
            if (moment(vm.start).isAfter(vm.finish)) {
                vm.start = vm.finish;
            }
        };

        function getTotalPrice(data) {
            vm.totalPrice = 0;
            data.forEach(function(schedule){
                vm.totalPrice += schedule.duty.price;
            });
        }

    }
})();
