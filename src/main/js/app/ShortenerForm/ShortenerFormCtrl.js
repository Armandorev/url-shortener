(function (angular) {
    'use strict';

    var app = angular.module('UrlShortenerApp');
    app.controller('ShortenerFormCtrl', function ($scope, $http) {
        $scope.postURL = function () {
            $http.post('/api/shorten', {url: $scope.url})
                .success(function(data, status, headers, config) {
                    $scope.shortenerResponse = data;
                })
                .error(function(data, status, headers, config) {
                    $scope.shortenerResponse = data;
                });
        };
    });

}(angular));