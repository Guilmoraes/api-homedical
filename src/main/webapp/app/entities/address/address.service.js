(function() {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Address', Address);

    /** @ngInject **/
    function Address ($resource) {
        var resourceUrl =  'api/addresses/:id/:action';

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
