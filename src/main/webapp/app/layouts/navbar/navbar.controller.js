(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('NavbarController', NavbarController);

    /** @ngInject **/
    function NavbarController ($state, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        vm.goHome = goHome;

        vm.checkShowNavbar = function () {
           if ($state.current.name === 'login' ||
               $state.current.name === 'register' ||
               $state.current.name === 'requestReset') {
               vm.showNavBar = false;
           } else {
               vm.showNavBar = true;
           }
        };

        vm.checkShowNavbar();

        function login() {
            collapseNavbar();
            $state.go('login', {}, {reload: true});
        }

        function goHome() {
            $state.go('home')
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('login', {}, {reload: true});
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
