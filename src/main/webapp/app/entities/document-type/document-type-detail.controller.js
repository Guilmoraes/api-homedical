(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DocumentTypeDetailController', DocumentTypeDetailController);

    /** @ngInject **/
    function DocumentTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, DocumentType) {
        var vm = this;

        vm.documentType = entity;
        vm.previousState = previousState.name;

    }
})();
