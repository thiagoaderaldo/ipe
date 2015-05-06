/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "agentes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Agentes.findAll", query = "SELECT a FROM Agentes a"),
    @NamedQuery(name = "Agentes.findById", query = "SELECT a FROM Agentes a WHERE a.id = :id"),
    @NamedQuery(name = "Agentes.findByMatricula", query = "SELECT a FROM Agentes a WHERE a.matricula = :matricula"),
    @NamedQuery(name = "Agentes.findByNome", query = "SELECT a FROM Agentes a WHERE a.nome = :nome"),
    @NamedQuery(name = "Agentes.findByNomeGuerra", query = "SELECT a FROM Agentes a WHERE a.nomeGuerra = :nomeGuerra"),
    @NamedQuery(name = "Agentes.findByTelefone", query = "SELECT a FROM Agentes a WHERE a.telefone = :telefone")})
public class Agentes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "nome_guerra")
    private String nomeGuerra;
    @Column(name = "telefone")
    private String telefone;

    public Agentes() {
    }

    public Agentes(Integer id) {
        this.id = id;
    }

    public Agentes(Integer id, String matricula, String nome, String nomeGuerra) {
        this.id = id;
        this.matricula = matricula;
        this.nome = nome;
        this.nomeGuerra = nomeGuerra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeGuerra() {
        return nomeGuerra;
    }

    public void setNomeGuerra(String nomeGuerra) {
        this.nomeGuerra = nomeGuerra;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
        if (!(object instanceof Agentes)) {
            return false;
        }
        Agentes other = (Agentes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Agentes[ id=" + id + " ]";
    }
    
}
