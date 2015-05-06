package entities;

import entities.Usuarios;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2015-05-05T00:29:28")
@StaticMetamodel(Grupos.class)
public class Grupos_ { 

    public static volatile SingularAttribute<Grupos, Boolean> usuarioPadrao;
    public static volatile CollectionAttribute<Grupos, Usuarios> usuariosCollection;
    public static volatile SingularAttribute<Grupos, String> grupoId;
    public static volatile SingularAttribute<Grupos, String> descricao;

}