/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.rest.filter;

import br.gov.ce.fortaleza.sesec.rest.security.HttpHeadersNames;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import java.util.logging.Logger;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Jorge
 */
@Provider
public class RESTResponseFilter implements ContainerResponseFilter{
    
    private final static Logger log = Logger.getLogger(RESTResponseFilter.class.getName());

    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        log.info("Filtering REST Response");
        response.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHttpHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        response.getHttpHeaders().add("Access-Control-Allow-Headers", HttpHeadersNames.SERVICE_KEY+","+HttpHeadersNames.AUTH_TOKEN);
        return response;
    }
    
}
