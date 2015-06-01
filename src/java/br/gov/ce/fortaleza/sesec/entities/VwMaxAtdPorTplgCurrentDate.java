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
@Table(name = "vw_max_atd_por_tplg_current_date")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwMaxAtdPorTplgCurrentDate.findAll", query = "SELECT v FROM VwMaxAtdPorTplgCurrentDate v"),
    @NamedQuery(name = "VwMaxAtdPorTplgCurrentDate.findByIdTipologia", query = "SELECT v FROM VwMaxAtdPorTplgCurrentDate v WHERE v.idTipologia = :idTipologia"),
    @NamedQuery(name = "VwMaxAtdPorTplgCurrentDate.findByNome", query = "SELECT v FROM VwMaxAtdPorTplgCurrentDate v WHERE v.nome = :nome"),
    @NamedQuery(name = "VwMaxAtdPorTplgCurrentDate.findByQtdAtd", query = "SELECT v FROM VwMaxAtdPorTplgCurrentDate v WHERE v.qtdAtd = :qtdAtd")})
public class VwMaxAtdPorTplgCurrentDate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id_tipologia")
    @Id
    private Integer idTipologia;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "qtd_atd")
    private long qtdAtd;

    public VwMaxAtdPorTplgCurrentDate() {
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

    public long getQtdAtd() {
        return qtdAtd;
    }

    public void setQtdAtd(long qtdAtd) {
        this.qtdAtd = qtdAtd;
    }
    
}
