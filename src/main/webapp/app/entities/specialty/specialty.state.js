(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('specialty', {
            parent: 'entity',
            url: '/specialty?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.specialty.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/specialty/specialties.html',
                    controller: 'SpecialtyController',
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
                    $translatePartialLoader.addPart('specialty');
                    $translatePartialLoader.addPart('objectStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }
            }
        })
        .state('specialty.new', {
            parent: 'specialty',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/specialty/specialty-manage.html',
                    controller: 'SpecialtyManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: undefined,
                        status: undefined,
                        id: undefined
                    };
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'specialty',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }

        })
        .state('specialty.edit', {
            parent: 'specialty',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/specialty/specialty-manage.html',
                    controller: 'SpecialtyManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function($stateParams, $state, Specialty) {
                    return Specialty.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'specialty',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
    }

})();
