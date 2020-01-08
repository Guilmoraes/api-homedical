(function() {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Specialty', Specialty);

    /** @ngInject **/
    function Specialty ($resource) {
        var resourceUrl =  'api/specialties/:id/:action';

        return $resource(resourceUrl, {}, {
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
            'getEnabled': {
                method: 'GET', isArray: true, params: {action: 'enabled'},
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
