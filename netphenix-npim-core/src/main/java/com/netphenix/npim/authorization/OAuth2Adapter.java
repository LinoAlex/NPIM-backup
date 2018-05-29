package com.netphenix.npim.authorization;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
       
public class OAuth2Adapter {

    private OAuth2Template oAuth2Template;
    private OAuth2Parameters oAuth2Parameters;

    public Map<String, Object> getOAuth2Token(String apiKey, String apiSecret, Map<String, List<String>> parameters, Map<String, String> oauthUrls) throws Exception {
        oAuth2Parameters = new OAuth2Parameters(parameters);
        Map<String, Object> returnMap = new HashMap<>();
        return OAuth2UrlGenerator(apiKey, apiSecret, oauthUrls.get("authorizeUrl"), oauthUrls.get("accessTokenUrl"), returnMap);
    }

    public Map<String, Object> OAuth2UrlGenerator(String apiKey, String apiSecret, String authorizeUrl, String accessTokenUrl, Map<String, Object> returnMap) throws Exception {
        oAuth2Template = new OAuth2Template(apiKey, apiSecret, authorizeUrl, accessTokenUrl);
        String oauthUrl = oAuth2Template.buildAuthenticateUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
        returnMap.put("authorizeUrl", oauthUrl);
        return returnMap;
    }

    public TokenTemplate exchangeForAccessToken(MultiValueMap<String, Object> dataMap) throws Exception {
        String code = (String) dataMap.getFirst("code");
//        boolean useParameters = Boolean.parseBoolean((String) dataMap.getFirst("useParameters"));
        System.out.println("oauth2Template------>"+oAuth2Template);
        System.out.println("code----->"+code);
        oAuth2Template.setUseParametersForClientAuthentication(true);
        AccessGrant tokenDetails = oAuth2Template.exchangeForAccess(code, oAuth2Parameters.getRedirectUri(), null);
        return getTokenTemplate(tokenDetails);
    }

    public void insertExtraDetails(MultiValueMap<String, Object> dataMap) throws Exception {
        HashMap<String, String> extraCredentials = (HashMap<String, String>) dataMap.getFirst("ExtraCredentials");
        if (extraCredentials != null) {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            DB database = mongoClient.getDB("dashience");
            DBCollection collection = database.getCollection("userDetails");
            DBObject json = new BasicDBObject();
            json.putAll(extraCredentials);
            collection.insert(json);
            mongoClient.close();
        }
    }

    public TokenTemplate getTokenTemplate(AccessGrant accessGrant) {
        TokenTemplate tokenTemplate = new TokenTemplate();
        tokenTemplate.setExpiryDate(accessGrant.getExpireTime());
        tokenTemplate.setRefreshToken(accessGrant.getRefreshToken());
        tokenTemplate.setScope(accessGrant.getScope());
        tokenTemplate.setTokenValue(accessGrant.getAccessToken());
        return tokenTemplate;
    }
}
