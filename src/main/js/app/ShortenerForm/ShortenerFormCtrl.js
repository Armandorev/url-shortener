(function (angular) {
    'use strict';

    var app = angular.module('UrlShortenerApp');
    app.controller('ShortenerFormCtrl', function ($scope, $http, $timeout) {
        var notificationDuration = 7000;
        $scope.shortenerResponse = {};

        $scope.postURL = function () {
            $http.post('/api/shorten', {url: $scope.url})
                .success(function(data) {
                    $scope.shortenerResponse = data;
                    $scope.showSuccess = true;
                    $scope.fetchURLCount();
                    $timeout(function () {
                        $scope.showSuccess = false;
                    }, notificationDuration);
                })
                .error(function(data) {
                    $scope.showError = true;
                    $scope.shortenerResponse = data;
                    $timeout(function () {
                        $scope.showError = false;
                    }, notificationDuration)
                });
        };

        $scope.fetchURLCount = function () {
            $http.get('/api/stats').success(function (data) {
                $scope.counter = data.counter;
            });
        };

        $scope.fetchURLCount();
    });

}(angular));