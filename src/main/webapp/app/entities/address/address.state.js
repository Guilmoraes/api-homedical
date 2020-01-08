(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('address', {
            parent: 'entity',
            url: '/address?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.address.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/addresses.html',
                    controller: 'AddressController',
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
                    $translatePartialLoader.addPart('address');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }
            }
        })
        .state('address-detail', {
            parent: 'address',
            url: '/address/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.address.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/address-detail.html',
                    controller: 'AddressDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('address');
                    return $translate.refresh();
                },
                entity: function($stateParams, Address) {
                    return Address.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'address',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('address.new', {
            parent: 'address',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/address-manage.html',
                    controller: 'AddressManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        street: undefined,
                        number: undefined,
                        zipcode: undefined,
                        district: undefined,
                        complement: undefined,
                        id: undefined
                    };
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'address',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }

        })
        .state('address.edit', {
            parent: 'address',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address/address-manage.html',
                    controller: 'AddressManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function($stateParams, $state, Address) {
                    return Address.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'address',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('address.delete', {
            parent: 'address',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            onEnter: function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address/address-delete-dialog.html',
                    controller: 'AddressDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: function(Address) {
                            return Address.get({id : $stateParams.id}).$promise;
                        }
                    }
                }).result.then(function() {
                    $state.go('address', null, { reload: 'address' });
                }, function() {
                    $state.go('^');
                });
            }
        });
    }

})();
