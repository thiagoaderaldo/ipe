/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author thiago
 */
@Entity
@Table(name = "atendimentos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atendimentos.findAll", query = "SELECT a FROM Atendimentos a"),
    @NamedQuery(name = "Atendimentos.findById", query = "SELECT a FROM Atendimentos a WHERE a.id = :id"),
    @NamedQuery(name = "Atendimentos.findByProtocolo", query = "SELECT a FROM Atendimentos a WHERE a.protocolo = :protocolo"),
    @NamedQuery(name = "Atendimentos.findByTitulo", query = "SELECT a FROM Atendimentos a WHERE a.titulo = :titulo"),
    @NamedQuery(name = "Atendimentos.findByProcedencia", query = "SELECT a FROM Atendimentos a WHERE a.procedencia = :procedencia"),
    @NamedQuery(name = "Atendimentos.findByProtProcedencia", query = "SELECT a FROM Atendimentos a WHERE a.protProcedencia = :protProcedencia"),
    @NamedQuery(name = "Atendimentos.findByDataSolicitacao", query = "SELECT a FROM Atendimentos a WHERE a.dataSolicitacao = :dataSolicitacao"),
    @NamedQuery(name = "Atendimentos.findByHoraSolicitacao", query = "SELECT a FROM Atendimentos a WHERE a.horaSolicitacao = :horaSolicitacao"),
    @NamedQuery(name = "Atendimentos.findByLocalizacao", query = "SELECT a FROM Atendimentos a WHERE a.localizacao = :localizacao"),
    @NamedQuery(name = "Atendimentos.findBySolicitante", query = "SELECT a FROM Atendimentos a WHERE a.solicitante = :solicitante"),
    @NamedQuery(name = "Atendimentos.findByResponsavel", query = "SELECT a FROM Atendimentos a WHERE a.responsavel = :responsavel"),
    @NamedQuery(name = "Atendimentos.findAtendimentosByEstatus", query = "SELECT a FROM Atendimentos a WHERE a.idEstatus = :idEstatus"),
    @NamedQuery(name = "Atendimentos.findByTelSolicitante", query = "SELECT a FROM Atendimentos a WHERE a.telSolicitante = :telSolicitante"),
    @NamedQuery(name = "Atendimentos.findByDataConclusao", query = "SELECT a FROM Atendimentos a WHERE a.dataConclusao = :dataConclusao"),
    @NamedQuery(name = "Atendimentos.findByHoraConclusao", query = "SELECT a FROM Atendimentos a WHERE a.horaConclusao = :horaConclusao"),
    @NamedQuery(name = "Atendimentos.findByObservacao", query = "SELECT a FROM Atendimentos a WHERE a.observacao = :observacao"),
    @NamedQuery(name = "Atendimentos.findByDataAtendimento", query = "SELECT a FROM Atendimentos a WHERE a.dataSolicitacao = :dataSolicitacao"),
    @NamedQuery(name = "Atendimentos.findByBetweenDate1AndDate2", query = "SELECT a FROM Atendimentos a WHERE a.dataSolicitacao BETWEEN :data1 AND :data2"),
    @NamedQuery(name = "Atendimentos.findAtendimentoLikeResponsavel", query = "SELECT a FROM Atendimentos a WHERE a.responsavel LIKE :responsavel ORDER BY a.responsavel"),
    @NamedQuery(name = "Atendimentos.findByEquipe", query = "SELECT a FROM Atendimentos a WHERE a.equipe = :equipe"),
    @NamedQuery(name = "Atendimentos.findAtendimentosWhereEquipeIsNull", query = "SELECT a FROM Atendimentos a WHERE a.equipe IS NULL")})
public class Atendimentos implements Serializable {
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;

