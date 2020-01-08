/**
 * Created by filipake on 17/07/17.
 */
(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DefineProfessionalPasswordController', DefineProfessionalPasswordController);

    function DefineProfessionalPasswordController($stateParams, $timeout, $state, DefineProfessionalPassword) {
        var vm = this;

        vm.keyMissing = angular.isUndefined($stateParams.key);
        vm.password = null;
        vm.confirmPassword = null;
        vm.doNotMatch = null;
        vm.error = null;
        vm.save = save;
        vm.login = login;

        vm.success = null;


        function save() {
            vm.doNotMatch = false;
            vm.error = false;

            if (vm.password !== vm.confirmPassword) {
                vm.doNotMatch = true;
            } else {
                DefineProfessionalPassword.define({
                    key: $stateParams.key,
                    newPassword: vm.password
                }, onSuccess, onError);
            }
        }

        function login() {
            $state.go('login');
        }

        function onSuccess() {
            vm.success = true;
            vm.error = false;
        }

        function onError(error) {
            vm.error = true;
            vm.success = false;
        }
    }
})();
