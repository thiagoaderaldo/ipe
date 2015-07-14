/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thiago
 */
@Entity
@Table(name = "ser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ser.findAll", query = "SELECT s FROM Ser s"),
    @NamedQuery(name = "Ser.findById", query = "SELECT s FROM Ser s WHERE s.id = :id"),
    @NamedQuery(name = "Ser.findByNome", query = "SELECT s FROM Ser s WHERE s.nome = :nome")})
public class Ser implements Serializable {
    @OneToMany(mappedBy = "idSer")
    private Collection<Bairros> bairrosCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    

    public Ser() {
    }

    public Ser(Integer id) {
        this.id = id;
    }

    public Ser(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        if (!(object instanceof Ser)) {
            return false;
        }
        Ser other = (Ser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    @XmlTransient
    public Collection<Bairros> getBairrosCollection() {
        return bairrosCollection;
    }

    public void setBairrosCollection(Collection<Bairros> bairrosCollection) {
        this.bairrosCollection = bairrosCollection;
    }
    
}
