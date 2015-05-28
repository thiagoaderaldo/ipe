/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.gov.ce.fortaleza.sesec.entities.Grupos;
import br.gov.ce.fortaleza.sesec.entities.Usuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author thiago
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getGruposCollection() == null) {
            usuarios.setGruposCollection(new ArrayList<Grupos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Grupos> attachedGruposCollection = new ArrayList<Grupos>();
            for (Grupos gruposCollectionGruposToAttach : usuarios.getGruposCollection()) {
                gruposCollectionGruposToAttach = em.getReference(gruposCollectionGruposToAttach.getClass(), gruposCollectionGruposToAttach.getGrupoId());
                attachedGruposCollection.add(gruposCollectionGruposToAttach);
            }
            usuarios.setGruposCollection(attachedGruposCollection);
            em.persist(usuarios);
            for (Grupos gruposCollectionGrupos : usuarios.getGruposCollection()) {
                gruposCollectionGrupos.getUsuariosCollection().add(usuarios);
                gruposCollectionGrupos = em.merge(gruposCollectionGrupos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getMatricula()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getMatricula());
            Collection<Grupos> gruposCollectionOld = persistentUsuarios.getGruposCollection();
            Collection<Grupos> gruposCollectionNew = usuarios.getGruposCollection();
            Collection<Grupos> attachedGruposCollectionNew = new ArrayList<Grupos>();
            for (Grupos gruposCollectionNewGruposToAttach : gruposCollectionNew) {
                gruposCollectionNewGruposToAttach = em.getReference(gruposCollectionNewGruposToAttach.getClass(), gruposCollectionNewGruposToAttach.getGrupoId());
                attachedGruposCollectionNew.add(gruposCollectionNewGruposToAttach);
            }
            gruposCollectionNew = attachedGruposCollectionNew;
            usuarios.setGruposCollection(gruposCollectionNew);
            usuarios = em.merge(usuarios);
            for (Grupos gruposCollectionOldGrupos : gruposCollectionOld) {
                if (!gruposCollectionNew.contains(gruposCollectionOldGrupos)) {
                    gruposCollectionOldGrupos.getUsuariosCollection().remove(usuarios);
                    gruposCollectionOldGrupos = em.merge(gruposCollectionOldGrupos);
                }
            }
            for (Grupos gruposCollectionNewGrupos : gruposCollectionNew) {
                if (!gruposCollectionOld.contains(gruposCollectionNewGrupos)) {
                    gruposCollectionNewGrupos.getUsuariosCollection().add(usuarios);
                    gruposCollectionNewGrupos = em.merge(gruposCollectionNewGrupos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarios.getMatricula();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getMatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Collection<Grupos> gruposCollection = usuarios.getGruposCollection();
            for (Grupos gruposCollectionGrupos : gruposCollection) {
                gruposCollectionGrupos.getUsuariosCollection().remove(usuarios);
                gruposCollectionGrupos = em.merge(gruposCollectionGrupos);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
