(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Duty', Duty);

    /** @ngInject **/
    function Duty($resource, DateUtils) {
        var resourceUrl = 'api/duties/:id/:action';

        return $resource(resourceUrl, {id: '@id'}, {
            'query': {method: 'GET'},
            'list': {
                method: 'GET',
                isArray: true,
                params: {
                    action: 'listDuties'
                },
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'queryAll': {method: 'GET', isArray: true, params: {action: 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                        data.finish = DateUtils.convertDateTimeFromServer(data.finish);
                        data.overtime = DateUtils.convertDateTimeFromServer(data.overtime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.start = DateUtils.convertDateTimeToServer(copy.start);
                    copy.finish = DateUtils.convertDateTimeToServer(copy.finish);
                    copy.overtime = DateUtils.convertDateTimeToServer(copy.overtime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.start = DateUtils.convertDateTimeToServer(copy.start);
                    copy.finish = DateUtils.convertDateTimeToServer(copy.finish);
                    copy.overtime = DateUtils.convertDateTimeToServer(copy.overtime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
