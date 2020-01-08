(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Professional', Professional);

    /** @ngInject **/
    function Professional($resource) {
        var resourceUrl = 'api/professionals/:id/:action';

        return $resource(resourceUrl, {id: '@id'}, {
            'query': {method: 'GET'},
            'searchAll': {
                method: 'GET',
                params: {action: 'searchlist'}
             },
            'queryAll': {method: 'GET', isArray: true, params: {action: 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'search': {
                method: 'GET', isArray: true, params: {action: 'search'},
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'getProfessionalDocuments': {
                method: 'GET',
                params: {action: 'documents'}
            },
            'update': {method: 'PUT'},
            'getProfessionalPatients': {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'patients'
                }
            },
            'professionalWithDuties': {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'duties'
                }
            },
            'updateProfessionalDuties': {
                method: 'PUT',
                isArray: true,
                params: {
                    action: 'duties'
                }
            },
            'updateProfessionalPatients': {
                method: 'PUT',
                isArray: true,
                params: {
                    action: 'patients'
                }
            }
        });
    }
})();
