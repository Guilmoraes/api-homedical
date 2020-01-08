(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ProfessionalManageController', ProfessionalManageController);

    /** @ngInject **/
    function ProfessionalManageController(AlertService, $translate, $stateParams, previousState, $state, $q, entity, Professional, User, City, Specialty) {
        var vm = this;
        vm.professional = entity;
        vm.professional.address = {};
        vm.clear = clear;
        vm.save = save;
        vm.cities = [];
        vm.specialties = [];
        vm.professional.specialties = [];
        vm.specialtySelected = specialtySelected;


        Specialty.getEnabled(function (result) {
            vm.specialties = vm.specialties.concat(result);
        }, function () {
            callback();
        });

        vm.loadcity = function (query, callback) {
            City.get({query: query}, function (result) {
                vm.cities = vm.cities.concat(result.content);
                callback(result.content);
            }, function () {
                callback();
            });
        };

        vm.loadSpecialties = function (query, callback) {
            Specialty.getEnabled(function (result) {
                vm.specialties = vm.specialties.concat(result);
                callback(result.content);
            }, function () {
                callback();
            });
        };


        vm.rendercity = function ($item, $escape) {
            return '<div><strong>' + $escape($item.name) + ' - ' + $escape($item.state.acronym) + '</strong></div>';
        };

        vm.citiesSelected = function ($id, $model) {
            vm.professional.address.city = $model;
        };

        function specialtySelected($id, $model) {
            vm.professional.specialties = $model;
        }


        User.query(function (result) {
            vm.users = result.content
        });

        function clear() {
            $state.go(previousState.name || '^');
        }

        function save() {
            vm.isSaving = true;


            if (validateFields()) {
                if (vm.professional.id !== undefined) {
                    Professional.update(vm.professional, onSaveSuccess, onSaveError);
                } else {
                    Professional.save(vm.professional, onSaveSuccess, onSaveError);
                }
            } else {
                vm.isSaving = false;
            }

        }

        function validateFields() {
            var isFieldValid = true;

            if (vm.professional.specialties.length <= 0) {
                AlertService.error($translate.instant("homedicalApp.professional.errors.specialtyError", null, "", ""));
                isFieldValid = false;
            }

            if (vm.professional.address.city === undefined || vm.professional.address.city === null) {
                AlertService.error($translate.instant("homedicalApp.professional.errors.cityError", null, "", ""));
                isFieldValid = false;
            }

            return isFieldValid;
        }

        function onSaveSuccess(result) {
            AlertService.success($translate.instant("homedicalApp.professional.successMessage", null, "", ""));
            $state.go(previousState.name || '^', $stateParams);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
