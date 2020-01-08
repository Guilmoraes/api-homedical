(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
            .state('health-operator', {
                parent: 'entity',
                url: '/health-operator?page&sort&search',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.healthOperator.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/health-operator/health-operators.html',
                        controller: 'HealthOperatorController',
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
                        $translatePartialLoader.addPart('healthOperator');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('health-operator-detail', {
                parent: 'health-operator',
                url: '/health-operator/{id}',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.healthOperator.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/health-operator/health-operator-detail.html',
                        controller: 'HealthOperatorDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('healthOperator');
                        return $translate.refresh();
                    },
                    entity: function ($stateParams, HealthOperator) {
                        return HealthOperator.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'health-operator',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('health-operator.new', {
                parent: 'health-operator',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/health-operator/health-operator-manage.html',
                        controller: 'HealthOperatorManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            ansRegister: undefined,
                            socialReason: undefined,
                            obs: undefined,
                            id: undefined
                        };
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'health-operator',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }

            })
            .state('health-operator.edit', {
                parent: 'health-operator',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/health-operator/health-operator-manage.html',
                        controller: 'HealthOperatorManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, HealthOperator) {
                        return HealthOperator.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'health-operator',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('health-operator.delete', {
                parent: 'health-operator',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                onEnter: function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/health-operator/health-operator-delete-dialog.html',
                        controller: 'HealthOperatorDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: function (HealthOperator) {
                                return HealthOperator.get({id: $stateParams.id}).$promise;
                            }
                        }
                    }).result.then(function () {
                        $state.go('health-operator', null, {reload: 'health-operator'});
                    }, function () {
                        $state.go('^');
                    });
                }
            });
    }

})();
