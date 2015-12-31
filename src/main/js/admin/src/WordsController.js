(function () {

    var app = angular.module('WordItAdminApp');

    app.controller('WordsController', function ($scope, $http) {

        $scope.page = 0;
        $scope.sortCriteria = '&sort=hash&hash.dir=asc';

        $scope.nextPage = function () {
            $scope.page += 1;
            loadHashes();
        };

        $scope.prevPage = function () {
            $scope.page -= 1;
            loadHashes();
        };

        $scope.sortBy = function (sortByCriteria) {
            $scope.sortCriteria = sortByCriteria;
            loadHashes();
        };

        loadHashes();

        function loadHashes() {
            $http.get('/api/admin/words?page=' + $scope.page + $scope.sortCriteria)
                .success(function (data) {
                    $scope.dictionaryHashes = data.content;
                })
                .error(function () {
                    console.err('error loading hashes');
                });
        }

    });

}());