/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.apiconnector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.analyticsreporting.v4.AnalyticsReporting;
import com.google.api.services.analyticsreporting.v4.model.ColumnHeader;
import com.google.api.services.analyticsreporting.v4.model.DateRange;
import com.google.api.services.analyticsreporting.v4.model.DateRangeValues;
import com.google.api.services.analyticsreporting.v4.model.Dimension;
import com.google.api.services.analyticsreporting.v4.model.GetReportsRequest;
import com.google.api.services.analyticsreporting.v4.model.GetReportsResponse;
import com.google.api.services.analyticsreporting.v4.model.Metric;
import com.google.api.services.analyticsreporting.v4.model.MetricHeaderEntry;
import com.google.api.services.analyticsreporting.v4.model.OrderBy;
import com.google.api.services.analyticsreporting.v4.model.Report;
import com.google.api.services.analyticsreporting.v4.model.ReportRequest;
import com.google.api.services.analyticsreporting.v4.model.ReportRow;
import com.netphenix.npim.utils.service.DateUtils;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.netphenix.npim.bean.GaReport;
//import com.visumbu.vb.model.DefaultFieldProperties;
import com.netphenix.npim.rest.service.ApiUtils;
import com.netphenix.npim.rest.service.Rest;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class GaService {

    private static final String URL = "https://analyticsreporting.googleapis.com/v4/reports:batchGet";

    public Map<String, List<Map<String, Object>>> getGaReport(String reportName, String analyticsProfileId, Date startDate, Date endDate, String reqDimensions, String reqProductSegments, Integer dataSetId, Integer dataSourceId, String accessToken) {
        Map<String, GaReport> gaReports = ApiUtils.getAllGaReports();
        System.out.println("Ga Reports -----> " + gaReports);
        GaReport gaReport = gaReports.get(reportName);
        System.out.println("Ga Report ----> " + gaReport);
        String metricsList = gaReport.getFields();
        System.out.println("MetricList ----> " + metricsList);
        String productSegments = reqProductSegments == null ? null : reqProductSegments;
        if (productSegments == null || productSegments.trim().isEmpty() || productSegments.trim().equalsIgnoreCase("none")) {
            productSegments = null;
        }
        String dimensions = reqDimensions;
        if (dimensions == null || dimensions.trim().isEmpty() || dimensions.trim().equalsIgnoreCase("none")) {
            dimensions = gaReport.getDefaultDimension();
        }
        if (dimensions == null || dimensions.trim().isEmpty() || dimensions.trim().equalsIgnoreCase("none")) {
            dimensions = null;
        }
        String filter = gaReport.getDefaultFilter();
        System.out.println("Metric List " + metricsList + " Product Segments " + productSegments + " dimensions " + dimensions + " Filter " + filter);
        Map<String, List<Map<String, Object>>> gaData = getGenericData(analyticsProfileId, startDate, endDate, null, null, dataSourceId, metricsList, dimensions, productSegments, filter, accessToken);
        return gaData;
    }

    public Map<String, List<Map<String, Object>>> getGenericData(String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2, Integer dataSourceId, String metrics, String dimentions, String productSegments, String filter, String accessToken) {
        System.out.println(viewId);
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }

            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray[0].isEmpty() || nameAliasArray[0].equalsIgnoreCase("none")) {
                    continue;
                }
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            List<Dimension> dimensionList = new ArrayList<>();
            System.out.println("in ga service dimension---------->" + dimentions);
            if (dimentions != null) {
                String[] dimensionArray = dimentions.split(";");
                for (int i = 0; i < dimensionArray.length; i++) {
                    String dimensionStr = dimensionArray[i];
                    if (dimensionStr.isEmpty() || dimensionStr.equalsIgnoreCase("none")) {
                        continue;
                    }
                    Dimension dimension = new Dimension()
                            .setName(dimensionStr);
                    dimensionList.add(dimension);

                }
            }
            if (productSegments != null) {
                String[] productSegmentArray = productSegments.split(";");
                for (int i = 0; i < productSegmentArray.length; i++) {
                    String productSegment = productSegmentArray[i];
                    String[] nameAliasArray = productSegment.split(",");
                    if (nameAliasArray[0].isEmpty() || nameAliasArray[0].equalsIgnoreCase("none")) {
                        continue;
                    }
                    if (nameAliasArray.length >= 2) {
                        Dimension dimension = new Dimension()
                                .setName(nameAliasArray[0]);
                        dimensionList.add(dimension);
                    } else if (nameAliasArray.length >= 1) {
                        Dimension dimension = new Dimension()
                                .setName(nameAliasArray[0]);
                        dimensionList.add(dimension);
                    }
                }
            }
            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    //                    .setDimensions(dimensionList)
                    .setFiltersExpression(filter)
                    .setMetrics(metricList)
                    .setPageSize(100000);
            if (filter != null) {
                request.setFiltersExpression(filter);
            }
