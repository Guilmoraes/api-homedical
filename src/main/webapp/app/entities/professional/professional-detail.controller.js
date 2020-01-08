(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ProfessionalDetailController', ProfessionalDetailController);

    /** @ngInject **/
    function ProfessionalDetailController($scope, $rootScope, $stateParams, previousState, entity, Professional, User) {
        var vm = this;

        vm.professional = entity;
        vm.previousState = previousState.name;

    }
})();
