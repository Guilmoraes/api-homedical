/**
 * Created by filipake on 17/07/17.
 */
(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    // stateConfig.$inject = ['stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('defineProfessionalPassword', {
                parent: 'account',
                url: '/defineProfessionalPassword?key',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/account/defineProfessionalPassword/defineProfessionalPassword.html',
                        controller: 'DefineProfessionalPasswordController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('defineProfessionalPassword');
                        $translatePartialLoader.addPart('reset');
                        return $translate.refresh();
                    }]
                }
            });
    }

})();
