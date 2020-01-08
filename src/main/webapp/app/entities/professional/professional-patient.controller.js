(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ProfessionalPatientController', ProfessionalPatientController);

    /** @ngInject **/
    function ProfessionalPatientController($stateParams, previousState, $state, $q, entity, AlertService, $translate, Patient, Professional) {
        var vm = this;

        vm.professional = entity;
        vm.clear = clear;
        vm.save = save;
        vm.patients = [];
        vm.professionalPatients = [];

        vm.selectedPatient = null;

        Professional.getProfessionalPatients({id: vm.professional.id}, function (result) {
            vm.professionalPatients = result;
        });

        vm.addPatient = addPatient;
        vm.patientSelected = patientSelected;

        vm.removePatient = removePatient;

        function clear() {
            $state.go(previousState.name || '^');
        }

        vm.loadPatients = function (query, callback) {
            Patient.get({query: query}, function (result) {
                vm.patients = vm.patients.concat(result.content);
                callback(result.content);
            }, function () {
                callback();
            });
        };

        function patientSelected($id, $model) {
            vm.selectedPatient = $model;
        }

        vm.renderPatient = function ($item, $escape) {
            var template = '<div><strong>' + $escape($item.name) + '</strong></div>';
            return template;
        };

        function addPatient() {

            if (vm.selectedPatient !== undefined && vm.selectedPatient !== null && vm.selectedPatient !== "") {

                var isPatientAlreadyAdded = vm.professionalPatients.filter(function (t) {
                    return t.id === vm.selectedPatient.id;
                });

                if (isPatientAlreadyAdded.length === 0) {
                    vm.professionalPatients.push(vm.selectedPatient);
                    vm.selectedProducer = null;
                    var $select = $('#field_patient').selectize();
                    var control = $select[0].selectize;
                    control.clear();
                } else {
                    AlertService.error($translate.instant("homedicalApp.professional.errors.patientAlreadyAdded", null, "", ""));
                }

            }
        }

        function removePatient(patient) {
            var index = vm.professionalPatients.indexOf(patient);
            vm.professionalPatients.splice(index, 1);
        }

        function save() {
            vm.isSaving = true;
            Professional.updateProfessionalPatients({id: vm.professional.id}, vm.professionalPatients, onSaveSuccess, onSaveError())
        }

        function onSaveSuccess(result) {
            AlertService.success($translate.instant("homedicalApp.professional.updateSuccess", null, "", ""));
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
