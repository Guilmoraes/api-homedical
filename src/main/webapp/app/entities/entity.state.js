(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider.state('entity', {
            abstract: true,
            parent: 'app'
        });
    }
})();
