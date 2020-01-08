(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('AddressDetailController', AddressDetailController);

    /** @ngInject **/
    function AddressDetailController($scope, $rootScope, $stateParams, previousState, entity, Address, City) {
        var vm = this;

        vm.address = entity;
        vm.previousState = previousState.name;

    }
})();
