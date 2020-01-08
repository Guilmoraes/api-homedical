(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider.state('account', {
            abstract: true,
            parent: 'app'
        });
    }
})();
