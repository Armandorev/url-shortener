(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ImportController', function ($scope, $http) {
        $scope.importRandom = function() {
            $http.post('/api/admin/import').success(function () {
                console.log('Data imported.');
            });
        }
    });

}());