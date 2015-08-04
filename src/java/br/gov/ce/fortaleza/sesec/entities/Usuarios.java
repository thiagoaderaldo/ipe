/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author thiago
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByMatricula", query = "SELECT u FROM Usuarios u WHERE u.matricula = :matricula"),
    @NamedQuery(name = "Usuarios.findByNome", query = "SELECT u FROM Usuarios u WHERE u.nome = :nome"),
    @NamedQuery(name = "Usuarios.findByTelefone", query = "SELECT u FROM Usuarios u WHERE u.telefone = :telefone"),
    @NamedQuery(name = "Usuarios.findByCelular", query = "SELECT u FROM Usuarios u WHERE u.celular = :celular"),
    @NamedQuery(name = "Usuarios.findByLogin", query = "SELECT u FROM Usuarios u WHERE u.login = :login"),
    @NamedQuery(name = "Usuarios.findByEmail", query = "SELECT u FROM Usuarios u WHERE u.email = :email"),
    @NamedQuery(name = "Usuarios.findBySenha", query = "SELECT u FROM Usuarios u WHERE u.senha = :senha"),
    @NamedQuery(name = "Usuarios.innerJoinKey", query = "SELECT u FROM Usuarios u LEFT JOIN u.chaveApiCollection c WHERE u.login = :login"),
    @NamedQuery(name = "Usuarios.findUsuarioChavesByLogin", query = "SELECT u FROM Usuarios u LEFT JOIN u.chaveApiCollection c WHERE u.login = :login AND c.chave = :key")
})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String USUARIOS_INNER_JOIN = "Usuarios.innerJoinKey";
    public static final String USUARIOS_FIND_BY_LOGIN = "Usuarios.findByLogin";
    public static final String USUARIOS_FIND_CHAVES = "Usuarios.findUsuarioChavesByLogin";
    
    @Id
    @Basic(optional = false)
    @Column(name = "matricula")
    private String matricula;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "celular")
    private String celular;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "senha")
    private String senha;
    @JoinTable(name = "grupoDoUsuario", joinColumns = {
        @JoinColumn(name = "login", referencedColumnName = "login")}, inverseJoinColumns = {
        @JoinColumn(name = "grupoId", referencedColumnName = "grupoId")})
    @ManyToMany
    private Collection<Grupos> gruposCollection;
    @JoinTable(name = "usuario_x_chave", joinColumns = {
        @JoinColumn(name = "usuarios_matricula", referencedColumnName = "matricula")}, inverseJoinColumns = {
        @JoinColumn(name = "chave_api_chave", referencedColumnName = "chave")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<ChaveApi> chaveApiCollection;

    public Usuarios() {
    }

    public Usuarios(String matricula) {
        this.matricula = matricula;
    }

    public Usuarios(String matricula, String nome, String login, String senha) {
        this.matricula = matricula;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @XmlTransient
    public Collection<Grupos> getGruposCollection() {
        return gruposCollection;
    }

    public void setGruposCollection(Collection<Grupos> gruposCollection) {
        this.gruposCollection = gruposCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Usuarios[ matricula=" + matricula + " ]";
    }

    //@XmlTransient
    //@JsonIgnore
    public Collection<ChaveApi> getChaveApiCollection() {
        if (chaveApiCollection == null) {
            chaveApiCollection = new ArrayList<ChaveApi>();
        }
        return chaveApiCollection;
    }

    public void setChaveApiCollection(Collection<ChaveApi> chaveApiCollection) {
        this.chaveApiCollection = chaveApiCollection;
    }
}
