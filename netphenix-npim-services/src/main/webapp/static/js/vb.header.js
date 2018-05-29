app.controller('HeaderController', function ($scope, $cookies, $http, $filter, $stateParams, localStorageService, $state, $location, $rootScope, $translate, $timeout) {
    $scope.permission = localStorageService.get("permission");
    $scope.userName = $cookies.getObject("username");
    $scope.isAdmin = $cookies.getObject("isAdmin");
    $scope.agencyId = $cookies.getObject("agencyId");
    $scope.fullName = $cookies.getObject("fullname");
    $scope.productId = $stateParams.productId;
    $scope.selectTabID = $state;
    $scope.agencyLanguage = localStorageService.get("agencyLanguage");
    $scope.tempLan = localStorageService.get('agenLan');
    $scope.lan = $stateParams.lan ? $stateParams.lan : $scope.agencyLanguage;
    $stateParams.lan = $scope.lan;
    changeLanguage($scope.lan);

    function changeLanguage(key) {
        if ($scope.lan != 'en') {
            $scope.showLangBtn = 'en';
        } else {
            var getAgencyLan = localStorageService.get('agenLan');
            $scope.showLangBtn = getAgencyLan;
        }
        $translate.use(key);
    }

    $scope.changeAgencyLang = function (lan) {
        if (lan == 'en') {
            localStorageService.set('agencyLanguage', lan);
            //var agencyLan = localStorageService.get('agenLan');
            var agencyLan = $stateParams.lan ? $stateParams.lan : localStorageService.get('agenLan');
            $scope.showLangBtn = agencyLan;
        } else {
            localStorageService.set('agencyLanguage', lan);
            $scope.showLangBtn = 'en';
        }
        $stateParams.lan = lan;
        $scope.lan = lan;
        $scope.loadNewUrl();
    };

    $scope.setParamsProduct = function (product) {
        var setTabId = 0;
        var productId = product.id;
        var lastTemplateId;
        var setTemplateId = product.templateId ? product.templateId.id : 0;

        if ($stateParams.productId != product.id) {
            $http.get("admin/template/getProductTemplate/" + productId + "/" + $stateParams.accountId).success(function (response) {
                var responseObj = response;
                // if (responseObj) {
                lastTemplateId = responseObj ? responseObj.id : null;
                $stateParams.productId = product.id;
                $stateParams.templateId = lastTemplateId ? lastTemplateId : setTemplateId;//product.templateId ? product.templateId.id : 0;
                // }
                $state.go("index.dashboard.widget", {
                    accountId: $stateParams.accountId,
                    accountName: $stateParams.accountName,
                    productId: $stateParams.productId,
                    templateId: $stateParams.templateId,
                    tabId: setTabId,
                    startDate: $stateParams.startDate,
                    endDate: $stateParams.endDate,
                    compareStatus: $scope.selectedTablesType,
                    compareStartDate: $scope.compare_startDate,
                    compareEndDate: $scope.compare_endDate
                });
            });
        } else {
            return;
        }
    };

    $scope.themes = [{name: "Green", value: "green"},
        {name: "Blue", value: "blue"},
        {name: "Cyan", value: "cyan"},
        {name: "Pink", value: "pink"},
        {name: "Gray", value: "gray"}
    ];

    $scope.setParams = function () {
        $scope.accountId = $stateParams.accountId;
        $scope.accountName = $stateParams.accountName;
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
        $scope.compareStatus = $scope.selectedTablesType;
        $scope.compareStartDate = $scope.compare_startDate;
        $scope.compareEndDate = $scope.compare_endDate;
    };
    $scope.product = [];
    $scope.selectAccount = {};

    $http.get('admin/ui/userAccountByUser').success(function (response) {

        if (!response[0]) {
            return;
        }
        $scope.accounts = response;
        $http.get('admin/user/getLastUserAccount').success(function (userAccountResponse) {
            if (userAccountResponse) {
                $stateParams.accountId = userAccountResponse.accountId.id;
                $stateParams.accountName = userAccountResponse.accountId.accountName;
            }
            $stateParams.accountId = $stateParams.accountId ? $stateParams.accountId : response[0].accountId.id;
            $stateParams.accountName = $stateParams.accountName ? $stateParams.accountName : response[0].accountId.accountName;
            angular.forEach($scope.accounts, function (value, key) {
                if (value.accountId.id == $stateParams.accountId) {
                    $scope.name = value;
                }
            });
            $scope.selectAccount.selected = {accountName: $scope.name.accountId.accountName};
            $scope.accountLogo = $scope.name.accountId.logo;
            if (!$scope.name.userId.agencyId) {
                $scope.loadNewUrl();
                return;
            }
            getAgencyProduct($scope.name.userId.agencyId.id);
        });
    });

    $scope.getAccountId = function (account) {
        console.log(account);
        if ($stateParams.accountId != account.accountId.id) {
            $stateParams.tabId = 0;
            $stateParams.templateId = 0;
        }
        if (account.accountId.logo) {
            $scope.accountLogo = account.accountId.logo;
        } else {
            $scope.accountLogo = "";
        }
        $stateParams.accountId = account.accountId.id;
        $scope.selectAccount.selected = {accountName: account.accountId.accountName};
        $stateParams.accountName = account.accountId.accountName;
        $http.get("admin/template/getProductTemplate/" + $stateParams.productId + "/" + $stateParams.accountId).success(function (response) {
            $stateParams.templateId = response.id;
            $scope.loadNewUrl();
        });
        $http({method: 'POST', url: "admin/user/lastUserAccount/" + $stateParams.productId + "/" + $stateParams.accountId}).success(function (response) {
            console.log(response);
        });
    };

    $scope.toDate = function (strDate) {
        if (!strDate) {
            return new Date();
        }
        var from = strDate.split("/");
        var f = new Date(from[2], from[0] - 1, from[1]);
        return f;
    };

    $scope.getDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 30);
        return yesterday;
    };

    $scope.getBeforeDay = function () {
        var today = new Date();
        var yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);
        return yesterday;
    };
