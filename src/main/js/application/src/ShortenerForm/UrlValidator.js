(function () {
  angular.module('WordItApp').factory('UrlValidator', function () {

    function startsWithHttp(url) {
      if (url.indexOf('http') < 0) {
        return false;
      } else {
        return true;
      }
    }

    return {
      validify: function (url) {
        var whitelist = ['', 'h', 'ht', 'htt'];

        for (var i = 0; i < whitelist.length; ++i) {
          if (whitelist[i] === url) {
            return url;
          }
        }

        if (!startsWithHttp(url)) {
          return 'http://' + url;
        }
        return url;
      }
    }

  })
}());

