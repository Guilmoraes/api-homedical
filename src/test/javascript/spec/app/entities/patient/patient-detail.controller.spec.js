'use strict';

describe('Controller Tests', function () {

    describe('Patient Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPatient, MockPhone, MockAddress;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPatient = jasmine.createSpy('MockPatient');
            MockPhone = jasmine.createSpy('MockPhone');
            MockAddress = jasmine.createSpy('MockAddress');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Patient': MockPatient,
                'Phone': MockPhone,
                'Address': MockAddress
            };
            createController = function () {
                $injector.get('$controller')("PatientDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'homedicalApp:patientUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
