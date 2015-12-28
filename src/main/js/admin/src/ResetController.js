(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ResetController', function ($scope, $http) {

        $scope.reset = function () {
            $http.post('/api/admin/reset').success(function () {
                console.log('cleared data');
            });
        };

    });

}());