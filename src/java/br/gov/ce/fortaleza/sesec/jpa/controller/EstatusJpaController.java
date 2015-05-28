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
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.entities.Estatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.IllegalOrphanException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author thiago
 */
public class EstatusJpaController implements Serializable {

    public EstatusJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estatus estatus) {
        if (estatus.getAtendimentosCollection() == null) {
            estatus.setAtendimentosCollection(new ArrayList<Atendimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Atendimentos> attachedAtendimentosCollection = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionAtendimentosToAttach : estatus.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentosToAttach = em.getReference(atendimentosCollectionAtendimentosToAttach.getClass(), atendimentosCollectionAtendimentosToAttach.getId());
                attachedAtendimentosCollection.add(atendimentosCollectionAtendimentosToAttach);
            }
            estatus.setAtendimentosCollection(attachedAtendimentosCollection);
            em.persist(estatus);
            for (Atendimentos atendimentosCollectionAtendimentos : estatus.getAtendimentosCollection()) {
                Estatus oldIdEstatusOfAtendimentosCollectionAtendimentos = atendimentosCollectionAtendimentos.getIdEstatus();
                atendimentosCollectionAtendimentos.setIdEstatus(estatus);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
                if (oldIdEstatusOfAtendimentosCollectionAtendimentos != null) {
                    oldIdEstatusOfAtendimentosCollectionAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionAtendimentos);
                    oldIdEstatusOfAtendimentosCollectionAtendimentos = em.merge(oldIdEstatusOfAtendimentosCollectionAtendimentos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estatus estatus) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estatus persistentEstatus = em.find(Estatus.class, estatus.getId());
            Collection<Atendimentos> atendimentosCollectionOld = persistentEstatus.getAtendimentosCollection();
            Collection<Atendimentos> atendimentosCollectionNew = estatus.getAtendimentosCollection();
            List<String> illegalOrphanMessages = null;
            for (Atendimentos atendimentosCollectionOldAtendimentos : atendimentosCollectionOld) {
                if (!atendimentosCollectionNew.contains(atendimentosCollectionOldAtendimentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Atendimentos " + atendimentosCollectionOldAtendimentos + " since its idEstatus field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Atendimentos> attachedAtendimentosCollectionNew = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionNewAtendimentosToAttach : atendimentosCollectionNew) {
                atendimentosCollectionNewAtendimentosToAttach = em.getReference(atendimentosCollectionNewAtendimentosToAttach.getClass(), atendimentosCollectionNewAtendimentosToAttach.getId());
                attachedAtendimentosCollectionNew.add(atendimentosCollectionNewAtendimentosToAttach);
            }
            atendimentosCollectionNew = attachedAtendimentosCollectionNew;
            estatus.setAtendimentosCollection(atendimentosCollectionNew);
            estatus = em.merge(estatus);
            for (Atendimentos atendimentosCollectionNewAtendimentos : atendimentosCollectionNew) {
                if (!atendimentosCollectionOld.contains(atendimentosCollectionNewAtendimentos)) {
                    Estatus oldIdEstatusOfAtendimentosCollectionNewAtendimentos = atendimentosCollectionNewAtendimentos.getIdEstatus();
                    atendimentosCollectionNewAtendimentos.setIdEstatus(estatus);
                    atendimentosCollectionNewAtendimentos = em.merge(atendimentosCollectionNewAtendimentos);
                    if (oldIdEstatusOfAtendimentosCollectionNewAtendimentos != null && !oldIdEstatusOfAtendimentosCollectionNewAtendimentos.equals(estatus)) {
                        oldIdEstatusOfAtendimentosCollectionNewAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionNewAtendimentos);
                        oldIdEstatusOfAtendimentosCollectionNewAtendimentos = em.merge(oldIdEstatusOfAtendimentosCollectionNewAtendimentos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estatus.getId();
                if (findEstatus(id) == null) {
                    throw new NonexistentEntityException("The estatus with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estatus estatus;
            try {
                estatus = em.getReference(Estatus.class, id);
                estatus.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estatus with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Atendimentos> atendimentosCollectionOrphanCheck = estatus.getAtendimentosCollection();
            for (Atendimentos atendimentosCollectionOrphanCheckAtendimentos : atendimentosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estatus (" + estatus + ") cannot be destroyed since the Atendimentos " + atendimentosCollectionOrphanCheckAtendimentos + " in its atendimentosCollection field has a non-nullable idEstatus field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estatus);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estatus> findEstatusEntities() {
        return findEstatusEntities(true, -1, -1);
    }

    public List<Estatus> findEstatusEntities(int maxResults, int firstResult) {
        return findEstatusEntities(false, maxResults, firstResult);
    }

    private List<Estatus> findEstatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estatus.class));
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

    public Estatus findEstatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estatus> rt = cq.from(Estatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
