/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.rest.filter;

import br.gov.ce.fortaleza.sesec.rest.security.Authenticator;
import br.gov.ce.fortaleza.sesec.rest.security.HttpHeadersNames;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import service.RestRecourceAuthenticate;

/**
 *
 * @author Jorge
 */
@Provider
public class RESTRequestFilter implements ContainerRequestFilter{

    private final static Logger log = Logger.getLogger(RESTRequestFilter.class.getName());

    @Override
    public ContainerRequest filter(ContainerRequest request)throws WebApplicationException{
        //GET, POST, PUT, DELETE, ...
        String method = request.getMethod();
         // myresource/get/56bCA for example
        String path = request.getPath(true);
        log.info( "Filtrando requisicao para: " + path );
        
        if(method.equals("OPTIONS")) {
            throw new WebApplicationException(Status.OK);
        }
        Authenticator authenticator = Authenticator.getInstance();
        String serviKey = request.getHeaderValue(HttpHeadersNames.SERVICE_KEY);
        if (!authenticator.isServiceKeyValid(serviKey)) {
            log.info("chave de serviço invalida");
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
        
        if (!path.startsWith("resource/login")) {
            String authToken = request.getHeaderValue(HttpHeadersNames.AUTH_TOKEN );
            log.info("verificando chave e token");
            if (!authenticator.isAuthTokenValid(serviKey, authToken)) {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
        }
        return request;
    }
}
