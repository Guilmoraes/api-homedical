(function() {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Document', Document);

    /** @ngInject **/
    function Document ($resource) {
        var resourceUrl =  'api/documents/:id/:action';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET'},
            'queryAll': { method: 'GET', isArray: true, params : {action : 'all'}},
            'getAllProfessionalDocuments': {
                method: 'GET',
                params : {action : 'professionals'},
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'updateStatus': {
                method: 'PUT',
                params: { action : 'status' }
            }
        });
    }
})();
