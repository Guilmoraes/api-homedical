(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('document-type', {
            parent: 'entity',
            url: '/document-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.documentType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-type/document-types.html',
                    controller: 'DocumentTypeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
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
                    $translatePartialLoader.addPart('documentType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }
            }
        })
        .state('document-type-detail', {
            parent: 'document-type',
            url: '/document-type/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.documentType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-type/document-type-detail.html',
                    controller: 'DocumentTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('documentType');
                    return $translate.refresh();
                },
                entity: function($stateParams, DocumentType) {
                    return DocumentType.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('document-type.new', {
            parent: 'document-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-type/document-type-manage.html',
                    controller: 'DocumentTypeManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: undefined,
                        required: undefined,
                        id: undefined
                    };
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }

        })
        .state('document-type.edit', {
            parent: 'document-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/document-type/document-type-manage.html',
                    controller: 'DocumentTypeManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function($stateParams, $state, DocumentType) {
                    return DocumentType.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'document-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('document-type.delete', {
            parent: 'document-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            onEnter: function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/document-type/document-type-delete-dialog.html',
                    controller: 'DocumentTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: function(DocumentType) {
                            return DocumentType.get({id : $stateParams.id}).$promise;
                        }
                    }
                }).result.then(function() {
                    $state.go('document-type', null, { reload: 'document-type' });
                }, function() {
                    $state.go('^');
                });
            }
        });
    }

})();
