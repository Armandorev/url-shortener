(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('HeaderController', function ($scope, $http) {
        $http.get('/api/stats').success(function (data) {
            $scope.shortenedCount = data.shortenedCount;
            $scope.remainingCount = data.remainingCount;
        });
    });

}());