//            if (dimensionList != null && !dimensionList.isEmpty()) {
//                request.setDimensions(dimensionList);
//            }
            ArrayList<ReportRequest> requests = new ArrayList<>();
            requests.add(request);
            System.out.println("requests------->" + dimensionList);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);
            ObjectMapper objMapper = new ObjectMapper();

//            List<TokenDetails> TokenDetails = resourceManager.getOauthToken(1, Integer.parseInt(dataSourceId));
//            String accessToken = TokenDetails.get(0).getTokenValue();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("access_token", accessToken);

            String response = Rest.postWithBody(URL, params, objMapper.writeValueAsString(getReport));
            // Call the batchGet method.
            System.out.println("get Report ---> " + objMapper.writeValueAsString(getReport));
            //            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();
//            BatchGet request23 = analyticsReporting.reports().batchGet(getReport).setAccessToken("");
//            GetReportsResponse response = request23.execute();
// Return the response.
            return getResponseAsMap(response);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map<String, List<Map<String, Object>>> getGenericData(String viewId, Date startDate1, Date endDate1, Date startDate2, Date endDate2,
            String metrics, String dimentions, String filter, String orderBy, Integer maxResults) {
        try {
            List<DateRange> dateRangeList = new ArrayList<>();
            DateRange dateRange = new DateRange();
            dateRange.setStartDate(DateUtils.getGaStartDate(startDate1));
            dateRange.setEndDate(DateUtils.getGaEndDate(endDate1));
            dateRangeList.add(dateRange);

            if (startDate2 != null) {
                DateRange dateRange1 = new DateRange();
                dateRange1.setStartDate(DateUtils.getGaStartDate(startDate2));
                if (endDate2 != null) {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(endDate2));
                } else {
                    dateRange1.setEndDate(DateUtils.getGaEndDate(startDate1));
                }
                dateRangeList.add(dateRange1);
            }
            String[] metricsArray = metrics.split(";");
            List<Metric> metricList = new ArrayList<>();
            for (int i = 0; i < metricsArray.length; i++) {
                String metricStr = metricsArray[i];
                String[] nameAliasArray = metricStr.split(",");
                if (nameAliasArray.length >= 2) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0])
                            .setAlias(nameAliasArray[1]);
                    metricList.add(metric);
                } else if (nameAliasArray.length >= 1) {
                    Metric metric = new Metric()
                            .setExpression(nameAliasArray[0]);
                    metricList.add(metric);
                }
            }

            String[] dimensionArray = dimentions.split(";");
            List<Dimension> dimensionList = new ArrayList<>();
            for (int i = 0; i < dimensionArray.length; i++) {
                String dimensionStr = dimensionArray[i];
                Dimension dimension = new Dimension()
                        .setName(dimensionStr);
                dimensionList.add(dimension);

            }
            List<OrderBy> orderByList = new ArrayList<>();
            String[] orderByArr = orderBy.split(";");
            for (int i = 0; i < orderByArr.length; i++) {
                String orderByStr = orderByArr[i];
                OrderBy orderByVal = new OrderBy();
                orderByVal.setFieldName(orderByStr);
                orderByVal.setSortOrder("DESCENDING");
                orderByList.add(orderByVal);
            }

            ReportRequest request = new ReportRequest()
                    .setViewId(viewId)
                    .setDateRanges(dateRangeList)
                    .setDimensions(dimensionList)
                    .setPageSize(maxResults)
                    .setFiltersExpression(filter)
                    .setOrderBys(orderByList)
                    .setMetrics(metricList);
            ArrayList<ReportRequest> requests = new ArrayList<>();
            requests.add(request);
            // Create the GetReportsRequest object.
            GetReportsRequest getReport = new GetReportsRequest()
                    .setReportRequests(requests);
            String reportRequest = requests.toString();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("access_token", "ya29.GlufBZZBgc7h7LWYOFwHLG8fcb83xws6dQ3tniCX-xahefWM69uLHr4dy6Y9EkNs5pRf84p89_JE6R638AitUBmLf7B3-r-Er3QaE412kzI05itruqLWJHi4nZ2G");

            String response = Rest.postWithBody(URL, params, reportRequest);
            // Call the batchGet method.
