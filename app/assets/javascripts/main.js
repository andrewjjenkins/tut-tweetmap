var app = angular.module('myApp', []);

app.factory('Twitter', function($http, $timeout) {
    
    var twitterService = {
        tweets: [],
        query: function (query) {
            $http({method: 'GET', url: '/tweets', params: {query: query}}).
                success(function (data) {
                    twitterService.tweets = data.statuses;
                });
        }
    };
    
   
    return twitterService;
});

app.controller('Search', function($scope, $http, $timeout, Twitter) {

    $scope.search = function() {
        console.log('Doing twitter query: ' + $scope.query);
        Twitter.query($scope.query);
    };

});

app.controller('Tweets', function($scope, $http, $timeout, Twitter) {

    $scope.tweets = [];
    
    $scope.$watch(
        function() {
            return Twitter.tweets;
        },
        function(tweets) { 
            $scope.tweets = tweets;
            
        }
    );
    
});
