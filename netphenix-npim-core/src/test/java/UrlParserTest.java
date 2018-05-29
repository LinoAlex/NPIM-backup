/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.netphenix.npim.utils.service.UrlParser;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class UrlParserTest {
    
    public static void main(String[] args){
//        urlQueryParseTest("\"npim://ga?clientId=\"lino123\"&clientSecret=sddfs&accessToken=\"3453465\"");
            urlToMapTest("apiKey=96704798310-73qm33higfsfhheirhhnhbof4vpro2kg.apps.googleusercontent.com&apiSecret=ibJYKMcTtL4vOurV2y-wiqP9&apiSource=ga&domainName=localhost:8084");
    }
    
    public static void urlQueryParseTest(String url){
        UrlParser parser = new UrlParser();
        Map<String,String> query = null;
        try {
            query = parser.getQueryPairs(url);
        } catch (Exception ex) {
            Logger.getLogger(UrlParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        printMap(query);
    }

    private static void printMap(Map<String, String> query) {
        for (Map.Entry<String, String> entry : query.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key:"+key+"  value:"+value); 
        }
    }
    private static void printObjectMap(Map<String, Object> query) {
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            String key = entry.getKey();
            String value = (String)entry.getValue();
            System.out.println("key:"+key+"  value:"+value); 
        }
    }
    public static void urlToMapTest(String url){
        UrlParser parser = new UrlParser();
               Map<String,Object> query = null;
        try {
            query = parser.queryToMap(url);
        } catch (Exception ex) {
            Logger.getLogger(UrlParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        printObjectMap(query);
    }
}
