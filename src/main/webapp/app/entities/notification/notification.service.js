(function() {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Notification', Notification);

    /** @ngInject **/
    function Notification ($resource, DateUtils) {
        var resourceUrl =  'api/notifications/:id/:action';

        return $resource(resourceUrl, {id : '@id'}, {
            'query': { method: 'GET'},
            'queryAll': { method: 'GET', isArray: true, params : {action : 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.executionDate = DateUtils.convertDateTimeFromServer(data.executionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
