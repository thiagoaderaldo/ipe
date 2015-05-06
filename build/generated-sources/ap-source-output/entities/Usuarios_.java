package entities;

import entities.Grupos;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Usuarios.class)
public class Usuarios_ { 

    public static volatile SingularAttribute<Usuarios, String> email;
    public static volatile SingularAttribute<Usuarios, String> telefone;
    public static volatile CollectionAttribute<Usuarios, Grupos> gruposCollection;
    public static volatile SingularAttribute<Usuarios, String> nome;
    public static volatile SingularAttribute<Usuarios, String> login;
    public static volatile SingularAttribute<Usuarios, String> senha;
    public static volatile SingularAttribute<Usuarios, String> matricula;
    public static volatile SingularAttribute<Usuarios, String> celular;

}