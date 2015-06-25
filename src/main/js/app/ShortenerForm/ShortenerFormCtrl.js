(function (angular) {
    'use strict';

    var app = angular.module('UrlShortenerApp');
    app.controller('ShortenerFormCtrl', function ($scope, $http, $timeout) {

        $scope.shortenerResponse = {};

        $scope.postURL = function () {
            $http.post('/api/shorten', {url: $scope.url})
                .success(function(data) {
                    $scope.shortenerResponse = data;
                    $scope.showSuccess = true;
                    $timeout(function () {
                        $scope.showSuccess = false;
                        $scope.url = undefined;
                    }, 5000);
                })
                .error(function(data) {
                    $scope.shortenerResponse = data;
                });
        };
    });

}(angular));