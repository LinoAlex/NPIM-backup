/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.OAuth.service;

import com.netphenix.npim.authorization.TokenTemplate;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Lino
 */
public interface OAuthSelector  {
   /**
    * 
    * @param request Http request with apiKey,apiSecret,apiSource,accountId values as RequestHeaders are expected 
    * @return Will return the AuthorizeUrl for the requested api Data Source 
    * as a MultiValueMap ,get the url by accessing sring Key authorizeUrl in MultiValueMap.
    * @throws Exception On missing of required library , wrong request parameters ,Mismatch of callback url.
    */ 
   public MultiValueMap<String, Object> generateOAuthUrl(HttpServletRequest request) throws Exception;
   /**
    * 
    * @param dataMap is the MultiValueMap returned from generateOAauthUrl along with the parameters in url of response for the authorizeurl 
    * @return The tokenDetails Entity object containing all token related data
    * @throws Exception On mismatch of callback url
    */
   public TokenTemplate getTokenDetails(MultiValueMap<String, Object> dataMap) throws Exception;
}
