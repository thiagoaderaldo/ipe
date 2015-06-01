/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(name = "vw_dshbrd_indic")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwDshbrdIndic.findAll", query = "SELECT v FROM VwDshbrdIndic v"),
    @NamedQuery(name = "VwDshbrdIndic.findByQtdAtdSemEquipe", query = "SELECT v FROM VwDshbrdIndic v WHERE v.qtdAtdSemEquipe = :qtdAtdSemEquipe"),
    @NamedQuery(name = "VwDshbrdIndic.findByQtdDeOcrrAbertas", query = "SELECT v FROM VwDshbrdIndic v WHERE v.qtdDeOcrrAbertas = :qtdDeOcrrAbertas"),
    @NamedQuery(name = "VwDshbrdIndic.findByIdTipologia", query = "SELECT v FROM VwDshbrdIndic v WHERE v.idTipologia = :idTipologia"),
    @NamedQuery(name = "VwDshbrdIndic.findByNome", query = "SELECT v FROM VwDshbrdIndic v WHERE v.nome = :nome"),
    @NamedQuery(name = "VwDshbrdIndic.findByQtdTplMaiorOcrr", query = "SELECT v FROM VwDshbrdIndic v WHERE v.qtdTplMaiorOcrr = :qtdTplMaiorOcrr")})
public class VwDshbrdIndic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "qtd_atd_sem_equipe")
    private long qtdAtdSemEquipe;
    @Basic(optional = false)
    @Column(name = "qtd_de_ocrr_abertas")
    private long qtdDeOcrrAbertas;
    @Column(name = "id_tipologia")
    @Id
    private Integer idTipologia;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "qtd_tpl_maior_ocrr")
    private long qtdTplMaiorOcrr;

    public VwDshbrdIndic() {
    }

    public long getQtdAtdSemEquipe() {
        return qtdAtdSemEquipe;
    }

    public void setQtdAtdSemEquipe(long qtdAtdSemEquipe) {
        this.qtdAtdSemEquipe = qtdAtdSemEquipe;
    }

    public long getQtdDeOcrrAbertas() {
        return qtdDeOcrrAbertas;
    }

    public void setQtdDeOcrrAbertas(long qtdDeOcrrAbertas) {
        this.qtdDeOcrrAbertas = qtdDeOcrrAbertas;
    }

    public Integer getIdTipologia() {
        return idTipologia;
    }

    public void setIdTipologia(Integer idTipologia) {
        this.idTipologia = idTipologia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getQtdTplMaiorOcrr() {
        return qtdTplMaiorOcrr;
    }

    public void setQtdTplMaiorOcrr(long qtdTplMaiorOcrr) {
        this.qtdTplMaiorOcrr = qtdTplMaiorOcrr;
    }
    
}
