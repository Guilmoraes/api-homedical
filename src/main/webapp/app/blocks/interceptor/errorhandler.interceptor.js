(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .factory('errorHandlerInterceptor', errorHandlerInterceptor);

    /** @ngInject **/
    function errorHandlerInterceptor ($q, $rootScope) {
        var service = {
            responseError: responseError
        };

        return service;

        function responseError (response) {
            if (!(response.status === 401 && (response.data === '' || (response.data.path && response.data.path.indexOf('/api/account') === 0 )))) {
                $rootScope.$emit('homedicalApp.httpError', response);
            }
            return $q.reject(response);
        }
    }
})();
