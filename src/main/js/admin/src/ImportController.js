(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('ImportController', function ($scope, $http) {
        $scope.import = function () {
            $http
                .post('/api/admin/words/import', {wordLength: $scope.wordLength, numberOfWords: $scope.numberOfWords})
                .success(function (data) {
                    console.log('Data imported: ' + data);
                });
        }
    });

}());