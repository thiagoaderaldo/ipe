/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.ChaveApi;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.gov.ce.fortaleza.sesec.entities.Usuarios;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jorge
 */
public class ChaveApiJpaController implements Serializable {

    public ChaveApiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ChaveApi chaveApi) throws PreexistingEntityException, Exception {
        if (chaveApi.getUsuariosCollection() == null) {
            chaveApi.setUsuariosCollection(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Usuarios> attachedUsuariosCollection = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionUsuariosToAttach : chaveApi.getUsuariosCollection()) {
                usuariosCollectionUsuariosToAttach = em.getReference(usuariosCollectionUsuariosToAttach.getClass(), usuariosCollectionUsuariosToAttach.getMatricula());
                attachedUsuariosCollection.add(usuariosCollectionUsuariosToAttach);
            }
            chaveApi.setUsuariosCollection(attachedUsuariosCollection);
            em.persist(chaveApi);
            for (Usuarios usuariosCollectionUsuarios : chaveApi.getUsuariosCollection()) {
                usuariosCollectionUsuarios.getChaveApiCollection().add(chaveApi);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findChaveApi(chaveApi.getChave()) != null) {
                throw new PreexistingEntityException("ChaveApi " + chaveApi + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ChaveApi chaveApi) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ChaveApi persistentChaveApi = em.find(ChaveApi.class, chaveApi.getChave());
            Collection<Usuarios> usuariosCollectionOld = persistentChaveApi.getUsuariosCollection();
            Collection<Usuarios> usuariosCollectionNew = chaveApi.getUsuariosCollection();
            Collection<Usuarios> attachedUsuariosCollectionNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosCollectionNewUsuariosToAttach : usuariosCollectionNew) {
                usuariosCollectionNewUsuariosToAttach = em.getReference(usuariosCollectionNewUsuariosToAttach.getClass(), usuariosCollectionNewUsuariosToAttach.getMatricula());
                attachedUsuariosCollectionNew.add(usuariosCollectionNewUsuariosToAttach);
            }
            usuariosCollectionNew = attachedUsuariosCollectionNew;
            chaveApi.setUsuariosCollection(usuariosCollectionNew);
            chaveApi = em.merge(chaveApi);
            for (Usuarios usuariosCollectionOldUsuarios : usuariosCollectionOld) {
                if (!usuariosCollectionNew.contains(usuariosCollectionOldUsuarios)) {
                    usuariosCollectionOldUsuarios.getChaveApiCollection().remove(chaveApi);
                    usuariosCollectionOldUsuarios = em.merge(usuariosCollectionOldUsuarios);
                }
            }
            for (Usuarios usuariosCollectionNewUsuarios : usuariosCollectionNew) {
                if (!usuariosCollectionOld.contains(usuariosCollectionNewUsuarios)) {
                    usuariosCollectionNewUsuarios.getChaveApiCollection().add(chaveApi);
                    usuariosCollectionNewUsuarios = em.merge(usuariosCollectionNewUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = chaveApi.getChave();
                if (findChaveApi(id) == null) {
                    throw new NonexistentEntityException("The chaveApi with id " + id + " no longer exists.");
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
            ChaveApi chaveApi;
            try {
                chaveApi = em.getReference(ChaveApi.class, id);
                chaveApi.getChave();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The chaveApi with id " + id + " no longer exists.", enfe);
            }
            Collection<Usuarios> usuariosCollection = chaveApi.getUsuariosCollection();
            for (Usuarios usuariosCollectionUsuarios : usuariosCollection) {
                usuariosCollectionUsuarios.getChaveApiCollection().remove(chaveApi);
                usuariosCollectionUsuarios = em.merge(usuariosCollectionUsuarios);
            }
            em.remove(chaveApi);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ChaveApi> findChaveApiEntities() {
        return findChaveApiEntities(true, -1, -1);
    }

    public List<ChaveApi> findChaveApiEntities(int maxResults, int firstResult) {
        return findChaveApiEntities(false, maxResults, firstResult);
    }

    private List<ChaveApi> findChaveApiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ChaveApi.class));
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

    public ChaveApi findChaveApi(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ChaveApi.class, id);
        } finally {
            em.close();
        }
    }

    public int getChaveApiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ChaveApi> rt = cq.from(ChaveApi.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public ChaveApi getChaveApiByKey(String serviceKey) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<ChaveApi> query = em.createNamedQuery(ChaveApi.FIND_BY_KEY, ChaveApi.class);
            query.setParameter("chave", serviceKey);
            return query.getSingleResult();
        }finally {
            em.close();
        }
    }
}
