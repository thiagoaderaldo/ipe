/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thiago
 */
@Entity
@Table(name = "vw_dshbrd_resumo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwDshbrdResumo.findAll", query = "SELECT v FROM VwDshbrdResumo v"),
    @NamedQuery(name = "VwDshbrdResumo.findByQtdOcrAbertas", query = "SELECT v FROM VwDshbrdResumo v WHERE v.qtdOcrAbertas = :qtdOcrAbertas"),
    @NamedQuery(name = "VwDshbrdResumo.findByQtdAtdSemEquipe", query = "SELECT v FROM VwDshbrdResumo v WHERE v.qtdAtdSemEquipe = :qtdAtdSemEquipe"),
    @NamedQuery(name = "VwDshbrdResumo.findByNomedaRegional", query = "SELECT v FROM VwDshbrdResumo v WHERE v.nomedaRegional = :nomedaRegional"),
    @NamedQuery(name = "VwDshbrdResumo.findByQtdOcrrNaRegional", query = "SELECT v FROM VwDshbrdResumo v WHERE v.qtdOcrrNaRegional = :qtdOcrrNaRegional"),
    @NamedQuery(name = "VwDshbrdResumo.findByTipoDeTipologia", query = "SELECT v FROM VwDshbrdResumo v WHERE v.tipoDeTipologia = :tipoDeTipologia"),
    @NamedQuery(name = "VwDshbrdResumo.findByQtdOcrrTipologia", query = "SELECT v FROM VwDshbrdResumo v WHERE v.qtdOcrrTipologia = :qtdOcrrTipologia"),
    @NamedQuery(name = "VwDshbrdResumo.findByNomeDoBairro", query = "SELECT v FROM VwDshbrdResumo v WHERE v.nomeDoBairro = :nomeDoBairro"),
    @NamedQuery(name = "VwDshbrdResumo.findByQtdDeOcrrBairro", query = "SELECT v FROM VwDshbrdResumo v WHERE v.qtdDeOcrrBairro = :qtdDeOcrrBairro")})
public class VwDshbrdResumo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "qtd_ocr_abertas")
    @Id
    private BigInteger qtdOcrAbertas;
    @Column(name = "qtd_atd_sem_equipe")
    private BigInteger qtdAtdSemEquipe;
    @Column(name = "Nome_da_Regional")
    private String nomedaRegional;
    @Column(name = "qtd_ocrr_na_regional")
    private BigInteger qtdOcrrNaRegional;
    @Column(name = "tipo_de_tipologia")
    private String tipoDeTipologia;
    @Column(name = "qtd_ocrr_tipologia")
    private BigInteger qtdOcrrTipologia;
    @Column(name = "nome_do_bairro")
    private String nomeDoBairro;
    @Column(name = "qtd_de_ocrr_bairro")
    private BigInteger qtdDeOcrrBairro;

    public VwDshbrdResumo() {
    }

    public BigInteger getQtdOcrAbertas() {
        return qtdOcrAbertas;
    }

    public void setQtdOcrAbertas(BigInteger qtdOcrAbertas) {
        this.qtdOcrAbertas = qtdOcrAbertas;
    }

    public BigInteger getQtdAtdSemEquipe() {
        return qtdAtdSemEquipe;
    }

    public void setQtdAtdSemEquipe(BigInteger qtdAtdSemEquipe) {
        this.qtdAtdSemEquipe = qtdAtdSemEquipe;
    }

    public String getNomedaRegional() {
        return nomedaRegional;
    }

    public void setNomedaRegional(String nomedaRegional) {
        this.nomedaRegional = nomedaRegional;
    }

    public BigInteger getQtdOcrrNaRegional() {
        return qtdOcrrNaRegional;
    }

    public void setQtdOcrrNaRegional(BigInteger qtdOcrrNaRegional) {
        this.qtdOcrrNaRegional = qtdOcrrNaRegional;
    }

    public String getTipoDeTipologia() {
        return tipoDeTipologia;
    }

    public void setTipoDeTipologia(String tipoDeTipologia) {
        this.tipoDeTipologia = tipoDeTipologia;
    }

    public BigInteger getQtdOcrrTipologia() {
        return qtdOcrrTipologia;
    }

    public void setQtdOcrrTipologia(BigInteger qtdOcrrTipologia) {
        this.qtdOcrrTipologia = qtdOcrrTipologia;
    }

    public String getNomeDoBairro() {
        return nomeDoBairro;
    }

    public void setNomeDoBairro(String nomeDoBairro) {
        this.nomeDoBairro = nomeDoBairro;
    }

    public BigInteger getQtdDeOcrrBairro() {
        return qtdDeOcrrBairro;
    }

    public void setQtdDeOcrrBairro(BigInteger qtdDeOcrrBairro) {
        this.qtdDeOcrrBairro = qtdDeOcrrBairro;
    }
    
}
