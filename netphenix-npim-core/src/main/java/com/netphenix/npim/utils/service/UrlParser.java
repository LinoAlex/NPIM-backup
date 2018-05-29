/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.utils.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * will parse the urls to extract the required results.
 * @author Lino Alex <linoalex1996@gmail.com>
 */
@Service("UrlParser")
public class UrlParser {

    /**
     * will get the query in an url.
     * @param url
     * @return
     * @throws java.lang.Exception
     */
    public Map<String, String> getQueryPairs(String url) throws Exception{
        Map<String, String> parsedQueryPairs = new HashMap<>();
        String query = getQuery(url);
        if (!nullOrEmpty(query)) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && !parts[0].isEmpty()) {
                    parsedQueryPairs.put(parts[0], parts[1]);
                }
            }
        }

        return parsedQueryPairs;
    }

    /**
     * gets map values of input array as a list of strings. eg -
     * metrices=[val1,val2,val3] ,val1,2,3 are stored as list for map key
     * metrices.
     *
     * @param url
     * @return
     */
    public Map<String, List<String>> getQueryPairsAsList(String url) throws Exception{
        Map<String, List<String>> parsedQueryPairs = new HashMap<>();
        String query = getQuery(url);
        if (!nullOrEmpty(query)) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && !parts[0].isEmpty()) {
                    parsedQueryPairs.put(parts[0], Arrays.asList(parts[1]));
                }
            }
        }
        return parsedQueryPairs;
    }

    public Map<String,Object> queryToMap(String query)throws Exception{
         Map<String,Object> parsedQueryPairs = new HashMap<>();
        if (!nullOrEmpty(query)) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                if (parts.length == 2 && !parts[0].isEmpty()) {
                    parsedQueryPairs.put(parts[0], parts[1]);
                }
            }
        }

        return parsedQueryPairs;
    }
    public String getDataSetPath(String url, String path) throws Exception{
        String metaData = null;
        System.out.println("metadata--------------->"+url);
        if (!nullOrEmpty(url)) {
//            metaData = url.split("?");
metaData = url.split("\\?")[0];
        }
        if (!nullOrEmpty(metaData)) {
            String[] paths = metaData.split("/");
           return paths[0];
        }
        return null;
    }

    private boolean nullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private String getQuery(String url) throws Exception{
        return url.substring(url.indexOf("?") + 1, url.length());
    }
}
