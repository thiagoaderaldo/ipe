/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Jorge
 */
@Path("/ola")
public class Hello {
    @GET
    @Produces("text/plain")
    public String ola(){
        return "Ola";
    }
}
