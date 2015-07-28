/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jorge
 */
@XmlRootElement
public class Teste {

    public Teste() {
    }
    
    public Teste(String nome, String titulo) {
        this.nome = nome;
        this.titulo = titulo;
    }
    
    private String nome;
    private String titulo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "Nome: "+this.nome+" titulo: "+this.titulo;
    }
}
