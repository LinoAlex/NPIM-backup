/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.authorization;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuth1Template;
import org.springframework.social.oauth1.OAuth1Version;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author dashience
 */
public class OAuth1Adapter {


    public OAuth1Template oAuth1Template;
    public OAuth1Parameters oAuth1Parameters;
    public OAuthToken requestToken;

    public Map<String, Object> generateOAuth1Url(String apiKey, String apiSecret, Map<String, List<String>> parameters,Map<String, String> oauthUrls) throws Exception {
        oAuth1Parameters = new OAuth1Parameters(parameters);
        Map<String, Object> returnMap = new HashMap<>();
        return OAuth1UrlGenerator(apiKey, apiSecret,oauthUrls.get("requestTokenUrl"), oauthUrls.get("authorizeUrl"), oauthUrls.get("authenticateUrl"), oauthUrls.get("accessTokenUrl"), returnMap);
        
    }

    public Map<String, Object> OAuth1UrlGenerator(String apiKey, String apiSecret, String requestTokenUrl, String authorizeUrl,String authenticateUrl, String accessTokenUrl, Map<String, Object> returnMap) throws Exception {
        oAuth1Template = new OAuth1Template(apiKey, apiSecret, requestTokenUrl, authorizeUrl, authenticateUrl, accessTokenUrl, OAuth1Version.CORE_10_REVISION_A);
        requestToken = oAuth1Template.fetchRequestToken(oAuth1Parameters.getCallbackUrl(), null);
        String oAuthUrl = oAuth1Template.buildAuthenticateUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        returnMap.put("authorizeUrl", oAuthUrl);
        return returnMap;
    }
    public TokenTemplate exchangeForAccessToken(MultiValueMap<String, Object> returnMap) {
        AuthorizedRequestToken setToken = new AuthorizedRequestToken(requestToken, (String) returnMap.getFirst("oauth_verifier"));
        OAuthToken tokenDetails = oAuth1Template.exchangeForAccessToken(setToken, OAuth1Parameters.NONE);
        return getTokenTemplate(tokenDetails);
    }

    public TokenTemplate getTokenTemplate(OAuthToken oAuthToken) {
        TokenTemplate tokenTemplate = new TokenTemplate();
        tokenTemplate.setTokenValue(oAuthToken.getValue());
        tokenTemplate.setTokenSecret(oAuthToken.getSecret());
        return tokenTemplate;
    }
}
