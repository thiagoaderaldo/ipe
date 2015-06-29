/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import java.util.List;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Jorge
 */
@Path("/atendimentos")
public class AtendimentoService {
    
    private AtendimentosJpaController jpaController = null;
    
    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    
    @GET
    @Produces({"application/xml","application/json"})
    public List<Atendimentos> findAll(){
        List<Atendimentos> atendimentos = getJpaController().findAtendimentosEntities();
        return atendimentos;
    }
}
