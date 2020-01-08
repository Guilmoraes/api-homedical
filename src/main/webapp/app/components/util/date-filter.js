(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .filter('utcToLocal', ['$filter','$locale', Filter]);

    function Filter($filter, $locale){

        return function (input, format) {
            var date;
            if (!angular.isDefined(format)) {
                format = $locale['DATETIME_FORMATS']['medium'];
            }

            if(input.indexOf('[UTC]') > -1) {
                input = input.replace('[UTC]', '');
                date = moment(input).toDate().toISOString();
            }

            return $filter('date')(date, format)
        };

    }
})();
