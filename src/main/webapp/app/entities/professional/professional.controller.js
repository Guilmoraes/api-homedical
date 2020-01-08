(function () {
    'use strict';

    angular
        .module('homedicalApp')
        .controller('ProfessionalController', ProfessionalController);

    /** @ngInject **/
    function ProfessionalController($state, Professional, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.searchValue = "";
        vm.search = search;


        loadAll();

        function loadAll() {
            Professional.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = data.totalElements;
                vm.queryCount = vm.totalItems;
                vm.professionals = data.content;
                vm.page = data.number + 1;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(){
            if(vm.searchValue === ""){
                loadAll();
                return;
            }
            Professional.searchAll({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                name: vm.searchValue
            }, onSuccess, onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = data.totalElements;
                vm.queryCount = vm.totalItems;
                vm.professionals = data.content;
                vm.page = data.number + 1;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
