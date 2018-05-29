//var myApp = angular.module('apiService', []);

app.controller('socialController', function ($window, $scope, $http) {
    $scope.oAuth2Details = false;
    $scope.showDataSets = false;
    $scope.oAuthData = {};
    $scope.scheduler ={};
    $scope.onload = function () {
//        $http.get('admin/ui/dataSource').success(function (response) {
//                $scope.dataSources = response;
//                console.log("response--------->",$scope.dataSources);
//            });
        $scope.dataSources = [{name: 'facebook', oauth: false}, {name: 'linkedIn', oauth: false}, {name: 'twitter', oauth: false}, {name: 'googleAnalytics', oauth: false}];
    };
    $scope.onload();
//    $scope.dataSources = [{name: 'facebook', oauth: false}, {name: 'linkedIn', oauth: false}, {name: 'twitter', oauth: false}, {name: 'googleAnalytics', oauth: false}];
    $scope.getOAuthToken = function (index) {
        if ($scope.dataSources[index].oauth == true) {
            $scope.oAuth2Details = false;
            $scope.oAuthData.source = $scope.dataSources[index].name;
             $http({method: "GET", url: "admin/getDataSets/"+$scope.dataSources[index].name}).success(function (response) {
                 console.log("response---------->",response);
            $scope.dataSets = response[0].value;
            $scope.showDataSets = true;
            console.log("dataSets------------------>",$scope.dataSets);
        });
        } else {
            $scope.showDataSets = false;
            $scope.oAuth2Details = true;
            $scope.oAuthData.source = $scope.dataSources[index].name;
            $scope.oAuthData.index = index;
        }
    };
    $scope.onSubmit = function () {
        login("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source);
//        $window.open("admin/social/signin?apiKey=" + $scope.oAuthData.clientId + "&apiSecret=" + $scope.oAuthData.clientSecret + "&apiSource=" + $scope.oAuthData.source);
    };
    $scope.success = function () {
        console.log("called-------->", $scope.oAuth2Details);
        $scope.dataSources[$scope.oAuthData.index].oauth = true;
        $scope.oAuth2Details = false;
        $http({method:"PUT",url:"admin/putDataSource",data:$scope.dataSources[$scope.oAuthData.index]}).success(function(response){
            console.log("response-------->",response);
        });
        console.log("oAuth2Details-------->", $scope.oAuth2Details);
    };
    $scope.setDataSet = function(index){
        $scope.scheduler.dataSetName = $scope.dataSets[index];
        $scope.scheduler.dataSourceName = $scope.oAuthData.source;
    };
    $scope.scheduleData = function(scheduleData){
        console.log("scheduleData-------->",scheduleData);
        $http({method:"POST",url:"admin/schedule",data:scheduleData}).success(function(response){
            console.log("response------>",response);
        });
    };
    function login(url) {
        console.log("url----->", url);
        var win = $window.open(url);

        var pollTimer = $window.setInterval(function () {
            console.log("win--->", win.document.URL);
            try {
                console.log(win.document.URL);
                if (win.document.URL.indexOf("success") != -1) {
                    $window.clearInterval(pollTimer);
                    var url = win.document.URL;
                    win.top.close();
                    $scope.success();
                }
            } catch (e) {
            }
        }, 500);
    }
});
