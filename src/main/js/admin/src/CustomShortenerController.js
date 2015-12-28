(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('CustomShortenerController', function ($scope, $http) {
        $scope.shorten = function () {
            $http.post('/api/admin/shorten');
        }
    });

}());