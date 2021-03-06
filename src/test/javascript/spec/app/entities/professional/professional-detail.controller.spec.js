'use strict';

describe('Controller Tests', function () {

    describe('Professional Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProfessional, MockUser;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProfessional = jasmine.createSpy('MockProfessional');
            MockUser = jasmine.createSpy('MockUser');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Professional': MockProfessional,
                'User': MockUser
            };
            createController = function () {
                $injector.get('$controller')("ProfessionalDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'homedicalApp:professionalUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
