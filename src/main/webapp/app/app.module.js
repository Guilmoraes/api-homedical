(function () {
    'use strict';

    angular
        .module('homedicalApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'notyModule',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'ui.utils.masks',
            'infinite-scroll',
            'selectize',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .constant('uiDatetimePickerConfig', {
            dateFormat: 'dd/MM/yyyy HH:mm',
            defaultTime: '00:00:00',
            html5Types: {
                date: 'dd/MM/yyyy',
                'datetime-local': 'yyyy-MM-ddTHH:mm:ss.sss',
                'month': 'MM/yyyy'
            },
            initialPicker: 'date',
            reOpenDefault: false,
            enableDate: true,
            showMeridian: false,
            enableTime: true,
            buttonBar: {
                show: true,
                now: {
                    show: false,
                    text: 'Agora',
                    cls: 'btn-sm btn-default'
                },
                today: {
                    show: false,
                    text: 'Hoje',
                    cls: 'btn-sm btn-default'
                },
                clear: {
                    show: true,
                    text: 'Limpar',
                    cls: 'btn-sm btn-default'
                },
                date: {
                    show: true,
                    text: 'Data',
                    cls: 'btn-sm btn-default'
                },
                time: {
                    show: true,
                    text: 'Hora',
                    cls: 'btn-sm btn-default'
                },
                close: {
                    show: true,
                    text: 'Fechar',
                    cls: 'btn-sm btn-default'
                },
                cancel: {
                    show: false,
                    text: 'Cancelar',
                    cls: 'btn-sm btn-default'
                }
            },
            closeOnDateSelection: true,
            closeOnTimeNow: true,
            appendToBody: false,
            altInputFormats: [],
            ngModelOptions: {},
            saveAs: false,
            readAs: false
        })
        .run(run)
        .controller('AppController', AppController);


    function AppController($scope, $state) {
        $scope.$state = $state;
    }

    /** @ngInject **/
    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
