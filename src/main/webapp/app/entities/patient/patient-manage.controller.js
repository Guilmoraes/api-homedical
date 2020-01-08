(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('PatientManageController', PatientManageController);

    /** @ngInject **/
    function PatientManageController(AlertService, $translate, $scope, $uibModal, HealthOperator, $stateParams, previousState, $state, $q, entity, Patient) {
        var vm = this;

        vm.patient = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.addressClicked = addressClicked;
        vm.healthOperatorSelected = healthOperatorSelected;
        vm.healthOperators = [];

        if (vm.patient.id) {
            vm.healthOperators.push(vm.patient.healthOperator)
        }

        function addressClicked() {

            var modalInstance = $uibModal.open({
                templateUrl: 'app/entities/patient/patient-select-address.html',
                controller: function ($scope, $uibModalInstance, Geocoder) {
                    var modalControllerVm = this;
                    modalControllerVm.addressText = null;
                    modalControllerVm.addresses = [];

                    modalControllerVm.selectedItem = selectedItem;
                    modalControllerVm.processAddressChanged = processAddressChanged;

                    function processAddressChanged() {
                        modalControllerVm.addresses = [];
                        if (modalControllerVm.addressText !== undefined && modalControllerVm.addressText !== "") {
                            Geocoder.findByAddress({q: modalControllerVm.addressText}, function (result) {
                                modalControllerVm.addresses = result;
                            });
                        }
                    }

                    function selectedItem(address) {
                        $uibModalInstance.close(address)
                    }
                },
                controllerAs: 'modalControllerVm'
            });

            modalInstance.result.then(function (address) {
                if (address) {
                    vm.patient.address = address;
                }
            })


        }

        vm.loadHealthOperator = function (query, callback) {
            vm.healthOperators = [];

            HealthOperator.get({query: query}, function (result) {
                vm.healthOperators = vm.healthOperators.concat(result.content);
                callback(result.content);
            }, function () {
                callback();
            });
        };

        function healthOperatorSelected($id, $model) {
            vm.patient.healthOperator = $model;
        }

        function clear() {
            $state.go(previousState.name || '^');
        }

        function save() {
            vm.isSaving = true;

            if (!validFields()) {
                vm.isSaving = false;
                return
            }

            if (vm.patient.id !== undefined) {
                Patient.update(vm.patient, onSaveSuccess, onSaveError);
            } else {
                Patient.save(vm.patient, onSaveSuccess, onSaveError);
            }
        }

        function validFields() {
            var isFieldValid = true;

            if (!vm.patient.healthOperator) {
                AlertService.error($translate.instant("homedicalApp.patient.errors.selectHealthOperator", null, "", ""));
                isFieldValid = false;
            }

            return isFieldValid;
        }

        function onSaveSuccess(result) {
            $state.go(previousState.name || '^', $stateParams);
            AlertService.success($translate.instant("homedicalApp.patient.successMessage", null, "", ""));
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.birthdate = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
