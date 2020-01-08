(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
            .state('duty', {
                parent: 'entity',
                url: '/duty?page&sort&search',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.duty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/duty/duties.html',
                        controller: 'DutyController',
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
                        $translatePartialLoader.addPart('duty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('duty-detail', {
                parent: 'duty',
                url: '/duty/{id}',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.duty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/duty/duty-detail.html',
                        controller: 'DutyDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('duty');
                        return $translate.refresh();
                    },
                    entity: function ($stateParams, Duty) {
                        return Duty.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'duty',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('duty.new', {
                parent: 'duty',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/duty/duty-manage.html',
                        controller: 'DutyManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: undefined,
                            start: undefined,
                            finish: undefined,
                            overtime: undefined,
                            price: undefined,
                            id: undefined
                        };
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'duty',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }

            })
            .state('duty.edit', {
                parent: 'duty',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/duty/duty-manage.html',
                        controller: 'DutyManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Duty) {
                        return Duty.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'duty',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('duty.delete', {
                parent: 'duty',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                onEnter: function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/duty/duty-delete-dialog.html',
                        controller: 'DutyDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: function (Duty) {
                                return Duty.get({id: $stateParams.id}).$promise;
                            }
                        }
                    }).result.then(function () {
                        $state.go('duty', null, {reload: 'duty'});
                    }, function () {
                        $state.go('^');
                    });
                }
            });
    }

})();
