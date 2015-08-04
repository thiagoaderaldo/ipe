/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.gov.ce.fortaleza.sesec.entities.Usuarios;
import br.gov.ce.fortaleza.sesec.jpa.controller.UsuariosJpaController;
import br.gov.ce.fortaleza.sesec.rest.json.messages.BuildMessageJson;
import br.gov.ce.fortaleza.sesec.rest.json.messages.JsonMessagesNames;
import br.gov.ce.fortaleza.sesec.rest.security.Authenticator;
import br.gov.ce.fortaleza.sesec.rest.security.HttpHeadersNames;
import br.gov.ce.fortaleza.sesec.util.JpaControllerUtil;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.Persistence;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jorge
 */
@Path("/resource")
public class RestRecourceAuthenticate {
    
    private final static Logger log = Logger.getLogger(RestRecourceAuthenticate.class.getName());
    //private Authenticator authenticator;
    private UsuariosJpaController jpaController = null;
    
    private UsuariosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new UsuariosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }
    
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(@Context HttpHeaders httHeaders,
            @FormParam("username") String username,
            @FormParam("password") String password) {
        
        Authenticator authenticator = Authenticator.getInstance();
        
        String serviceKey = httHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY).get(0);
        
        try {
            Usuarios usuario = getJpaController().getUsuarioChaves(username, serviceKey);
            log.info("Usuario encontrado: " + usuario.getLogin());
            
            String authToken = authenticator.login(serviceKey, usuario, password);
            
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("auth_token", authToken);
            JsonObject jsonObj = jsonObjBuilder.build();
            
            return getNoCacheResponseBuilder(Response.Status.CREATED).entity(jsonObj.toString()).build();
        } catch (final GeneralSecurityException ex) {
            log.info("Erro password. " + ex);
            JsonObject jsonObj = BuildMessageJson.getMessageJson(JsonMessagesNames.MESSAGE_LOGIN_ERRO).build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        } catch (Exception e) {
            log.info("Não foi possivel recuperar a entidade Usuarios . " + e);
            JsonObject jsonObj = BuildMessageJson.getMessageJson(JsonMessagesNames.MESSAGE_LOGIN_ERRO).build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }
        
    }
    
    @GET
    @Path("/demo-get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response demoGetMethod(@Context HttpHeaders httpHeaders) {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        
        log.info("Accept: " + headers.getFirst("Accept"));
        
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("mensagem", "hedears");
        if (headers.containsKey("Accept")) {
            jsonObjBuilder.add("Accept", headers.getFirst("Accept"));
        }
        if (headers.containsKey("Content-Type")) {
            jsonObjBuilder.add("Content-Type", headers.getFirst("Content-Type"));
        }
        if (headers.containsKey(HttpHeadersNames.AUTH_TOKEN)) {
            jsonObjBuilder.add(HttpHeadersNames.AUTH_TOKEN, headers.getFirst("auth_token"));
        }
        if (headers.containsKey(HttpHeadersNames.SERVICE_KEY)) {
            jsonObjBuilder.add(HttpHeadersNames.SERVICE_KEY, headers.getFirst("service_key"));
        }
        JsonObject jsonObjt = jsonObjBuilder.build();
        
        return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObjt.toString()).build();
    }
    
    @POST
    @Path("/demo-post")
    @Produces(MediaType.APPLICATION_JSON)
    public Response demoPostMethod() {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("mensagem", "metodo postMethod executado");
        JsonObject jsonObj = jsonObjBuilder.build();
        
        return getNoCacheResponseBuilder(Response.Status.ACCEPTED).entity(jsonObj.toString()).build();
    }
    
    @GET
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpHeaders httpHeaders) {
        try {
            Authenticator authenticator = Authenticator.getInstance();
            
            String serviceKey = httpHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY).get(0);
            
            String authToken = httpHeaders.getRequestHeader(HttpHeadersNames.AUTH_TOKEN).get(0);
            
            authenticator.logout(serviceKey, authToken);
            
            JsonObject jsonObj = BuildMessageJson.getMessageJson(JsonMessagesNames.MESSAGE_LOGOUT).build();
            return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObj.toString()).build();
        } catch (final GeneralSecurityException ex) {
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).build();
        }
    }
    
    @GET
    @Path("/headers")
    public void checkHeaders(@Context HttpHeaders httpHeaders) {
        String serviceKey = httpHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY).get(0);
        log.info("Chave: " + serviceKey);
        
        String authToken = httpHeaders.getRequestHeader(HttpHeadersNames.AUTH_TOKEN).get(0);
        log.info("Token: " + authToken);
    }
    
    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);
        return Response.status(status).cacheControl(cc);
    }
}
