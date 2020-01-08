(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('HomeController', HomeController);

    /** @ngInject **/
    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
              if (!Principal.isAuthenticated()) {
                  $state.go('login', {}, {reload: true});
              } else {
                  vm.account = account;
                  vm.isAuthenticated = Principal.isAuthenticated;
              }
            });
        }
        function register () {
            $state.go('register', {}, {reload: true});
        }
    }
})();
