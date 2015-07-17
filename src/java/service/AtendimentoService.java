/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.entities.rest.AtendimentosRest;
import br.gov.ce.fortaleza.sesec.entities.rest.TesteAtendimentoGson;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import com.google.gson.Gson;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Jorge
 */
@Path("/atendimentos")
@Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML,
    MediaType.APPLICATION_JSON})
@Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML,
    MediaType.APPLICATION_JSON})
public class AtendimentoService {

    private AtendimentosJpaController jpaController = null;

    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    @GET
    @Path("/gson")  
    @Produces("application/json")
    public String listGson(){
        List<Atendimentos> atendimentos = getJpaController().findAtendimentosEntities();
        List<TesteAtendimentoGson> atendimentosRest = new ArrayList<TesteAtendimentoGson>();
        for (Atendimentos atendimento : atendimentos) {
            TesteAtendimentoGson atendimentoGosn = new TesteAtendimentoGson();
            atendimentoGosn.setProtocolo(atendimento.getProtocolo());
            atendimentoGosn.setTitulo(atendimento.getTitulo());
            atendimentosRest.add(atendimentoGosn);
        }
        return new Gson().toJson(atendimentosRest);
    }
    
    @GET
    @Path("/list")
    public List<Atendimentos> findAll() {
        List<Atendimentos> atendimentos = getJpaController().findAtendimentosEntities();
        return atendimentos;
    }

    @GET
    @Path("/object")
    public AtendimentosRest listAll() {
        List<Atendimentos> atendimentos = getJpaController().findAtendimentosEntities();
        return new AtendimentosRest(atendimentos);
    }
    
    @GET
    @Path("/response")
    public Response findResponse(){
        List<Atendimentos> atendimentos = getJpaController().findAtendimentosEntities();
        AtendimentosRest atendimentos_object = new AtendimentosRest(atendimentos);
        return Response.ok().entity(atendimentos_object).build();
    }
    
    @POST
    @Path("/add")
    public Response add(Atendimentos atendimento){
        try {
            getJpaController().create(atendimento);
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        URI uri = UriBuilder.fromPath("atendimentos/{protocolo}").build(atendimento.getProtocolo());
        return Response.created(uri).entity(atendimento).build();
    }
    
    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findId(){
        Long id = new Long(333);
        Atendimentos ate = getJpaController().findAtendimentos(id);
        return Response.ok().entity(ate).build();
    }
    
}
