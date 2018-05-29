package com.netphenix.npim.OAuth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("oAuth1ServiceConfig")
class OAuth1ServiceConfig {

    @Value("${app.config.oauth.twitter.requestTokenUrl}")
    public String twitterRequestTokenUrl;
    @Value("${app.config.oauth.twitter.authorizeUrl}")
    public String twitterAuthorizeUrl;
    @Value("${app.config.oauth.twitter.authenticateUrl}")
    public String twitterAuthenticateUrl;
    @Value("${app.config.oauth.twitter.accessTokenUrl}")
    public String twitterAccessTokenUrl;
//    public String twitterRequestTokenUrl = "https://api.twitter.com/oauth/request_token";
//    public String twitterAuthorizeUrl = "https://api.twitter.com/oauth/authorize";
//    public String twitterAuthenticateUrl = "https://api.twitter.com/oauth/authorize";
//    public String twitterAccessTokenUrl = "https://api.twitter.com/oauth/access_token";
    public String CALLBACK_URL = "http://tellyourstory.lino.com:8080/dashboard/admin/social/callback";
}
