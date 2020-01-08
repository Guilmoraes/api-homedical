(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('country', {
            parent: 'entity',
            url: '/country?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.country.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/countries.html',
                    controller: 'CountryController',
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
                    $translatePartialLoader.addPart('country');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }
            }
        })
        .state('country-detail', {
            parent: 'country',
            url: '/country/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.country.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-detail.html',
                    controller: 'CountryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('country');
                    return $translate.refresh();
                },
                entity: function($stateParams, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'country',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('country.new', {
            parent: 'country',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-manage.html',
                    controller: 'CountryManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        name: undefined,
                        acronym: undefined,
                        id: undefined
                    };
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'country',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }

        })
        .state('country.edit', {
            parent: 'country',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/country/country-manage.html',
                    controller: 'CountryManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function($stateParams, $state, Country) {
                    return Country.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'country',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('country.delete', {
            parent: 'country',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            onEnter: function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/country/country-delete-dialog.html',
                    controller: 'CountryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: function(Country) {
                            return Country.get({id : $stateParams.id}).$promise;
                        }
                    }
                }).result.then(function() {
                    $state.go('country', null, { reload: 'country' });
                }, function() {
                    $state.go('^');
                });
            }
        });
    }

})();
