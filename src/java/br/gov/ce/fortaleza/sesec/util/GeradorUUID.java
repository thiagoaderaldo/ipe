/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.util;

import java.util.UUID;

/**
 *
 * @author Jorge
 */
public class GeradorUUID {
    
    public static String create(){
        String keyUUID = UUID.randomUUID().toString();
        return keyUUID;
    }
}
