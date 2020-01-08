(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('DocumentDetailController', DocumentDetailController);

    /** @ngInject **/
    function DocumentDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Document) {
        var vm = this;

        vm.document = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

    }
})();
