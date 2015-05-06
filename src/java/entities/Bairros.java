/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "bairros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bairros.findAll", query = "SELECT b FROM Bairros b ORDER BY b.nome"),
    @NamedQuery(name = "Bairros.findById", query = "SELECT b FROM Bairros b WHERE b.id = :id ORDER BY b.nome"),
    @NamedQuery(name = "Bairros.findByNome", query = "SELECT b FROM Bairros b WHERE b.nome = :nome ORDER BY b.nome")})
public class Bairros implements Serializable {
    @JoinColumn(name = "id_ser", referencedColumnName = "id")
    @ManyToOne
    private Ser idSer;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome")
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBairro")
    private Collection<Atendimentos> atendimentosCollection;

    public Bairros() {
    }

    public Bairros(Integer id) {
        this.id = id;
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
        if (!(object instanceof Bairros)) {
            return false;
        }
        Bairros other = (Bairros) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getNome();
    }

    public Ser getIdSer() {
        return idSer;
    }

    public void setIdSer(Ser idSer) {
        this.idSer = idSer;
    }
    
}