//alert($stateParams.endDate)
//return
    $scope.firstDate = $stateParams.startDate ? $scope.toDate(decodeURIComponent($stateParams.startDate)) : $scope.getDay().toLocaleDateString("en-US");
    $scope.lastDate = $stateParams.endDate ? $scope.toDate(decodeURIComponent($stateParams.endDate)) : $scope.getBeforeDay().toLocaleDateString("en-US");
    if (!$stateParams.startDate) {
        $stateParams.startDate = $scope.firstDate;
    }
    if (!$stateParams.endDate) {
        $stateParams.endDate = $scope.lastDate;
    }

    function getAgencyProduct(agencyId) {
        $http.get('admin/user/agencyProduct/' + agencyId).success(function (response) {
            $scope.products = response;
            if (!response) {
                return;
            }
            if (!response[0]) {
                return;
            }
            var getTemplateId = response[0].templateId ? response[0].templateId.id : 0;
            $stateParams.productId = $stateParams.productId ? $stateParams.productId : response[0].id;
            var getProductTemplateId = $stateParams.templateId ? $stateParams.templateId : getTemplateId;
//            var templateId = $stateParams.templateId = $stateParams.templateId ? $stateParams.templateId : getTemplateId;


            $http.get("admin/template/getProductTemplate/" + $stateParams.productId + "/" + $stateParams.accountId).success(function (response) {
                var getLastTemplateId = response;
                var templateId = getLastTemplateId ? getLastTemplateId.id : getProductTemplateId;
                $stateParams.templateId = templateId;
                try {
                    var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);

                    var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                } catch (e) {
                }
                // $stateParams.startDate = startDate;
                //$stateParams.endDate = endDate;
                // alert(startDate)
                //alert(endDate)
                defaultCalls();
            });
        });
    }

    function defaultCalls() {
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                templateId: $stateParams.templateId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "editWidget") {
            $state.go("index.editWidget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                tabId: $stateParams.tabId,
                widgetId: $stateParams.widgetId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.startDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "updateReportWidget") {
            $state.go("index.widgetEditByReport", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                reportWidgetId: $stateParams.reportWidgetId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "scheduler") {
            $state.go("index.schedulerIndex.scheduler", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "editOrNewScheduler") {
            $state.go("index.schedulerIndex.editOrNewScheduler", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                schedulerId: $stateParams.schedulerId,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "user") {
            $state.go("index.user", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "account") {
            $state.go("index.account", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "agency") {
            $state.go("index.agency", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "fieldSettings") {
            $state.go("index.fieldSettings", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate ? $stateParams.startDate : $scope.startDate,
                endDate: $stateParams.endDate ? $stateParams.endDate : $scope.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "favourites") {
            $state.go("index.favourites", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "viewFavouritesWidget") {
            $state.go("index.viewFavouritesWidget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                favouriteName: $stateParams.favouriteName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "oauth") {
            $state.go("index.oauth", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                favouriteName: $stateParams.favouriteName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "settings") {
            $state.go("index.settings", {
                lan: $stateParams.lan,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    }

    $scope.setProductByFav = function () {
        $scope.accountId = $stateParams.accountId;
        $scope.accountName = $stateParams.accountName;
        $scope.productId = $stateParams.productId;
        $scope.startDate = $stateParams.startDate;
        $scope.endDate = $stateParams.endDate;
    };

    $scope.loadNewUrl = function () {
        try {
            var startDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
            var endDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
        } catch (e) {
        }
        $stateParams.startDate = startDate;
        $stateParams.endDate = endDate;
        if ($scope.getCurrentPage() === "dashboard") {
            $state.go("index.dashboard." + $scope.getCurrentTab(), {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                templateId: $stateParams.templateId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "editWidget") {
            $state.go("index.editWidget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                tabId: $stateParams.tabId,
                widgetId: $stateParams.widgetId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "reports") {
            $state.go("index.report.reports", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                tabId: $stateParams.tabId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "newOrEdit") {
            $state.go("index.report.newOrEdit", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "updateReportWidget") {
            $state.go("index.widgetEditByReport", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                reportId: $stateParams.reportId,
                reportWidgetId: $stateParams.reportWidgetId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        }
//        else if ($scope.getCurrentPage() === "franchiseMarketing") {
//            $state.go("index.franchiseMarketing", {
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                productId: $stateParams.productId,
//                startDate: $scope.startDate,
//                endDate: $scope.endDate
//            });
//        }
        else if ($scope.getCurrentPage() === "dataSource") {
            $state.go("index.dataSource", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "dataSet") {
            $state.go("index.dataSet", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "scheduler") {
            $state.go("index.schedulerIndex.scheduler", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "editOrNewScheduler") {
            $state.go("index.schedulerIndex.editOrNewScheduler", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                schedulerId: $stateParams.schedulerId,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "user") {
            $state.go("index.user", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "account") {
            $state.go("index.account", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "agency") {
            $state.go("index.agency", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "fieldSettings") {
            $state.go("index.fieldSettings", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        }
//        else if ($scope.getCurrentPage() === "tag") {
//            $state.go("index.tag", {
//                accountId: $stateParams.accountId,
//                accountName: $stateParams.accountName,
//                startDate: $stateParams.startDate,
//                endDate: $stateParams.endDate
//            });
//        }
        else if ($scope.getCurrentPage() === "favourites") {
            $state.go("index.favourites", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "viewFavouritesWidget") {
            $state.go("index.viewFavouritesWidget", {
                lan: $stateParams.lan,
                accountId: $stateParams.accountId,
                accountName: $stateParams.accountName,
                productId: $stateParams.productId,
                //favouriteId: $stateParams.favouriteId,
                favouriteName: $stateParams.favouriteName,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else if ($scope.getCurrentPage() === "settings") {
            $state.go("index.settings", {
                lan: $stateParams.lan,
                startDate: $stateParams.startDate,
                endDate: $stateParams.endDate,
                compareStatus: $scope.selectedTablesType,
                compareStartDate: $scope.compare_startDate,
                compareEndDate: $scope.compare_endDate
            });
        } else {
            $location.path("/" + "?startDate=" + $('#startDate').val() + "&endDate=" + $('#endDate').val());
        }
    };
    $scope.userLogout = function () {
        window.location.href = "login.html";
        localStorageService.set("selectedTableType", "")
    };
    $scope.getCurrentPage = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "dashboard";
        }
        if (url.indexOf("dashboardTemplate") > 0) {
            return "dashboardTemplate";
        }
        if (url.indexOf("editWidget") > 0) {
            return "editWidget";
        }
        if (url.indexOf("newOrEdit") > 0) {
            return "newOrEdit";
        }
        if (url.indexOf("report") > 0) {
            return "reports";
        }
        if (url.indexOf("updateReportWidget") > 0) {
            return "updateReportWidget";
        }
//        if (url.indexOf("franchiseMarketing") > 0) {
//            return "franchiseMarketing";
//        }
        if (url.indexOf("dataSource") > 0) {
            return "dataSource";
        }
        if (url.indexOf("dataSet") > 0) {
            return "dataSet";
        }
        if (url.indexOf("editOrNewScheduler") > 0) {
            return "editOrNewScheduler";
        }
        if (url.indexOf("scheduler") > 0) {
            return "scheduler";
        }
        if (url.indexOf("user") > 0) {
            return "user";
        }
        if (url.indexOf("account") > 0) {
            return "account";
        }
        if (url.indexOf("agency") > 0) {
            return "agency";
        }
        if (url.indexOf("fieldSettings") > 0) {
            return "fieldSettings";
        }
        if (url.indexOf("favourites") > 0) {
            return "favourites";
        }
        if (url.indexOf("viewFavouritesWidget") > 0) {
            return "viewFavouritesWidget";
        }
        if (url.indexOf("settings") > 0) {
            return "settings";
        }
        if (url.indexOf("oauth") > 0) {
            return "oauth";
        }
//        if (url.indexOf("tag") > 0) {
//            return "tag";
//        }
        return "dashboard";
    };

    $scope.getCurrentTab = function () {
        var url = window.location.href;
        if (url.indexOf("widget") > 0) {
            return "widget";
        }
        return "widget";
    };
    function checkDate(date) {
        var d = new Date(date);
        if (d == "Invalid Date") {
            return false;
        } else {
            return true;
        }
    }
    function getChartColor() {
        $http.get("admin/ui/getChartColorByUserId").success(function (response) {
            $scope.chartColor = response;
            if (response.optionValue) {
                var userChatColor = response.optionValue.split(",");
                $scope.color = userChatColor[userChatColor.length - 1];
            } else {
                $scope.color = "#000000";
            }
        });
    }

    getChartColor();

    $scope.selectChartColor = function (color, chartColor) {
        if ($scope.chartColor.optionValue) {
            $scope.chartColor.optionValue = $scope.chartColor.optionValue + "," + color;
        } else {
            $scope.chartColor = {
                id: chartColor ? chartColor.id : null,
                optionName: 'Chart_Color_Options',
                optionValue: color,
                userId: chartColor ? chartColor.userId : null
            };
        }
    };

    $scope.themeDropDownChange = function (data) {
        var colorCode = {
            optionName: data.name,
            optionValue: data.value
        };
        $http({
            url: 'admin/ui/updateThemeSettings',
            method: 'POST',
            data: JSON.stringify(colorCode)
        }).success(function (response) {
            $scope.theme = {
                name: response.optionName,
                value: response.optionValue
            };
            var themeColor = {value: data.value};
            setThemeColor(themeColor);
        });
    };

    function getThemeColor() {
        $http.get("admin/ui/getThemeByUserId").success(function (response) {
            var data = {
                name: response.optionName,
                value: response.optionValue
            };
            $scope.theme = data;
            var themeColor = {value: response.optionValue};
            setThemeColor(themeColor);
        });
    }

    getThemeColor();

    function setThemeColor(themeColor) {
        $(document).ready(function (e) {
            $('head').append('<link rel="stylesheet" href="static/lib/css/' + themeColor.value + '/style.css" type="text/css" />');
        });
    }
    $(document).ready(function (e) {
        $(".inside").click(function (e) {
            e.stopPropagation();
        });
    });

    $scope.saveChartColor = function (userPreferences) {
        var data = {
            id: userPreferences.id,
            optionName: 'Chart_Color_Options',
            optionValue: userPreferences.optionValue,
            userId: userPreferences.userId
        };
        $http({method: userPreferences.id ? 'PUT' : 'POST', url: 'admin/ui/updateChartColor', data: data}).success(function (response) {
            getChartColor();
            $rootScope.getWidgetItem();
//            $scope.loadNewUrl();
        });
    };

    $(window).on('popstate', function () {
        if ($scope.showLangBtn === $stateParams.lan) {
            if ($stateParams.lan === 'en') {
                $scope.showLangBtn = localStorageService.get("agenLan");
            } else {
                $scope.showLangBtn = 'en';
            }
        }
        $scope.firstDate = $stateParams.startDate;
        $scope.lastDate = $stateParams.endDate;
    });

    $(document).ready(function (e) {
        $(document).on("click", ".calendar", function (e) {
            $("#dataRange").parent().addClass("open");
            $("#dataRange").attr("aria-expanded", "true");
            e.stopPropagation();
        });

        $(document).on("click", ".daterangepicker", function (e) {
            $("#dataRange").parent().addClass("open");
            $("#dataRange").attr("aria-expanded", "true");
            e.stopPropagation();
        });

        $(document).on("click", ".available", function (e) {
            $("#dataRange").parent().addClass("open");
            $("#dataRange").attr("aria-expanded", "true");
            e.stopPropagation();
        });

        $(document).on("click", ".ranges > ul > li", function (e) {
            $("#dataRange").parent().addClass("open");
            $("#dataRange").attr("aria-expanded", "true");
            e.stopPropagation();
        });

        $(document).on('keyup', "input[name=daterangepicker_start]", function () {
            var date_start = $(this).val();
            var date_end = $("input[name=daterangepicker_end]").val();
            var dateStartSecondSlashIndex = date_start.lastIndexOf("/");
            var checkStartYear = date_start.substring(dateStartSecondSlashIndex + 1, date_start.length);
            var regExp = /(\d{1,2})\/(\d{1,2})\/(\d{2,4})/;
            if (!checkDate(date_start) || checkStartYear.length != 4 || parseInt(date_start.replace(regExp, "$3$1$2")) > parseInt(date_end.replace(regExp, "$3$1$2"))) {
                $(".applyBtn").prop('disabled', true);
            } else {
                $(".applyBtn").prop('disabled', false);
            }
        });

        $(document).on('keyup', "input[name=daterangepicker_end]", function () {
            var date_end = $(this).val();
            var date_start = $("input[name=daterangepicker_start]").val();
            var dateEndSecondSlashIndex = date_end.lastIndexOf("/");
            var checkEndYear = date_end.substring(dateEndSecondSlashIndex + 1, date_end.length);
            var regExp = /(\d{1,2})\/(\d{1,2})\/(\d{2,4})/;
            if (!checkDate(date_end) || checkEndYear.length != 4 || parseInt(date_start.replace(regExp, "$3$1$2")) > parseInt(date_end.replace(regExp, "$3$1$2"))) {
                $(".applyBtn").prop('disabled', true);
            } else {
                $(".applyBtn").prop('disabled', false);
            }
        });

        $(document).on('click', '.applyBtn', function () {
//            $scope.loadNewUrl();
        });
        $(".ranges ul").find("li").addClass("custom-picker-dashboard");
        $(document).on("click", ".ranges ul li", function (e) {
            // $scope.loadNewUrl();
        });
        //Initialize Select2 Elements
        $(".select2").select2();
        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();
        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        $('#daterange-btn').daterangepicker(
                {
                    ranges: {
                        'Today': [moment(), moment()],
                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                        'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                        'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                        'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'dayswidgetTableDateRange'), moment().endOf(new Date())],
                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().startOf('week')],
//                        'Last 2 Weeks (Sun - Sat)': [moment().subtract(2, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').add(1, 'days')],
//                        'Last Business Week (Mon - Fri)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').subtract(1, 'days')],
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 2 Months': [moment().subtract(2, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 3 Months' : [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                        'This Year': [moment().startOf('year'), moment().endOf(new Date())],
                        'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')],
//                        'Last 2 Years': [moment().subtract(2, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
//                        'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: $stateParams.startDate ? $stateParams.startDate : moment().subtract(30, 'days'),
                    endDate: $stateParams.endDate ? $stateParams.endDate : moment().subtract(1, 'days'),
                    maxDate: new Date()
                },
                function (startDate, endDate, ranges) {
                    $('#daterange-btn span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                    var compareDate = getComparisonDate(ranges, startDate.format('YYYY-MM-DD'), endDate.format('YYYY-MM-DD'));
                    $('#compare-daterange-btn span').html(compareDate.autoCompareStartDate + ' - ' + compareDate.autoCompareEndDate);
                    $('#compare-daterange-btn').daterangepicker(
                            {
                                ranges: {
                                    'Today': [moment(), moment()],
                                    'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                                    'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                                    'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                                    'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
                                    'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                                'This Week (Mon - Today)': [moment().startOf('week').add(1, 'dayswidgetTableDateRange'), moment().endOf(new Date())],
                                    'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().startOf('week')],
//                                'Last 2 Weeks (Sun - Sat)': [moment().subtract(2, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                                'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').add(1, 'days')],
//                                'Last Business Week (Mon - Fri)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').subtract(1, 'days')],
                                    'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                                    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                                'Last 2 Months': [moment().subtract(2, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                                'Last 3 Months' : [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                                    'This Year': [moment().startOf('year'), moment().endOf(new Date())],
                                    'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')],
//                                'Last 2 Years': [moment().subtract(2, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
//                                'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
                                    'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                                    'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                                },
                                startDate: compareDate.autoCompareStartDate,
                                endDate: compareDate.autoCompareEndDate,
                                maxDate: new Date()
                            },
                            function (startDate, endDate) {
                                $('#compare-daterange-btn span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                            }
                    );
                });
        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });
        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal,  input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();
        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });

        //$("#config-demo").click(function (e) {

        $(document).on('click', '.table-condensed .month', function () {
            var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var selectedMonth = $(this).text();
            var splitMY = selectedMonth.split(" ");
            var monthvalue = $.inArray(splitMY[0], months);
            var FirstDay = new Date(splitMY[1], monthvalue, 1);
            var LastDay = new Date(splitMY[1], monthvalue + 1, 0);

            $("input[name='daterangepicker_start']").daterangepicker({
                singleDatePicker: false,
                startDate: FirstDay
            });

            $("input[name='daterangepicker_end']").daterangepicker({
                singleDatePicker: false,
                startDate: LastDay
            });
        });
    });

    function getComparisonDate(range, startDate, endDate) {
        var date1 = new Date(startDate);
        var date2 = new Date(endDate);
        var dayDiff = new Date(date2 - date1);
        dayDiff = (dayDiff / 1000 / 60 / 60 / 24) + 1;
        var dateRange = {};
        if ((range === 'This Year') || (range === 'Last Year')) {
            var first = date1;
            var last = date2;
            var firstYear = first.getFullYear() - 1;
            first.setFullYear(firstYear);
            var lastYear = last.getFullYear() - 1;
            last.setFullYear(lastYear);
        } else {
            var first = new Date(date1.getTime() - (dayDiff * 24 * 60 * 60 * 1000));
            var last = new Date(date2.getTime() - (dayDiff * 24 * 60 * 60 * 1000));
        }
        dateRange.autoCompareStartDate = (first.getMonth() + 1) + '-' + first.getDate() + '-' + first.getFullYear();
        dateRange.autoCompareEndDate = (last.getMonth() + 1) + '-' + last.getDate() + '-' + last.getFullYear();
        return dateRange;
    }

    var tableTypeByDateRange = localStorageService.get("selectedTableType") ? localStorageService.get("selectedTableType") : "compareOff";
    if (tableTypeByDateRange == 'compareOn') {
        $scope.selectedTablesType = 'compareOn';
        $scope.compareDateRangeType = true;
    } else {
        $scope.selectedTablesType = 'compareOff';
        $scope.compareDateRangeType = false;
    }
//    localStorageService.set("loadStatus", true);
    $scope.getTableType = tableTypeByDateRange ? tableTypeByDateRange : "compareOff";
//    $rootScope.loadStatus=true;
    $scope.selectTableOptions = function (type, loadStatus) {
        if (loadStatus == true) {
            $scope.getTableType = "";
//            $rootScope.loadStatus="";
        }
        $scope.firstDate = moment($('#daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY');
        $scope.lastDate = moment($('#daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY');
//        $('#daterange-btn').daterangepicker().entries(ranges)
//        $stateParams.startDate = $scope.firstDate;
//        $stateParams.endDate = $scope.lastDate;
        var selectTableType;
        if (type == true) {
            selectTableType = "compareOn";
        } else if (type == false) {
            selectTableType = "compareOff";
        }
        $scope.compareDateRange = {
            startDate: $scope.compare_startDate,
            endDate: $scope.compare_endDate
        };
        localStorageService.set("comparisonStartDate", $scope.compare_startDate);
        localStorageService.set("comparisonEndDate", $scope.compare_endDate);
//       
        localStorageService.set("selectedTableType", selectTableType);
//        localStorageService.set("loadStatus", loadStatus);
//            
        $scope.getTableType = selectTableType;
        $scope.selectedTablesType = selectTableType;
        $scope.loadNewUrl();
//        $rootScope.$broadcast('loadStatusChanged', loadStatus);
//            

    };



    var compareSTDate = localStorageService.get("compareStartDate");
    var compareENDate = localStorageService.get("compareEndDate");
    $scope.compare_startDate = compareSTDate ? compareSTDate : $scope.getDay().toLocaleDateString("en-US");
    $scope.compare_endDate = compareENDate ? compareENDate : new Date().toLocaleDateString("en-US");

    if (!compareSTDate) {
        localStorageService.set("compareStartDate", $scope.compare_startDate);
    }

    if (!compareENDate) {
        localStorageService.set("compareEndDate", $scope.compare_endDate);
    }

    $scope.compareDateRange = {
        startDate: $scope.compare_startDate,
        endDate: $scope.compare_endDate
    };



    $(document).ready(function () {
        var text = $(".ranges > ul >li:contains('Custom Range')").index();
        $(".ranges > ul > li:eq(" + text + ")").click(function (e) {

        });

        $('.dropdown-submenu').on("click", function (e) {
            $(this).next('ul').toggle();
            e.stopPropagation();
            e.preventDefault();
        });
    });
    $(document).ready(function (e) {
        $(document).on('click', '.applyBtn', function () {
            try {
                $scope.compareStartDate = moment($('#compare-daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#compare-daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
                $scope.compareEndDate = moment($('#compare-daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#compare-daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                localStorageService.set("compareStartDate", $scope.compareStartDate);
                localStorageService.set("compareEndDate", $scope.compareEndDate);
                $scope.compare_startDate = $scope.compareStartDate;
                $scope.compare_endDate = $scope.compareEndDate;
            } catch (e) {
            }
        });

        $(".ranges ul").find("li").addClass("compare-custom-picker-dashboard");
        $(document).on("click", ".ranges ul li", function (e) {
            try {
                $scope.compareStartDate = moment($('#compare-daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') ? moment($('#compare-daterange-btn').data('daterangepicker').startDate).format('MM/DD/YYYY') : $scope.firstDate;//$scope.startDate.setDate($scope.startDate.getDate() - 1);
                $scope.compareEndDate = moment($('#compare-daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') ? moment($('#compare-daterange-btn').data('daterangepicker').endDate).format('MM/DD/YYYY') : $scope.lastDate;
                localStorageService.set("compareStartDate", $scope.compareStartDate);
                localStorageService.set("compareEndDate", $scope.compareEndDate);
                $scope.compare_startDate = $scope.compareStartDate;
                $scope.compare_endDate = $scope.compareEndDate;
            } catch (e) {
            }
            //$scope.loadNewUrl();
        });
        //Initialize Select2 Elements
        $(".select2").select2();
        //Datemask dd/mm/yyyy
        $("#datemask").inputmask("dd/mm/yyyy", {"placeholder": "dd/mm/yyyy"});
        //Datemask2 mm/dd/yyyy
        $("#datemask2").inputmask("mm/dd/yyyy", {"placeholder": "mm/dd/yyyy"});
        //Money Euro
        $("[data-mask]").inputmask();
        //Date range picker
        $('#reservation').daterangepicker();
        //Date range picker with time picker
        $('#reservationtime').daterangepicker({timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'});
        //Date range as a button
        //Date picker
        $('#compare-daterange-btn').daterangepicker(
                {
                    ranges: {
                        'Today': [moment(), moment()],
                        'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                        'Last 7 Days': [moment().subtract(7, 'days'), moment().subtract(1, 'days')],
                        'Last 14 Days ': [moment().subtract(14, 'days'), moment().subtract(1, 'days')],
                        'Last 30 Days': [moment().subtract(30, 'days'), moment().subtract(1, 'days')],
                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'days'), moment().endOf(new Date())],
//                        'This Week (Mon - Today)': [moment().startOf('week').add(1, 'dayswidgetTableDateRange'), moment().endOf(new Date())],
                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().startOf('week')],
//                        'Last 2 Weeks (Sun - Sat)': [moment().subtract(2, 'week').startOf('week'), moment().subtract(1, 'week').endOf('week')],
//                        'Last Week (Mon - Sun)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').add(1, 'days')],
//                        'Last Business Week (Mon - Fri)': [moment().subtract(1, 'week').startOf('week').add(1, 'days'), moment().subtract(1, 'week').add(1, 'days').endOf('week').subtract(1, 'days')],
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 2 Months': [moment().subtract(2, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
//                        'Last 3 Months' : [moment().subtract(3, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                        'This Year': [moment().startOf('year'), moment().endOf(new Date())],
                        'Last Year': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')],
//                        'Last 2 Years': [moment().subtract(2, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
//                        'Last 3 Years': [moment().subtract(3, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
                        'This Month': [moment().startOf('month'), moment().endOf(new Date())],
                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
                    },
                    startDate: $scope.compare_startDate ? $scope.compare_startDate : moment().subtract(30, 'days'),
                    endDate: $scope.compare_endDate ? $scope.compare_endDate : moment().subtract(1, 'days'),
                    maxDate: new Date()
                },
                function (startDate, endDate) {
                    $('#compare-daterange-btn span').html(startDate.format('MM-DD-YYYY') + ' - ' + endDate.format('MM-DD-YYYY'));
                });

        //Date picker
        $('#datepicker').datepicker({
            autoclose: true
        });
        //iCheck for checkbox and radio inputs
        $('input[type="checkbox"].minimal,  input[type="radio"].minimal').iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        //Red color scheme for iCheck
        $('input[type="checkbox"].minimal-red, input[type="radio"].minimal-red').iCheck({
            checkboxClass: 'icheckbox_minimal-red',
            radioClass: 'iradio_minimal-red'
        });
        //Flat red color scheme for iCheck
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
        //Colorpicker
        $(".my-colorpicker1").colorpicker();
        //color picker with addon
        $(".my-colorpicker2").colorpicker();
        //Timepicker
        $(".timepicker").timepicker({
            showInputs: false
        });

        //$("#config-demo").click(function (e) {

        $(document).on('click', '.table-condensed .month', function () {
            var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
            var selectedMonth = $(this).text();
            var splitMY = selectedMonth.split(" ");
            var monthvalue = $.inArray(splitMY[0], months);
            var FirstDay = new Date(splitMY[1], monthvalue, 1);
            var LastDay = new Date(splitMY[1], monthvalue + 1, 0);

            $("input[name='daterangepicker_start']").daterangepicker({
                singleDatePicker: false,
                startDate: FirstDay
            });

            $("input[name='daterangepicker_end']").daterangepicker({
                singleDatePicker: false,
                startDate: LastDay
            });

        });
    });
});
app.filter('filterLanguage', [function ($http) {
        return function (lan) {
            var langs = [{name: 'English', type: 'en'}, {name: 'Chinese', type: 'cn'}, {name: "Spanish", type: "sp"}];
            var returnFilterLang;
            var returnLan = $.grep(langs, function (value) {
                return value.type == lan;
            });
            angular.forEach(returnLan, function (val, k) {
                returnFilterLang = val.name;
            });
            return returnFilterLang;
        };
    }]);
