(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ListController', function ($scope, $http) {
        $scope.shortenedUrls = null;

        $http.get('/api/admin/shortened_urls').success(function (data) {
            $scope.shortenedUrls = data;
        });

    });

}());