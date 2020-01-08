(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider.state('finishReset', {
            parent: 'account',
            url: '/reset/finish?key',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/reset/finish/reset.finish.html',
                    controller: 'ResetFinishController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reset');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
