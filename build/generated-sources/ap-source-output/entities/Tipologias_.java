package entities;

import entities.Atendimentos;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Tipologias.class)
public class Tipologias_ { 

    public static volatile SingularAttribute<Tipologias, Integer> id;
    public static volatile CollectionAttribute<Tipologias, Atendimentos> atendimentosCollection;
    public static volatile SingularAttribute<Tipologias, String> nome;

}