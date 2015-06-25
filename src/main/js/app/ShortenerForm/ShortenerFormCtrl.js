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
                    $timeout(function () {
                        $scope.showSuccess = false;
                        $scope.url = undefined;
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
    });

}(angular));