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
@Table(name = "vw_max_atd_por_rgnl_current_date")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwMaxAtdPorRgnlCurrentDate.findAll", query = "SELECT v FROM VwMaxAtdPorRgnlCurrentDate v"),
    @NamedQuery(name = "VwMaxAtdPorRgnlCurrentDate.findByIdSer", query = "SELECT v FROM VwMaxAtdPorRgnlCurrentDate v WHERE v.idSer = :idSer"),
    @NamedQuery(name = "VwMaxAtdPorRgnlCurrentDate.findByNome", query = "SELECT v FROM VwMaxAtdPorRgnlCurrentDate v WHERE v.nome = :nome"),
    @NamedQuery(name = "VwMaxAtdPorRgnlCurrentDate.findByQtdAtd", query = "SELECT v FROM VwMaxAtdPorRgnlCurrentDate v WHERE v.qtdAtd = :qtdAtd")})
public class VwMaxAtdPorRgnlCurrentDate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "id_ser")
    @Id
    private Integer idSer;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "qtd_atd")
    private long qtdAtd;

    public VwMaxAtdPorRgnlCurrentDate() {
    }

    public Integer getIdSer() {
        return idSer;
    }

    public void setIdSer(Integer idSer) {
        this.idSer = idSer;
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
