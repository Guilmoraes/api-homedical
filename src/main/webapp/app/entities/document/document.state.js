(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('document', {
            parent: 'entity',
            url: '/document?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.document.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document/documents.html',
                    controller: 'DocumentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'lastModifiedDate,desc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                },
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('document');
                    $translatePartialLoader.addPart('documentType');
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('documentStatus');
                    return $translate.refresh();
                }
            }
        }).state('document-detail', {
            parent: 'document',
            url: '/document/{id}',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'homedicalApp.document.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document/document-detail.html',
                    controller: 'DocumentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('document');
                    $translatePartialLoader.addPart('documentType');
                    return $translate.refresh();
                },
                entity: function($stateParams, Document) {
                    return Document.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('documents-professional', {
            parent: 'entity',
            url: '/document/professional?page&sort&search',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'homedicalApp.document.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document/documents.html',
                    controller: 'DocumentController',
                    controllerAs: 'vm'
                }
            },
            params: {
                id: null,
                name: null,
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'lastModifiedDate,desc',
                    squash: true
                },
                search: null
            },
            entity: function($stateParams, Document) {
                return $stateParams.id;
            },
            resolve: {
                pagingParams: function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                },
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('document');
                    $translatePartialLoader.addPart('documentType');
                    $translatePartialLoader.addPart('global');
                    $translatePartialLoader.addPart('documentStatus');
                    return $translate.refresh();
                }
            }
        });
    }

})();
