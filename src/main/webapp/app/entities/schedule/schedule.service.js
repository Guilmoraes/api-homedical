(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Schedule', Schedule);

    /** @ngInject **/
    function Schedule($resource, DateUtils) {
        var resourceUrl = 'api/schedules/:id/:action';

        return $resource(resourceUrl,{}, {
            'query': {method: 'GET'},
            'queryAll': {method: 'GET', isArray: true, params: {action: 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                        data.finish = DateUtils.convertDateTimeFromServer(data.finish);
                    }
                    return data;
                }
            },

            'update': {method: 'PUT'},
            'filterForDate': {
                method: 'GET', params: {action: 'report'}
            },
            'schedulesPending': {
                method: 'GET', isArray: true, params: {action: 'pending'}
            },
            'scheduleCompleteReport': {
                method: 'GET', params: {action: 'complete-report' }
            },
            'scheduleApproved': {
                method: 'PUT', params: {action: 'approved'}
            }

        });
    }
})();
