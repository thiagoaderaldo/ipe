/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities.rest;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jorge
 * Essa classe sera utilizada para encapsular outros elementos
 * E podera ser usada futuramente para adicionar HATEOAS
 */
@XmlRootElement
public class AtendimentosRest {
    
    private List<Atendimentos> atendimentos = new ArrayList<Atendimentos>();

    public AtendimentosRest() {
    }
    
    public AtendimentosRest(List<Atendimentos> atendimentos) {
        this.atendimentos = atendimentos;
    }
    
    
    @XmlElement(name="atendimento")
    public List<Atendimentos> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<Atendimentos> atendimentos) {
        this.atendimentos = atendimentos;
    }
    
}
