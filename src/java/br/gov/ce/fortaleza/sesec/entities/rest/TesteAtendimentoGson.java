/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jorge
 */
@XmlRootElement
public class TesteAtendimentoGson {
    private String protocolo;
    private String titulo;

    public String getProtocolo() {
        return protocolo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    
    
}
