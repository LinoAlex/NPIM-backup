/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.OAuth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author dashience
 */
@Service("oAuth2ServiceConfig")
class OAuth2ServiceConfig {

    @Value("${app.config.oauth.facebook.authorizeUrl}")
    public String facebookAuthorizeUrl;
    @Value("${app.config.oauth.facebook.accessTokenUrl}")
    public String facebookAccessTokenUrl;
    @Value("${app.config.oauth.linkedIn.authorizeUrl}")
    public String linkedInAuthorieUrl;
    @Value("${app.config.oauth.linkedIn.accessTokenUrl}")
    public String linkedInAccessTokenUrl;
    @Value("${app.config.oauth.ga.authorizeUrl}")
    public String gaAuthorieUrl;
    @Value("${app.config.oauth.ga.accessTokenUrl}")
    public String gaAccessTokenUrl;
    @Value("${app.config.oauth.ga.scope}")
    public String gaScope;
   
//public String facebookAuthorizeUrl= "https://www.facebook.com/v2.12/dialog/oauth";
//
//public String linkedInAuthorieUrl= "https://www.linkedin.com/oauth/v2/authorization" ; 
//public String linkedInAccessTokenUrl= "https://www.linkedin.com/oauth/v2/accessToken" ;



    public String CALLBACK_URL = "http://localhost:8084/NPIM/admin/social/callback";

}
