/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.authorization;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dashience
 */
public class TokenTemplate {

    private String accessToken;
    private String tokenSecret;
    private Long expiryDate;
    private String refreshToken;
    private String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenValue() {
        return accessToken;
    }

    public void setTokenValue(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }
    @Override
    public String toString(){
        return "{accessToken:\""+accessToken+"\",tokenSecret:\""+tokenSecret+"\",expiryDate:\""+expiryDate+"\",refreshToken:\""+refreshToken+"\",scope:\""+scope+"\"}";
    }
    public Map<String,Object> toMap(){
        Map<String,Object> returnMap = new HashMap<>();
        returnMap.put("accessToken", accessToken);
        returnMap.put("tokenSecret", tokenSecret);
        returnMap.put("expiryDate", expiryDate);
        returnMap.put("refreshToken", refreshToken);
        returnMap.put("scope", scope);
        return returnMap;
    }
}
