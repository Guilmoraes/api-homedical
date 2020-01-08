(function () {
    angular
        .module('homedicalApp')
        .controller('ProfessionalDutyController', ProfessionalDutyController);

    /** @ngInject **/
    function ProfessionalDutyController(Duty, $state, Professional, $stateParams, AlertService, previousState, $translate) {

        var vm = this;


        vm.clear = clear;
        vm.save = save;
        vm.duties = [];
        vm.profissionalDuties = [];
        vm.dutySelected = dutySelected;
        vm.addDuties = addDuties;
        vm.removeDuties = removeDuties;
        vm.selectedDuties = null;

        Duty.list(function (result) {
            vm.duties = vm.duties.concat(result);
        }, function () {
            callback();
        });


        vm.loadDuties = function (query, callback) {
            Duty.get(function (result) {
                vm.duties = vm.duties.concat(result);
                callback(result.content);
            }, function () {
                callback();
            });
        };


        function save() {
            vm.isSaving = true;
            Professional.updateProfessionalDuties({id: $stateParams.id}, vm.profissionalDuties, onSaveSuccess(), onSaveError())
        }

        function addDuties() {
            if (vm.selectedDuties !== undefined && vm.selectedDuties !== null && vm.selectedDuties !== "") {
                var isDutiesAlreadyAdded = vm.profissionalDuties.filter(function (t) {
                    return t.id === vm.selectedDuties.id;
                });

                if (isDutiesAlreadyAdded.length === 0) {
                    vm.profissionalDuties.push(vm.selectedDuties);
                    vm.selectedProducer = null;
                    var $select = $('#field_duties').selectize();
                    var control = $select[0].selectize;
                    control.clear();
                } else {
                    AlertService.error($translate.instant("homedicalApp.professional.errors.dutyAlreadAdded", null, "", ""));
                }

            }

        }

        function removeDuties(duties) {
            var index = vm.profissionalDuties.indexOf(duties);
            vm.profissionalDuties.splice(index, 1);
        }

        Professional.professionalWithDuties({id: $stateParams.id}, function (result) {
            vm.profissionalDuties = result;
        });

        function dutySelected($id, $model) {
            vm.selectedDuties = $model;
        }

        function clear() {
            $state.go(previousState.name || '^');
        }

        function onSaveSuccess() {
            AlertService.success($translate.instant("homedicalApp.professional.dutySucessMessage", null, "", ""));
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }

})();
