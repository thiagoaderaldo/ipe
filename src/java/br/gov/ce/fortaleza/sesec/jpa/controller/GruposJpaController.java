/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.Grupos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class GruposJpaController implements Serializable {

    public GruposJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupos grupos) throws PreexistingEntityException, Exception {
        if (grupos.getUsuariosCollection() == null) {
            grupos.setUsuariosCollection(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Usuarios> attachedUsuariosCollection = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionUsuariosToAttach : grupos.getUsuariosCollection()) {
                usuariosCollectionUsuariosToAttach = em.getReference(usuariosCollectionUsuariosToAttach.getClass(), usuariosCollectionUsuariosToAttach.getMatricula());
                attachedUsuariosCollection.add(usuariosCollectionUsuariosToAttach);
            }
            grupos.setUsuariosCollection(attachedUsuariosCollection);
            em.persist(grupos);
            for (Usuarios usuariosCollectionUsuarios : grupos.getUsuariosCollection()) {
                usuariosCollectionUsuarios.getGruposCollection().add(grupos);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupos(grupos.getGrupoId()) != null) {
                throw new PreexistingEntityException("Grupos " + grupos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grupos grupos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupos persistentGrupos = em.find(Grupos.class, grupos.getGrupoId());
            Collection<Usuarios> usuariosCollectionOld = persistentGrupos.getUsuariosCollection();
            Collection<Usuarios> usuariosCollectionNew = grupos.getUsuariosCollection();
            Collection<Usuarios> attachedUsuariosCollectionNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionNewUsuariosToAttach : usuariosCollectionNew) {
                usuariosCollectionNewUsuariosToAttach = em.getReference(usuariosCollectionNewUsuariosToAttach.getClass(), usuariosCollectionNewUsuariosToAttach.getMatricula());
                attachedUsuariosCollectionNew.add(usuariosCollectionNewUsuariosToAttach);
            }
            usuariosCollectionNew = attachedUsuariosCollectionNew;
            grupos.setUsuariosCollection(usuariosCollectionNew);
            grupos = em.merge(grupos);
            for (Usuarios usuariosCollectionOldUsuarios : usuariosCollectionOld) {
                if (!usuariosCollectionNew.contains(usuariosCollectionOldUsuarios)) {
                    usuariosCollectionOldUsuarios.getGruposCollection().remove(grupos);
                    usuariosCollectionOldUsuarios = em.merge(usuariosCollectionOldUsuarios);
                }
            }
            for (Usuarios usuariosCollectionNewUsuarios : usuariosCollectionNew) {
                if (!usuariosCollectionOld.contains(usuariosCollectionNewUsuarios)) {
                    usuariosCollectionNewUsuarios.getGruposCollection().add(grupos);
                    usuariosCollectionNewUsuarios = em.merge(usuariosCollectionNewUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = grupos.getGrupoId();
                if (findGrupos(id) == null) {
                    throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.");
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
            Grupos grupos;
            try {
                grupos = em.getReference(Grupos.class, id);
                grupos.getGrupoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.", enfe);
            }
            Collection<Usuarios> usuariosCollection = grupos.getUsuariosCollection();
            for (Usuarios usuariosCollectionUsuarios : usuariosCollection) {
                usuariosCollectionUsuarios.getGruposCollection().remove(grupos);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
            }
            em.remove(grupos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grupos> findGruposEntities() {
        return findGruposEntities(true, -1, -1);
    }

    public List<Grupos> findGruposEntities(int maxResults, int firstResult) {
        return findGruposEntities(false, maxResults, firstResult);
    }

    private List<Grupos> findGruposEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupos.class));
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

    public Grupos findGrupos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGruposCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupos> rt = cq.from(Grupos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
