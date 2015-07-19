(function (angular) {
    'use strict';

    var app = angular.module('UrlShortenerApp');
    app.controller('ShortenerFormCtrl', function ($scope, $http, $timeout) {
        $scope.shortenerResponse = {};

        $scope.postURL = function () {
            $http.post('/api/shorten', {url: $scope.url})
                .success(function(data) {
                    $scope.shortenerResponse = {};
                    $scope.shortenerResponse = data;
                    $scope.showError = false;
                    $scope.showSuccess = true;
                    $scope.fetchURLCount();
                })
                .error(function(data) {
                    $scope.showSuccess = false;
                    $scope.showError = true;
                    $scope.shortenerResponse = data;
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