describe('ShortenerFormCtrl', function () {
    'use strict';

    var $rootScope, $controller, $scope, controller, $state, $httpBackend;

    beforeEach(module('UrlShortenerApp'));

    beforeEach(inject(function (_$rootScope_, _$controller_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $controller = _$controller_;
        $httpBackend = _$httpBackend_;

        $scope = $rootScope.$new();
        controller = function () {
            return $controller('ShortenerFormCtrl', {
                $scope: $scope
            });
        };
    }));

    it('should post a url to the shortener REST backend', function () {
        var originalUrl = 'http://www.google.com';
        var shortenedUrl = 'http://www.short.com/a';
        $httpBackend.when('POST', '/api/shorten').respond(200, {
            original: originalUrl,
            shortened: shortenedUrl
        });
        $httpBackend.when('GET', '/api/stats').respond(200, {
            shortenedCount: 100
        });

        controller();

        $scope.url = originalUrl;
        $scope.postURL();
        $httpBackend.flush();

        expect($scope.shortenerResponse).toEqual({
            original: originalUrl,
            shortened: shortenedUrl
        });
    });

});
