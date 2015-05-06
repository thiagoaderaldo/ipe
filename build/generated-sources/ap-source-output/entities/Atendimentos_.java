package entities;

import entities.Bairros;
import entities.Encaminhamentos;
import entities.Estatus;
import entities.Ser;
import entities.Tipologias;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Atendimentos.class)
public class Atendimentos_ { 

    public static volatile SingularAttribute<Atendimentos, String> proprietarioResidencia;
    public static volatile SingularAttribute<Atendimentos, String> solicitante;
    public static volatile SingularAttribute<Atendimentos, String> descFinal;
    public static volatile SingularAttribute<Atendimentos, Date> dataSolicitacao;
    public static volatile SingularAttribute<Atendimentos, String> pontoReferencia;
    public static volatile SingularAttribute<Atendimentos, Integer> qtdPessoasAtingidas;
    public static volatile SingularAttribute<Atendimentos, Date> dataConclusao;
    public static volatile SingularAttribute<Atendimentos, Long> id;
    public static volatile SingularAttribute<Atendimentos, String> procedencia;
    public static volatile SingularAttribute<Atendimentos, Date> horaConclusao;
    public static volatile SingularAttribute<Atendimentos, String> numCasa;
    public static volatile SingularAttribute<Atendimentos, Ser> idSer;
    public static volatile SingularAttribute<Atendimentos, Estatus> idEstatus;
    public static volatile SingularAttribute<Atendimentos, String> descInicial;
    public static volatile SingularAttribute<Atendimentos, Bairros> idBairro;
    public static volatile CollectionAttribute<Atendimentos, Encaminhamentos> encaminhamentosCollection;
    public static volatile SingularAttribute<Atendimentos, Tipologias> idTipologia;
    public static volatile SingularAttribute<Atendimentos, String> protocolo;
    public static volatile SingularAttribute<Atendimentos, String> vtr;
    public static volatile SingularAttribute<Atendimentos, String> observacao;
    public static volatile SingularAttribute<Atendimentos, String> titulo;
    public static volatile SingularAttribute<Atendimentos, Date> horaSolicitacao;
    public static volatile SingularAttribute<Atendimentos, String> telSolicitante;
    public static volatile SingularAttribute<Atendimentos, Integer> qtdComodosCasa;
    public static volatile SingularAttribute<Atendimentos, String> responsavel;
    public static volatile SingularAttribute<Atendimentos, String> equipe;
    public static volatile SingularAttribute<Atendimentos, String> localizacao;
    public static volatile SingularAttribute<Atendimentos, String> protProcedencia;

}