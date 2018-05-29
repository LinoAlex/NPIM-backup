/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.utils.service;

import com.netphenix.npim.authorization.TokenTemplate;
import com.netphenix.npim.exceptions.MissingAuthorizationException;
import com.netphenix.npim.exceptions.MissingDataSetDetailsException;
import com.netphenix.npim.exceptions.MissingDataSourceDetailsException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 * is a helper service class supporting the operations for dataSource
 * connection.
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
@Service("dataSourceUtils")
public class DataSourceUtils {

    /**
     * will generate the SourceUrl based on the user given parameters in map.
     *
     * @param queryParams is the map containing the user parameters for making
     * connection.
     * @return a string with sourceUrl underStood by NPMConnection.getInstance
     * method, for getting a connection with the particular type .
     */
    public String generateSourceUrl(Map<String, Object> queryParams) {
        if (isExist(queryParams, "accessToken")) {
            return "npim://" + queryParams.get("apiSource") + "?clientId=" + queryParams.get("apiKey") + "&clientSecret=" + queryParams.get("apiSecret") + "&accessToken=" + queryParams.get("accessToken");
        } else if (isExist(queryParams, "refreshToken")) {
            return "npim://" + queryParams.get("apiSource") + "?clientId=" + queryParams.get("apiKey") + "&clientSecret=" + queryParams.get("apiSecret") + "&refreshToken=" + queryParams.get("refreshToken");
        } else if (!isExist(queryParams, "apiKey") && !isExist(queryParams, "apiSecret")) {
            throw new MissingDataSourceDetailsException("apiKey or apiSecret for api connection not found");
        } else {
            return "authenticate";
        }
    }

    /**
     * checks if a key value pair is present in the map.
     *
     * @param queryParams is the map to check for existance of key
     * @param key is the key to check if exists
     * @return true if key exists else return false
     */
    private boolean isExist(Map<String, Object> queryParams, String key) {
        return queryParams.containsKey(key) && isNotNull((String) queryParams.get(key));
    }

    /**
     * check if the string contains value also the value is not null.
     *
     * @param str is the string to be checked.
     * @return true if string is not null or empty
     */
    private boolean isNotNull(String str) {
        return !(str == null || str.isEmpty() || str.equals("undefined"));
    }

    private void listMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key---------->" + key);
            System.out.println("value---------->" + value);
        }
    }

    public Map<String, Object> mapValues(TokenTemplate tokenTemplate, MultiValueMap<String, Object> oAuthData) {
        oAuthData.getFirst("apiKey");
        oAuthData.getFirst("apiSecret");
        oAuthData.getFirst("apiSource");
        Map<String, Object> returnMap = oAuthData.toSingleValueMap();
        returnMap.putAll(tokenTemplate.toMap());
        return returnMap;
    }

    public String generateDataSeturl(Map<String, Object> queryString) {
        if (isExist(queryString, "dataSetName")) {
            return queryString.get("dataSetName")+"?"+"metrices="+queryString.get("metrices")+"&dimensions="+queryString.get("dimensions");
        } else {
            throw new MissingDataSetDetailsException("DataSet name is Empty or not exist");
        }
    }
}
