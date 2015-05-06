package entities;

import entities.Atendimentos;
import entities.Bairros;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Ser.class)
public class Ser_ { 

    public static volatile SingularAttribute<Ser, Integer> id;
    public static volatile CollectionAttribute<Ser, Atendimentos> atendimentosCollection;
    public static volatile CollectionAttribute<Ser, Bairros> bairrosCollection;
    public static volatile SingularAttribute<Ser, String> nome;

}