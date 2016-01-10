(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ListController', function ($scope, $http) {
        $scope.shortenedUrls = null;

        $http.get('/api/admin/shortened_urls').success(function (data) {
            $scope.shortenedUrls = data;
        });

        $scope.remove = function (hash) {
            $http
                .post('/api/admin/words/remove', {hash: hash})
                .success(function (data) {
                    console.log('Hash removed: ' + data);
                });
        };

    });

}());