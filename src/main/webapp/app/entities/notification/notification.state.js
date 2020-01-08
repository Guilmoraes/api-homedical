(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification', {
            parent: 'entity',
            url: '/notification?page&sort&search',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notifications.html',
                    controller: 'NotificationController',
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
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationSourceType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }
            }
        })
        .state('notification-detail', {
            parent: 'notification',
            url: '/notification/{id}',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                pageTitle: 'homedicalApp.notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notification-detail.html',
                    controller: 'NotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('notificationType');
                    $translatePartialLoader.addPart('notificationSourceType');
                    return $translate.refresh();
                },
                entity: function($stateParams, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('notification.new', {
            parent: 'notification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notification-manage.html',
                    controller: 'NotificationManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        type: undefined,
                        sourceId: undefined,
                        sourceType: undefined,
                        pending: undefined,
                        executionDate: undefined,
                        id: undefined
                    };
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }

        })
        .state('notification.edit', {
            parent: 'notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notification-manage.html',
                    controller: 'NotificationManageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function($stateParams, $state, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                },
                previousState: function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }
            }
        })
        .state('notification.delete', {
            parent: 'notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER', 'ROLE_ADMIN']
            },
            onEnter: function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-delete-dialog.html',
                    controller: 'NotificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }
        });
    }

})();
