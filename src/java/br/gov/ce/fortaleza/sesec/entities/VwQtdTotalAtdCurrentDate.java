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
@Table(name = "vw_qtd_total_atd_current_date")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwQtdTotalAtdCurrentDate.findAll", query = "SELECT v FROM VwQtdTotalAtdCurrentDate v"),
    @NamedQuery(name = "VwQtdTotalAtdCurrentDate.findByQtdAtd", query = "SELECT v FROM VwQtdTotalAtdCurrentDate v WHERE v.qtdAtd = :qtdAtd")})
public class VwQtdTotalAtdCurrentDate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "qtd_atd")
    @Id
    private long qtdAtd;

    public VwQtdTotalAtdCurrentDate() {
    }

    public long getQtdAtd() {
        return qtdAtd;
    }

    public void setQtdAtd(long qtdAtd) {
        this.qtdAtd = qtdAtd;
    }
    
}
