
app.config(function ($stateProvider, $urlRouterProvider, $routeProvider) {
    $stateProvider
            .state("index", {
                url: "/index",
                templateUrl: "static/views/vb.index.html"
                        //controller: "IndexController"
            })
            .state("index.dashboard", {
                url: "/dashboard/:lan/:accountId/:accountName/:productId/:templateId",
                templateUrl: "static/views/dashboard/dashboard.html",
                controller: "UiController"
            })

            .state("index.dashboard.widget", {
                url: "/widget/:tabId?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/dashboard/widgets.html",
                controller: 'WidgetController'
            })
            .state("index.editWidget", {
                url: "/editWidget/:lan/:accountId/:accountName/:productId/:tabId/:widgetId?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/dashboard/editWidget.html",
                controller: 'EditWidgetController'
            })
            .state("index.report", {
                url: "/reportIndex/:lan/:accountId/:accountName",
                templateUrl: "static/views/reports/reportIndex.html",
                controller: 'ReportIndexController'
            })
            .state("index.report.reports", {
                url: "/report?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/reports/reports.html",
                controller: 'ReportController',
                activetab: 'report'
            })
            .state("index.report.newOrEdit", {
                url: "/newOrEdit/:reportId?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/reports/newOrEditReports.html",
                controller: 'NewOrEditReportController',
                activetab: 'report'
            })
            .state("index.widgetEditByReport", {
                url: "/updateReportWidget/:lan/:accountId/:accountName/:reportId/:reportWidgetId?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/reports/editReportWidget.html",
                controller: 'WidgetEditReportController'
            })
//            .state("index.report.template", {
//                url: "/template/:accountId/:accountName/:reportId?:startDate/:endDate",
//                templateUrl: "static/views/reports/reportTemplate.html",
//                controller: 'TemplateController',
//                activetab: 'template'
//            })
            .state("index.dataSource", {
                url: "/dataSource/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/source/dataSource.html",
                controller: 'DataSourceController'
            })
            .state("index.dataSet", {
                url: "/dataSet/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/source/dataSet.html",
                controller: 'DataSetController'
            })
//            .state("index.franchiseMarketing", {
//                url: "/franchiseMarketing/:locationId?:startDate/:endDate",
//                templateUrl: "static/views/franchiseMarketing/franchiseMarketing.html",
//                controller: 'FranchiseMarketingController'
//            })
            .state("index.schedulerIndex", {
                url: "/schedulerIndex/:lan/:accountId/:accountName",
                templateUrl: "static/views/scheduler/schedulerIndex.html"
//                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.scheduler", {
                url: "/scheduler?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/scheduler/scheduler.html",
                controller: 'SchedulerController'
            })
            .state("index.schedulerIndex.editOrNewScheduler", {
                url: "/editOrNewScheduler/:schedulerId?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/scheduler/newOrEditScheduler.html",
                controller: 'NewOrEditSchedulerController'
            })
            .state("index.user", {
                url: "/user/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/user.html",
                controller: 'UserController'
            })
            .state("index.account", {
                url: "/account/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/account.html",
                controller: 'AccountController'
            })
            .state("index.agency", {
                url: "/agency/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/agency.html",
                controller: 'AgencyController'
            })
            .state("index.fieldSettings", {
                url: "/fieldSettings/:lan/:accountId/:accountName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/fieldSettings/fieldSettings.html",
                controller: 'FieldSettingsController'
            })
//            .state("index.favourites", {
//                url: "/favourites/:accountId/:accountName?:startDate/:endDate",
//                templateUrl: "static/views/admin/favourites.html",
//                controller: 'FavouritesController'
//            })
            .state("index.viewFavouritesWidget", {
                url: "/viewFavouritesWidget/:lan/:accountId/:accountName/:productId/:favouriteName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/viewFavouritesWidget.html",
                controller: 'ViewFavouritesWidgetController'
            })
            .state("index.oauth", {
                url: "/oauth/:lan/:accountId/:accountName/:productId/:favouriteName?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/oauth.html",
                controller: 'socialController'
            })
            .state("index.settings", {
                url: "/settings?:startDate/:endDate/:compareStatus/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/admin/settings.html",
                controller: 'SettingsController'
            })
            .state("viewPdf", {
                url: "/viewPdf/:lan/:agencyId/:accountId/:accountName/:productId/:templateId/:userId/:tabId?:startDate/:endDate/:getTableType/:productName",
                templateUrl: "static/views/pdf/vb.pdf.html",
                controller: 'PdfController'
            })
            .state("viewReportPdf", {
                url: "/viewReportPdf/:lan/:accountId/:userId/:reportId?:startDate/:endDate/:getTableType/:compareStartDate/:compareEndDate",
                templateUrl: "static/views/pdf/vb.reportPdf.html",
                controller: 'ReportPdfController'
            })
            .state("viewFavouritesPdf", {
                url: "/viewFavouritesPdf/:lan/:accountId/:userId/:favouriteName?:startDate/:endDate/:getTableType",
                templateUrl: "static/views/pdf/vb.favouritesPdf.html",
                controller: 'FavouritesPdfController'
            });
//            .state("index.viewFavouritesWidget", {
//                url: "/viewFavouritesWidget/:accountId/:accountName/:favouriteId/:favouriteName?:startDate/:endDate",
//                templateUrl: "static/views/admin/viewFavouritesWidget.html",
//                controller: 'ViewFavouritesWidgetController'
//            });


    $urlRouterProvider.otherwise(function ($injector) {
        $injector.get('$state').go('index.dashboard');
    });
//    $routeProvider.when('/viewPdf', {
//        url: '/viewPdf/:accountId/:accountName/:tabId',
//        templateUrl: 'static/views/pdf/vb.pdf.html'});
//    $urlRouterProvider.otherwise('index/dashboard/1/1');
});
app.run(['$window', '$rootScope', '$stateParams', '$state',
    function ($window, $rootScope, $stateParams, $state) {
        //$rootScope.accountNameByPdf = $stateParams.accountName; 

        $rootScope.goBack = function () {
            $window.history.back();
        };
        $rootScope.setParamByTemplateId = function (template) {
            if ($stateParams.templateId != template.id) {
                $state.go("index.dashboard.widget", {
                    accountId: $stateParams.accountId,
                    accountName: $stateParams.accountName,
                    productId: template.agencyProductId.id,
                    templateId: template.id,
                    tabId: 0,
                    startDate: $stateParams.startDate,
                    endDate: $stateParams.endDate
                });
            }
            $rootScope.setTmpIdByTab(template);
            $stateParams.templateId = template.id;
        };
        $rootScope.setTmpIdByTab = function (template) {
            $stateParams.templateId = template.id;
            this.rootTemplateId = template.id; //$stateParams.templateId;
        };
    }]);
