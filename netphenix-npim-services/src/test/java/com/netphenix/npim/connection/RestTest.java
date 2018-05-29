/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.connection;

import com.netphenix.npim.rest.service.Rest;

/**
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class RestTest {
    public static void main(String[] args){
        String url = "https://api.linkedin.com/v1/companies/10671978/company-statistics?oauth2_access_token=AQX3vmCwPkhi1I9JapMFSnBo-QDhjxXv28a6aRfgrfdqpy1QhxU7H78DUxWG5PQTLYQK5T95pxQZ32FX6v_UpFX1yQ5uODKDxJ27QM5Fn1uAJ-_X8-wAFYiSD7q79i8ks3lV0xgOjzN_OmEWmi1NCf-4_CMpPC0mdtLkmMSrShHVRqMN5CSSxpXVeqVbyMeBnM1hs55gNV1xxsaT_xOa489zNMDS9xP4erOjxnq2y76Q1tHceAgA2dW48eZeTt2NJrdv8XN6Dh9uwYfsCRq9ziXaI80zlbK5WyC8tL-PWTFmOrmcpN9QzAv-8go2p0aeNLxrnPWtUAwrR0irpWjx9Ka9XUX9lg&format=json";
        Rest.getData(url);
    }
}