    @Column(name = "num_casa")
    private String numCasa;
    @Column(name = "ponto_referencia")
    private String pontoReferencia;
    @Column(name = "equipe")
    private String equipe;
    @Column(name = "vtr")
    private String vtr;
    @Column(name = "proprietario_residencia")
    private String proprietarioResidencia;
    @Column(name = "qtd_comodos_casa")
    private Integer qtdComodosCasa;
    @Column(name = "qtd_pessoas_atingidas")
    private Integer qtdPessoasAtingidas;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "protocolo")
    private String protocolo;
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "procedencia")
    private String procedencia;
    @Column(name = "prot_procedencia")
    private String protProcedencia;
    @Basic(optional = false)
    @Column(name = "data_solicitacao")
    @Temporal(TemporalType.DATE)
    private Date dataSolicitacao;
    @Basic(optional = false)
    @Column(name = "hora_solicitacao")
    @Temporal(TemporalType.TIME)
    private Date horaSolicitacao;
    @Basic(optional = false)
    @Column(name = "localizacao")
    private String localizacao;
    @Basic(optional = false)
    @Column(name = "solicitante")
    private String solicitante;
    @Column(name = "responsavel")
    private String responsavel;
    @Column(name = "tel_solicitante")
    private String telSolicitante;
    @Basic(optional = false)
    @Lob
    @Column(name = "desc_inicial")
    private String descInicial;
    @Lob
    @Column(name = "desc_final")
    private String descFinal;
    @Column(name = "data_conclusao")
    @Temporal(TemporalType.DATE)
    private Date dataConclusao;
    @Column(name = "hora_conclusao")
    @Temporal(TemporalType.TIME)
    private Date horaConclusao;
    @Column(name = "observacao")
    private String observacao;
    @JoinTable(name = "atd_x_enc", joinColumns = {
        @JoinColumn(name = "id_atendimento", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_encaminhamento", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Encaminhamentos> encaminhamentosCollection;
    @JoinColumn(name = "id_estatus", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Estatus idEstatus;
    @JoinColumn(name = "id_tipologia", referencedColumnName = "id")
    @ManyToOne
    private Tipologias idTipologia;
    @JoinColumn(name = "id_ser", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Ser idSer;
    @JoinColumn(name = "id_bairro", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bairros idBairro;

    public Atendimentos() {
    }

    public Atendimentos(Long id) {
        this.id = id;
    }

    public Atendimentos(Long id, String titulo, String procedencia, Date dataSolicitacao, Date horaSolicitacao, String localizacao, String solicitante, String responsavel, String descInicial) {
        this.id = id;
        this.titulo = titulo;
        this.procedencia = procedencia;
        this.dataSolicitacao = dataSolicitacao;
        this.horaSolicitacao = horaSolicitacao;
        this.localizacao = localizacao;
        this.solicitante = solicitante;
        this.responsavel = responsavel;
        this.descInicial = descInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    public String getProtProcedencia() {
        return protProcedencia;
    }

    public void setProtProcedencia(String protProcedencia) {
        this.protProcedencia = protProcedencia;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Date getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setHoraSolicitacao(Date horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelSolicitante() {
        return telSolicitante;
    }

    public void setTelSolicitante(String telSolicitante) {
        this.telSolicitante = telSolicitante;
    }

    public String getDescInicial() {
        return descInicial;
    }

    public void setDescInicial(String descInicial) {
        this.descInicial = descInicial;
    }

    public String getDescFinal() {
        return descFinal;
    }

    public void setDescFinal(String descFinal) {
        this.descFinal = descFinal;
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public Date getHoraConclusao() {
        return horaConclusao;
    }

    public void setHoraConclusao(Date horaConclusao) {
        this.horaConclusao = horaConclusao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @XmlTransient
    public Collection<Encaminhamentos> getEncaminhamentosCollection() {
        return encaminhamentosCollection;
    }

    public void setEncaminhamentosCollection(Collection<Encaminhamentos> encaminhamentosCollection) {
        this.encaminhamentosCollection = encaminhamentosCollection;
    }

    public Estatus getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Estatus idEstatus) {
        this.idEstatus = idEstatus;
    }

    public Tipologias getIdTipologia() {
        return idTipologia;
    }

    public void setIdTipologia(Tipologias idTipologia) {
        this.idTipologia = idTipologia;
    }

    public Ser getIdSer() {
        return idSer;
    }

    public void setIdSer(Ser idSer) {
        this.idSer = idSer;
    }

    public Bairros getIdBairro() {
        return idBairro;
    }

    public void setIdBairro(Bairros idBairro) {
        this.idBairro = idBairro;
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
        if (!(object instanceof Atendimentos)) {
            return false;
        }
        Atendimentos other = (Atendimentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Atendimentos[ id=" + id + " ]";
    }

    public String getNumCasa() {
        return numCasa;
    }

    public void setNumCasa(String numCasa) {
        this.numCasa = numCasa;
    }

    public String getPontoReferencia() {
        return pontoReferencia;
    }

    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getVtr() {
        return vtr;
    }

    public void setVtr(String vtr) {
        this.vtr = vtr;
    }

    public String getProprietarioResidencia() {
        return proprietarioResidencia;
    }

    public void setProprietarioResidencia(String proprietarioResidencia) {
        this.proprietarioResidencia = proprietarioResidencia;
    }

    public Integer getQtdComodosCasa() {
        return qtdComodosCasa;
    }

    public void setQtdComodosCasa(Integer qtdComodosCasa) {
        this.qtdComodosCasa = qtdComodosCasa;
    }

    public Integer getQtdPessoasAtingidas() {
        return qtdPessoasAtingidas;
    }

    public void setQtdPessoasAtingidas(Integer qtdPessoasAtingidas) {
        this.qtdPessoasAtingidas = qtdPessoasAtingidas;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