//            GetReportsResponse response = analyticsReporting.reports().batchGet(getReport).execute();

            // Return the response.
            return getResponseAsMap(response);
        } catch (Exception ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Map<String, List<Map<String, Object>>> getResponseAsMap(String response) {
        try {
            Map returnMap = new HashMap();

            System.out.println("report--------->" + response);
            List<String> dimensionHeaders = ApiUtils.getJsonArray(response, "$..dimensions");
            JSONArray headerValue = ApiUtils.getJsonArray(response, "$..metricHeaderEntries");
            List<MetricHeaderEntry> metricHeaders = new ArrayList<>();
            for (Iterator<JSONObject> iterator = headerValue.iterator(); iterator.hasNext();) {
                JSONObject next = iterator.next();
                Object name = next.get("name");
                Object type = next.get("type");
                MetricHeaderEntry entry = new MetricHeaderEntry();
                entry.set("name", name);
                entry.set("type", type);
                metricHeaders.add(entry);
            }
//                List<ReportRow> rowsold = report.getData().getRows();
//                System.out.println("rows original--------->"+rowsold);
//            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
            JSONArray allReports = ApiUtils.getJsonArray(response, "$..rows");
            List<ReportRow> rows = new ArrayList<>();
            for (Iterator<JSONObject> iterator = allReports.iterator(); iterator.hasNext();) {
                JSONObject next = iterator.next();
                List<String> dimensions = (List<String>) next.get("dimensions");
                Object allMetrics = next.get("metrics");
                JSONArray metrics = ApiUtils.getJsonArray(allMetrics.toString(), "$..values");
                List<String> valueList = new ArrayList<>();
                List<DateRangeValues> dateRangeList = new ArrayList<>();
                DateRangeValues dateRangeValues = new DateRangeValues();
                for (Iterator<String> metricesIterator = metrics.iterator(); metricesIterator.hasNext();) {
                    valueList.add(metricesIterator.next());
                }
                dateRangeValues.setValues(valueList);
                dateRangeList.add(dateRangeValues);
                ReportRow entry = new ReportRow();
                entry.setDimensions(dimensions);
                entry.setMetrics(dateRangeList);
                rows.add(entry);
            }
//            List<ColumnDef> columnDefs = new ArrayList<>();
//            if (dimensionHeaders != null) {
//                for (int i = 0; i < dimensionHeaders.size(); i++) {
//                    String key = dimensionHeaders.get(i);
//                    key = key.replaceAll("ga:", "");
//                    DefaultFieldProperties fieldProperties = uiDao.getDefaultFieldProperties(key);
//                    if (fieldProperties != null) {
//                        columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
//                    } else {
//                        columnDefs.add(new ColumnDef(key, "string", key));
//                    }
//                }
//            }
//            for (int i = 0; i < metricHeaders.size(); i++) {
//                String key = metricHeaders.get(i).getName();
//                key = key.replaceAll("ga:", "");
//                String type = metricHeaders.get(i).getType();
//                DefaultFieldProperties fieldProperties = uiDao.getDefaultFieldProperties(key);
//                if (fieldProperties != null) {
//                    columnDefs.add(new ColumnDef(key, fieldProperties.getDataType() == null ? "string" : fieldProperties.getDataType(), fieldProperties.getDisplayName(), fieldProperties.getAgregationFunction(), fieldProperties.getDisplayFormat()));
//                } else {
//                    columnDefs.add(new ColumnDef(key, "string", key));
//                }
//            }
//            returnMap.put("columnDefs", columnDefs);
            if (rows == null) {
                System.out.println("No data found for ");
                return new HashMap();
            }
            List<Map<String, Object>> data = new ArrayList<>();
            for (ReportRow row : rows) {
                List<String> dimensions = row.getDimensions();
                List<DateRangeValues> metrics = row.getMetrics();
                Map<String, Object> dataMap = new HashMap();
                if (dimensionHeaders != null) {
                    for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
                        System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
                        String key = dimensionHeaders.get(i);
                        key = key.replaceAll("ga:", "");
                        dataMap.put(key, dimensions.get(i));
                    }
                }
                for (int j = 0; j < metrics.size(); j++) {
                    System.out.print("Date Range (" + j + "): ");
                    DateRangeValues values = metrics.get(j);
                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
                        String key = metricHeaders.get(k).getName();
                        key = key.replaceAll("ga:", "");
                        dataMap.put(key, values.getValues().get(k));
                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
                    }
                }
                data.add(dataMap);
            }
            returnMap.put("data", data);
            return returnMap;
        } catch (Exception ex) {
            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Parses and prints the Analytics Reporting API V4 response.
     *
     * @param response the Analytics Reporting API V4 response.
     */
//    private static void printResponse(GetReportsResponse response) {
//
//        for (Report report : response.getReports()) {
//            ColumnHeader header = report.getColumnHeader();
//            List<String> dimensionHeaders = header.getDimensions();
//            List<MetricHeaderEntry> metricHeaders = header.getMetricHeader().getMetricHeaderEntries();
//            List<ReportRow> rows = report.getData().getRows();
//
//            if (rows == null) {
//                System.out.println("No data found for ");
//                return;
//            }
//
//            for (ReportRow row : rows) {
//                List<String> dimensions = row.getDimensions();
//                List<DateRangeValues> metrics = row.getMetrics();
//                for (int i = 0; i < dimensionHeaders.size() && i < dimensions.size(); i++) {
//                    System.out.println(dimensionHeaders.get(i) + ": " + dimensions.get(i));
//                }
//
//                for (int j = 0; j < metrics.size(); j++) {
//                    System.out.print("Date Range (" + j + "): ");
//                    DateRangeValues values = metrics.get(j);
//                    for (int k = 0; k < values.getValues().size() && k < metricHeaders.size(); k++) {
//                        System.out.println(metricHeaders.get(k).getName() + ": " + values.getValues().get(k));
//                    }
//                }
//            }
//        }
//    }
    /**
     * Query the Analytics Reporting API V4. Constructs a request for the
     * sessions for the past seven days. Returns the API response.
     *
     * @param service
     * @return GetReportResponse
     * @throws IOException
     */
    private static GetReportsResponse getReport(AnalyticsReporting service) throws IOException {
        // Create the DateRange object.
        List<DateRange> dateRangeList = new ArrayList<>();
// The start date for the query in the format `YYYY-MM-DD`.
        DateRange dateRange = new DateRange();
        dateRange.setStartDate("7DaysAgo");
        dateRange.setEndDate("today");

        dateRangeList.add(dateRange);
        DateRange dateRange1 = new DateRange();
        dateRange1.setStartDate("14DaysAgo");
        dateRange1.setEndDate("7DaysAgo");
        dateRangeList.add(dateRange1);
        // Create the Metrics object.
        Metric sessions = new Metric()
                .setExpression("ga:sessions")
                .setAlias("sessions");

        //Create the Dimensions object.
        Dimension browser = new Dimension()
                .setName("ga:browser");

        // Create the ReportRequest object.
        ReportRequest request = new ReportRequest()
                .setViewId("172698561")
                .setDateRanges(dateRangeList)
                .setDimensions(Arrays.asList(browser))
                .setMetrics(Arrays.asList(sessions));

        ArrayList<ReportRequest> requests = new ArrayList<ReportRequest>();
        requests.add(request);

        // Create the GetReportsRequest object.
        GetReportsRequest getReport = new GetReportsRequest()
                .setReportRequests(requests);

        // Call the batchGet method.
        GetReportsResponse response = service.reports().batchGet(getReport).execute();

        // Return the response.
        return response;
    }

//    private String getAccessToken(String gaAccountId, String gaRefreshToken, String clientId, String clientSecret, String grant_type) throws IOException, ParseException {
//        String accessTokenUrl = "https://www.googleapis.com/oauth2/v4/token?client_id=" + clientId + "&client_secret=" + clientSecret + "&refresh_token="
//                + gaRefreshToken + "&grant_type=" + grant_type;
//        String tokenData = Rest.postRawForm(accessTokenUrl, "{}");
//        JSONParser parser = new JSONParser();
//        Object jsonObj = parser.parse(tokenData);
//        JSONObject array = (JSONObject) jsonObj;
//        String accessToken = (String) array.get("access_token");
//        return accessToken;
//    }
//    public static String getGaGoals(String accountId, String viewId) {
//        String returnStr = "";
//        try {
//            Analytics analytics = initializeAnalytics();
//            try {
//                Goals goals = analytics.management().goals().list(accountId, "~all", "~all").execute();
//                for (Goal goal : goals.getItems()) {
//                    if (goal.getProfileId().equalsIgnoreCase(viewId) && goal.getActive()) {
//                        if (goal.getName().equalsIgnoreCase("Directions Page View") || goal.getName().equalsIgnoreCase("Directions Page Views")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,directionsPageView;";
//                        } else if (goal.getName().equalsIgnoreCase("Inventory Page View") || goal.getName().equalsIgnoreCase("Inventory Page Views")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,inventoryPageViews;";
//                        } else if (goal.getName().equalsIgnoreCase("Lead Submission") || goal.getName().equalsIgnoreCase("Lead Submission - General")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,leadSubmission;";
//                        } else if (goal.getName().equalsIgnoreCase("Specials Page Views") || goal.getName().equalsIgnoreCase("Specials Page View")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,specialsPageView;";
//                        } else if (goal.getName().equalsIgnoreCase("Time on Site > 2 Min") || goal.getName().equalsIgnoreCase("Time on Site > 2 Minutes")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,timeOnSiteGt2Mins;";
//                        } else if (goal.getName().equalsIgnoreCase("VDP Views")) {
//                            returnStr += "ga:goal" + goal.getId() + "Completions,vdpViews;";
//                        }
//                    }
//                }
//
//            } catch (GoogleJsonResponseException e) {
//                System.err.println("There was a service error: "
//                        + e.getDetails().getCode() + " : "
//                        + e.getDetails().getMessage());
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(GaService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return returnStr;
//    }
//
//    public static void main(String[] args) {
//        try {
//            getGaGoals("43651400", "79919517");
//            AnalyticsReporting service = initializeAnalyticsReporting();
//            GetReportsResponse response = getReport(service);
//            printResponse(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    private String getAccessToken(Integer dataSourceId) throws IOException, ParseException {
//        List<TokenDetails> TokenDetails = resourceManager.getOauthToken(dataSourceId);
//        TokenDetails token = TokenDetails.get(0);
//        return getAccessToken(null, token.getRefreshToken(), token.getClientId(), token.getClientSecret(), "refresh_token");
//    }
}
