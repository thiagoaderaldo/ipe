/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "encaminhamentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encaminhamentos.findAll", query = "SELECT e FROM Encaminhamentos e"),
    @NamedQuery(name = "Encaminhamentos.findById", query = "SELECT e FROM Encaminhamentos e WHERE e.id = :id")})
public class Encaminhamentos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "descricao")
    private String descricao;
    @ManyToMany(mappedBy = "encaminhamentosCollection")
    private Collection<Atendimentos> atendimentosCollection;

    public Encaminhamentos() {
    }

    public Encaminhamentos(Integer id) {
        this.id = id;
    }

    public Encaminhamentos(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Atendimentos> getAtendimentosCollection() {
        return atendimentosCollection;
    }

    public void setAtendimentosCollection(Collection<Atendimentos> atendimentosCollection) {
        this.atendimentosCollection = atendimentosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Encaminhamentos)) {
            return false;
        }
        Encaminhamentos other = (Encaminhamentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Encaminhamentos[ id=" + id + " ]";
    }
    
}
