/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netphenix.npim.exceptions;

/**
 *
 * @author Lino Alex <linoalex1996@gmail.com>
 */
public class ConnectionExceptions extends NPIMExceptions{
    
    public ConnectionExceptions(String message) {
        super(message);
    }
    public ConnectionExceptions(String message,Throwable cause) {
        super(message,cause);
    }
    
}
