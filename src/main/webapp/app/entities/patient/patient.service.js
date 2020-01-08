(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Patient', Patient);

    /** @ngInject **/
    function Patient($resource, DateUtils) {
        var resourceUrl = 'api/patients/:id/:action';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET'},
            'queryAll': {method: 'GET', isArray: true, params: {action: 'all'}},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthdate = DateUtils.convertLocalDateFromServer(data.birthdate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthdate = DateUtils.convertLocalDateToServer(copy.birthdate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.birthdate = DateUtils.convertLocalDateToServer(copy.birthdate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
