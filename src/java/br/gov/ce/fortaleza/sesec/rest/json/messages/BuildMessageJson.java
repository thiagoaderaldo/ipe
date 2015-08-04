/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.rest.json.messages;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Jorge
 */
public class BuildMessageJson {
    
    public static JsonObjectBuilder getMessageJson(String message){
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("message", message);
        return jsonObjBuilder;
    }
    
}
