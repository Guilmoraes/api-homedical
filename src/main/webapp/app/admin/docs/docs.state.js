(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    /** @ngInject **/
    function stateConfig ($stateProvider) {
        $stateProvider.state('docs', {
            parent: 'admin',
            url: '/docs',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'global.menu.admin.apidocs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/docs/docs.html'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', function ($translate) {
                    return $translate.refresh();
                }]
            }
        });
    }
})();
