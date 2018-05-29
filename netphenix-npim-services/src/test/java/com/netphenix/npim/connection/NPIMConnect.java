/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.connection;

import com.netphenix.npim.connect.connection.NPIMConnection;
import java.util.Map;

/**
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class NPIMConnect {
    
    public static void main(String[] args){
        NPIMConnection connection = NPIMConnection.getInstance("npim://ga?clientId=\"\"&clientSecret=\"\"");
        Map data = connection.getData("account performance?metrices=[browser]&dimension=[week]");
    }
}
