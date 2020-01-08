(function() {
    'use strict';

    angular
        .module('homedicalApp')
        .config(localStorageConfig);

    /** @ngInject **/
    function localStorageConfig($localStorageProvider, $sessionStorageProvider, $sceProvider) {
        $localStorageProvider.setKeyPrefix('homedicalApp-');
        $sessionStorageProvider.setKeyPrefix('homedicalApp-');

        $sceProvider.enabled(true);
    }
})();
