(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .config(stateConfig);

    /** @ngInject **/
    function stateConfig($stateProvider) {
        $stateProvider
            .state('patient', {
                parent: 'entity',
                url: '/patient?page&sort&search',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN'],
                    pageTitle: 'homedicalApp.patient.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/patient/patients.html',
                        controller: 'PatientController',
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
                        $translatePartialLoader.addPart('patient');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('error');
                        return $translate.refresh();
                    }
                }
            })
            .state('patient.new', {
                parent: 'patient',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/patient/patient-manage.html',
                        controller: 'PatientManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            name: undefined,
                            birthdate: undefined,
                            clinicalCondition: undefined,
                            healthOperator: undefined,
                            id: undefined
                        };
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'patient',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }

            })
            .state('patient.edit', {
                parent: 'patient',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/patient/patient-manage.html',
                        controller: 'PatientManageController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function ($stateParams, $state, Patient) {
                        return Patient.get({id: $stateParams.id}).$promise;
                    },
                    previousState: function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'patient',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }
                }
            })
            .state('patient.delete', {
                parent: 'patient',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_ADMIN']
                },
                onEnter: function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/patient/patient-delete-dialog.html',
                        controller: 'PatientDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: function (Patient) {
                                return Patient.get({id: $stateParams.id}).$promise;
                            }
                        }
                    }).result.then(function () {
                        $state.go('patient', null, {reload: 'patient'});
                    }, function () {
                        $state.go('^');
                    });
                }
            });
    }

})();
