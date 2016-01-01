(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ResetController', function ($scope, $http) {

        $scope.resetAll = function () {
            $http.post('/api/admin/reset').success(function () {
                console.log('cleared all words');
            });
        };

        $scope.resetUnused = function () {
            $http.post('/api/admin/words/remove_unused').success(function () {
                console.log('cleared unused words');
            });
        };

    });

}());