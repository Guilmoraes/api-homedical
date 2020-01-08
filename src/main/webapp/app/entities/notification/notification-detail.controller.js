(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('NotificationDetailController', NotificationDetailController);

    /** @ngInject **/
    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

    }
})();
