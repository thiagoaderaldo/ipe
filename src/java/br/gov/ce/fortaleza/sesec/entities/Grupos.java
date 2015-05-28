/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thiago
 */
@Entity
@Table(name = "grupos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupos.findAll", query = "SELECT g FROM Grupos g"),
    @NamedQuery(name = "Grupos.findByGrupoId", query = "SELECT g FROM Grupos g WHERE g.grupoId = :grupoId"),
    @NamedQuery(name = "Grupos.findByDescricao", query = "SELECT g FROM Grupos g WHERE g.descricao = :descricao"),
    @NamedQuery(name = "Grupos.findByUsuarioPadrao", query = "SELECT g FROM Grupos g WHERE g.usuarioPadrao = :usuarioPadrao")})
public class Grupos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "grupoId")
    private String grupoId;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "usuarioPadrao")
    private Boolean usuarioPadrao;
    @ManyToMany(mappedBy = "gruposCollection")
    private Collection<Usuarios> usuariosCollection;

    public Grupos() {
    }

    public Grupos(String grupoId) {
        this.grupoId = grupoId;
    }

    public Grupos(String grupoId, String descricao) {
        this.grupoId = grupoId;
        this.descricao = descricao;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getUsuarioPadrao() {
        return usuarioPadrao;
    }

    public void setUsuarioPadrao(Boolean usuarioPadrao) {
        this.usuarioPadrao = usuarioPadrao;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grupoId != null ? grupoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupos)) {
            return false;
        }
        Grupos other = (Grupos) object;
        if ((this.grupoId == null && other.grupoId != null) || (this.grupoId != null && !this.grupoId.equals(other.grupoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.grupoId;
    }
    
}
