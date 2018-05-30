/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.connect.connection;

import com.netphenix.npim.connect.connection.Api.GANPIMConnect;
import com.netphenix.npim.connect.connection.Api.LinkedInConnect;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * abstract class which will be extended to create a connection of particular
 * type from the connectUrl params.
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public abstract class NPIMConnection {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NPIMConnection.class);
    public NPIMConnection() {
    }

    public static NPIMConnection getInstance(String instanceType) {
        if (instanceType.startsWith("npim://ga")) {
            LOGGER.debug("the ga instance is returned");
            return new GANPIMConnect(instanceType);
        }
        if (instanceType.startsWith("npim://linkedIn")) {
            LOGGER.debug("the linkedIn instance is returned");
            return new LinkedInConnect(instanceType);
        }
        return null;
    }

    public abstract Map getData(String dataSetUrl);
}
