(function() {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('DocumentType', DocumentType);

    /** @ngInject **/
    function DocumentType ($resource) {
        var resourceUrl =  'api/document-types/:id/:action';

        return $resource(resourceUrl, {id : '@id'}, {
            'query': { method: 'GET'},
            'queryAll': { method: 'GET', isArray: true, params : {action : 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
