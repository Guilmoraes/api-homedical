(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('PhoneDetailController', PhoneDetailController);

    /** @ngInject **/
    function PhoneDetailController($scope, $rootScope, $stateParams, previousState, entity, Phone) {
        var vm = this;

        vm.phone = entity;
        vm.previousState = previousState.name;

    }
})();
