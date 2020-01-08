(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
            .state('schedule', {
                parent: 'entity',
                url: '/schedule?page&sort&search',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.schedule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedules.html',
                        controller: 'ScheduleController',
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
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('schedulesStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('schedule-detail', {
                parent: 'schedule',
                url: '/schedule/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.schedule.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-detail.html',
                        controller: 'ScheduleDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('schedulesStatus');
                        return $translate.refresh();
                    },
                    entity: function ($stateParams, Schedule) {
                        return Schedule.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'schedule',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('schedule.new', {
                parent: 'schedule',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-manage.html',
                        controller: 'ScheduleManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            start: undefined,
                            finish: undefined,
                            status: undefined,
                            id: undefined
                        };
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'schedule',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }

            })
            .state('schedule.edit', {
                parent: 'schedule',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-manage.html',
                        controller: 'ScheduleManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Schedule) {
                        return Schedule.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'schedule',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('schedule.report', {
                parent: 'schedule',
                url: '/{id}/reports?page&start&finish',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-report-professional-date.html',
                        controller: 'ScheduleReportController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('schedulesStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('schedule.delete', {
                parent: 'schedule',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/schedule/schedule-delete-dialog.html',
                        controller: 'ScheduleDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: function (Schedule) {
                                return Schedule.get({id: $stateParams.id}).$promise;
                            }
                        }
                    }).result.then(function () {
                        $state.go('schedule', null, {reload: 'schedule'});
                    }, function () {
                        $state.go('^');
                    });
                }
            })
            .state('schedule.pending', {
                parent: 'entity',
                url: '/schedule/pending',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.schedule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-list-pending.html',
                        controller: 'SchedulePendingController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('schedulesStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('schedule.image', {
                parent: 'entity',
                url: '/schedule/image',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.scheduleImage.detail.title'
                },
                params: {
                    schedule: null
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-images-detail.html',
                        controller: 'ScheduleImageDetailController',
                        controllerAs: 'vm'
                    }
                },
                entity: function ($stateParams) {
                    return $stateParams;
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('scheduleImage');
                        $translatePartialLoader.addPart('schedulesStatus');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            })
            .state('schedule-complete-report', {
                parent: 'entity',
                url: '/schedule/complete-report',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.scheduleReport.report.title'
                },
                params: {
                    id: null
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/schedule/schedule-report.html',
                        controller: 'ScheduleCompleteReportController',
                        controllerAs: 'vm'
                    }
                },
                previousState: function ($state) {
                    var currentStateData = {
                        // name: $state.current.name || 'professional',
                        // params: $state.params,
                        // url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                },
                resolve: {
                    translatePartialLoader: function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('schedule');
                        $translatePartialLoader.addPart('scheduleReport');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }
                }
            });
    }

})();
