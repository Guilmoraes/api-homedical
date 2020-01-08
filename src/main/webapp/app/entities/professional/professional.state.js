(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
            .state('professional', {
                parent: 'entity',
                url: '/professional?page&sort&search',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.professional.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professionals.html',
                        controller: 'ProfessionalController',
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
                        $translatePartialLoader.addPart('professional');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    }
                }
            })
            .state('professional-detail', {
                parent: 'professional',
                url: '/professional/{id}',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.professional.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-detail.html',
                        controller: 'ProfessionalDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('professional');
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    },
                    entity: function ($stateParams, Professional) {
                        return Professional.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'professional',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('professional.new', {
                parent: 'professional',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-manage.html',
                        controller: 'ProfessionalManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: undefined,
                            id: undefined
                        };
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'professional',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }

            })
            .state('professional.edit', {
                parent: 'professional',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-manage.html',
                        controller: 'ProfessionalManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Professional) {
                        return Professional.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'professional',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('professional.delete', {
                parent: 'professional',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                onEnter: function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/professional/professional-delete-dialog.html',
                        controller: 'ProfessionalDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: function (Professional) {
                                return Professional.get({id: $stateParams.id}).$promise;
                            }
                        }
                    }).result.then(function () {
                        $state.go('professional', null, {reload: 'professional'});
                    }, function () {
                        $state.go('^');
                    });
                }
            })
            .state('professional-patient', {
                parent: 'professional',
                url: '/{id}/patients',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-patient.html',
                        controller: 'ProfessionalPatientController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Professional) {
                        return Professional.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'professional',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('professional-duty', {
                parent: 'professional',
                url: '/{id}/duties',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-duty.html',
                        controller: 'ProfessionalDutyController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Professional) {
                        return Professional.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'professional',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('professional-documents', {
                parent: 'entity',
                url: '/professional/documents?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.professional.home.title'
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
                views: {
                    'content@': {
                        templateUrl: 'app/entities/professional/professional-documents.html',
                        controller: 'ProfessionalDocumentController',
                        controllerAs: 'vm'
                    }
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
                        $translatePartialLoader.addPart('professional');
                        $translatePartialLoader.addPart('duty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            });
    }

})();
