(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth'];

    function RequestResetController ($timeout, Auth) {
        var vm = this;

        vm.error = null;
        vm.errorCode = null;
        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;

        $timeout(function (){angular.element('#email').focus();});

        function requestReset () {

            vm.error = null;
            vm.errorCode = null;

            Auth.resetPasswordInit(vm.resetAccount.email).then(function () {
                vm.success = 'OK';
            }).catch(function (response) {
                vm.success = null;
                if (response.status === 400 && response.data === 'email address not registered') {
                    vm.errorCode = 400;
                } if (response.status === 404) {
                    vm.errorCode = 404;
                } else {
                    vm.error = 'ERROR';
                }
            });
        }
    }
})();
