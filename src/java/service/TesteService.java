/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.gov.ce.fortaleza.sesec.entities.Teste;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jorge
 */
@Path("/teste")
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML,
    MediaType.APPLICATION_JSON})
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML,
    MediaType.APPLICATION_JSON})
public class TesteService {
    
    @GET
    public Response find(){
        Teste teste = new Teste("Nome default", "titulo deafaut");
        return Response.ok().entity(teste).build();
    }
    @POST
    public Response add(Teste t){
        Teste teste = t;
        return Response.created(null).entity(teste).build();
    }
}
