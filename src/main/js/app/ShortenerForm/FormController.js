(function (angular) {
  'use strict';

  var app = angular.module('WordItApp');

  app.controller('WordItFormController', function ($scope, $http, UrlValidator) {

    function restoreDefault() {
      $scope.shortener = {
        hash: '...'
      };
      $scope.url = null;
      $scope.submitted = false;
    }

    function isSubmitted() {
      return $scope.submitted
    }

    restoreDefault();

    $scope.validify = function () {
      $scope.url = UrlValidator.validify($scope.url);
    };

    $scope.postURL = function () {
      if (isSubmitted()) {
        restoreDefault();
        return;
      }

      $scope.disabled = true;
      $http.post('/api/shorten', {url: $scope.url})
        .success(function (data) {
          $scope.submitted = true;

          $scope.shortener = {};
          $scope.shortener = data;
          $scope.fetchURLCount();
        })
        .error(function (data) {
          $scope.shortener = data;
          $scope.submitted = false;
        });
    };

    $scope.fetchURLCount = function () {
      $http.get('/api/stats').success(function (data) {
        $scope.stats = data;
      });
    };

    $scope.fetchURLCount();
  });

}(angular));