(function () {
    'use strict';
    angular
        .module('homedicalApp')
        .factory('Geocoder', Geocoder);

    /** @ngInject **/
    function Geocoder($resource, DateUtils) {
        var resourceUrl = 'api/address-geocoder/:id/:action';

        return $resource(resourceUrl, {}, {
            "findByAddress": {
                method: 'GET',
                isArray: true
            }
        });
    }
})();
