/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.connect.connection.Api;

import com.netphenix.npim.apiconnector.service.GaService;
import com.netphenix.npim.bean.ColumnDef;
import com.netphenix.npim.connect.connection.NPIMConnection;
import com.netphenix.npim.rest.service.ApiUtils;
import com.netphenix.npim.utils.service.UrlParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * will the google analytics connection implementation.
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class GANPIMConnect extends NPIMConnection {

//    private static final Logger LOGGER = LoggerFactory.getLogger(GANPIMConnect.class);
    private final UrlParser urlParser = new UrlParser();
    private final GaService gaService = new GaService();
    private Map<String, String> queryMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(GANPIMConnect.class);

    public GANPIMConnect(String connectUrl) {
        setApiConnectionData(connectUrl);
    }

    @Override
    public Map getData(String dataSetUrl) {
        try {
            return getDataSetData(dataSetUrl);
        } catch (Exception ex) {
            LOGGER.error(" Google analytics data fetching error ", ex);
        }
        return null;
    }

    private Map<String, String> getAccessToken(Map<String, String> queryMap) {
//        return apiUtils.Authenticate(queryMap);
        return null;
    }

    private Map getDataSetData(String dataSetUrl) throws Exception {
        Map<String, String> dataSetParams = urlParser.getQueryPairs(dataSetUrl);
        String dataSetName = urlParser.getDataSetPath(dataSetUrl, "dataSetName");
        String dimension = dataSetParams.get("dimension");
        String metrices = dataSetParams.get("metrices");
//        String profileId = queryMap.get("profileId");
        String profileId = "154537398";
        String accessToken = queryMap.get("accessToken");
        LOGGER.debug("access token------->" + accessToken);
        LOGGER.debug("dimension------->" + dimension);
//        Map<String, List<String>> dataSetParams = urlParser.getQueryPairsAsList(dataSetUrl);
        Map dataMap = gaService.getGaReport(dataSetName, profileId, null, null, dimension, metrices, 0, 1, accessToken);
        List<Map<String, Object>> data = (List<Map<String, Object>>) dataMap.get("data");
        List<ColumnDef> columnDefs = ApiUtils.getColumnDefObject(data);
        Map returnMap = new HashMap<>();
        returnMap.put("columnDefs", columnDefs);
        returnMap.put("data", data);
        return returnMap;
    }

    private void setApiConnectionData(String connectUrl) {
        try {
            queryMap = urlParser.getQueryPairs(connectUrl);
        } catch (Exception ex) {
            LOGGER.error("google analytics url parsing error", ex);
        }
    }

}
