(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('CustomShortenerController', function ($scope, $http) {

        $scope.shorten = function () {
            console.log('add url');

            $http
                .post('/api/admin/shorten', {url: $scope.url, hash: $scope.hash})
                .success(function (data) {
                    console.log('great, check out ' + $scope.hash);
                })
                .error(function () {
                    console.error('this should not have happened');
                });

            return false;
        };

    });

}());