(function () {
    'use strict';

    angular.module('homedicalApp')
        .controller('NewLoginController', NewLoginController);

    /** @ngInject **/
    function NewLoginController($rootScope, $state, $timeout, Auth, Principal) {
        var vm = this;

        vm.authenticationError = false;
        vm.cancel = cancel;
        vm.credentials = {};
        vm.login = login;
        vm.username = null;
        vm.password = null;
        vm.register = register;
        vm.requestResetPassword = requestResetPassword;
        vm.isLoginWhatsapp = false;

        function login(event) {
            event.preventDefault();

            Auth.login({
                username: vm.username,
                password: vm.password,
                rememberMe: vm.rememberMe
            }).then(function () {
                vm.authenticationError = false;
                $rootScope.$broadcast('authenticationSuccess');
                goTo();
            }).catch(function () {
                vm.authenticationError = true;
            });
        }

        function goTo() {
            Principal.hasAuthority("ROLE_USER").then(function (value) {
                if(value){
                    $state.go("professional", {}, {reload: true});
                }
            });
        }

        function cancel() {
            vm.credentials = {
                username: null,
                password: null,
                rememberMe: true
            };
            vm.authenticationError = false;
        }

        function requestResetPassword() {
            $state.go('requestReset');
        }

        function register() {
            $state.go('register');
        }


    }
})();
