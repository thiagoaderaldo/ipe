package entities;

import entities.Atendimentos;
import entities.Ser;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Bairros.class)
public class Bairros_ { 

    public static volatile SingularAttribute<Bairros, Integer> id;
    public static volatile CollectionAttribute<Bairros, Atendimentos> atendimentosCollection;
    public static volatile SingularAttribute<Bairros, String> nome;
    public static volatile SingularAttribute<Bairros, Ser> idSer;

}