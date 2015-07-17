/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.gov.ce.fortaleza.sesec.rest.security.Authenticator;
import br.gov.ce.fortaleza.sesec.rest.security.HttpHeadersNames;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Jorge
 */
@Path("/resource")
public class RestRecourceAuthenticate {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(@Context HttpHeaders httHeaders,
            @FormParam("username") String username,
            @FormParam("password") String password) {

        Authenticator autenticator = Authenticator.getInstance();

        List<String> key = httHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY);
        String serviceKey = key.get(0);

        try {
            String authToken = autenticator.login(serviceKey, username, password);

            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("auth_token", authToken);
            JsonObject jsonObj = jsonObjBuilder.build();

            return getNoCacheResponseBuilder(Response.Status.OK).entity(jsonObj.toString()).build();
        } catch (final GeneralSecurityException ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("messagem", "Problema service key, username and password");
            JsonObject jsonObj = jsonObjBuilder.build();

            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }

    }

    @GET
    @Path("/demo-get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response demoGetMethod() {
        JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
        jsonObjBuilder.add("mensagem", "metodo demoGetMethod executado");
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

    @POST
    @Path("/logout")
    public Response logout(@Context HttpHeaders httpHeaders) {
        try {
            Authenticator demoAuthenticator = Authenticator.getInstance();

            List<String> key = httpHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY);
            String serviceKey = key.get(0);

            List<String> auth = httpHeaders.getRequestHeader(HttpHeadersNames.SERVICE_KEY);
            String authToken = auth.get(0);

            demoAuthenticator.logout(serviceKey, authToken);

            return getNoCacheResponseBuilder(Response.Status.NO_CONTENT).build();
        } catch (final GeneralSecurityException ex) {
            return getNoCacheResponseBuilder(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);
        return Response.status(status).cacheControl(cc);
    }
}
