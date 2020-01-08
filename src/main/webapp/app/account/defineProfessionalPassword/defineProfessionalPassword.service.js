/**
 * Created by filipake on 17/07/17.
 */
(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('DefineProfessionalPassword', DefineProfessionalPassword);

    DefineProfessionalPassword.$inject = ['$resource'];

    function DefineProfessionalPassword($resource) {
        var resourceUrl = 'api/account/:action/:id';

        return $resource(resourceUrl, {}, {
            'define': {
                method: 'POST',
                isArray: false,
                params: {action: 'define_password'}
            }
        });
    }
})();
