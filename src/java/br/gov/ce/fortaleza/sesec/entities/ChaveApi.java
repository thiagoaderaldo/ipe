/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Jorge
 */
@Entity
@Table(name = "chave_api")
@XmlRootElement
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "ChaveApi.findAll", query = "SELECT c FROM ChaveApi c"),
    @NamedQuery(name = "ChaveApi.findByChave", query = "SELECT c FROM ChaveApi c WHERE c.chave = :chave"),
    @NamedQuery(name = "ChaveApi.findByNomeSistem", query = "SELECT c FROM ChaveApi c WHERE c.nomeSistem = :nomeSistem")})
public class ChaveApi implements Serializable {
    
    public static final String FIND_BY_KEY = "ChaveApi.findByChave";
    
    @ManyToMany(mappedBy = "chaveApiCollection")
    private Collection<Usuarios> usuariosCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "chave")
    private String chave;
    @Column(name = "nome_sistem")
    private String nomeSistem;

    public ChaveApi() {
    }

    public ChaveApi(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getNomeSistem() {
        return nomeSistem;
    }

    public void setNomeSistem(String nomeSistem) {
        this.nomeSistem = nomeSistem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chave != null ? chave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChaveApi)) {
            return false;
        }
        ChaveApi other = (ChaveApi) object;
        if ((this.chave == null && other.chave != null) || (this.chave != null && !this.chave.equals(other.chave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ce.fortaleza.sesec.entities.ChaveApi[ chave=" + chave + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }
    
}
