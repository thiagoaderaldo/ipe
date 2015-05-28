/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.entities.Tipologias;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author thiago
 */
public class TipologiasJpaController implements Serializable {

    public TipologiasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipologias tipologias) {
        if (tipologias.getAtendimentosCollection() == null) {
            tipologias.setAtendimentosCollection(new ArrayList<Atendimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Atendimentos> attachedAtendimentosCollection = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionAtendimentosToAttach : tipologias.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentosToAttach = em.getReference(atendimentosCollectionAtendimentosToAttach.getClass(), atendimentosCollectionAtendimentosToAttach.getId());
                attachedAtendimentosCollection.add(atendimentosCollectionAtendimentosToAttach);
            }
            tipologias.setAtendimentosCollection(attachedAtendimentosCollection);
            em.persist(tipologias);
            for (Atendimentos atendimentosCollectionAtendimentos : tipologias.getAtendimentosCollection()) {
                Tipologias oldIdTipologiaOfAtendimentosCollectionAtendimentos = atendimentosCollectionAtendimentos.getIdTipologia();
                atendimentosCollectionAtendimentos.setIdTipologia(tipologias);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
                if (oldIdTipologiaOfAtendimentosCollectionAtendimentos != null) {
                    oldIdTipologiaOfAtendimentosCollectionAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionAtendimentos);
                    oldIdTipologiaOfAtendimentosCollectionAtendimentos = em.merge(oldIdTipologiaOfAtendimentosCollectionAtendimentos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipologias tipologias) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipologias persistentTipologias = em.find(Tipologias.class, tipologias.getId());
            Collection<Atendimentos> atendimentosCollectionOld = persistentTipologias.getAtendimentosCollection();
            Collection<Atendimentos> atendimentosCollectionNew = tipologias.getAtendimentosCollection();
            Collection<Atendimentos> attachedAtendimentosCollectionNew = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionNewAtendimentosToAttach : atendimentosCollectionNew) {
                atendimentosCollectionNewAtendimentosToAttach = em.getReference(atendimentosCollectionNewAtendimentosToAttach.getClass(), atendimentosCollectionNewAtendimentosToAttach.getId());
                attachedAtendimentosCollectionNew.add(atendimentosCollectionNewAtendimentosToAttach);
            }
            atendimentosCollectionNew = attachedAtendimentosCollectionNew;
            tipologias.setAtendimentosCollection(atendimentosCollectionNew);
            tipologias = em.merge(tipologias);
            for (Atendimentos atendimentosCollectionOldAtendimentos : atendimentosCollectionOld) {
                if (!atendimentosCollectionNew.contains(atendimentosCollectionOldAtendimentos)) {
                    atendimentosCollectionOldAtendimentos.setIdTipologia(null);
                    atendimentosCollectionOldAtendimentos = em.merge(atendimentosCollectionOldAtendimentos);
                }
            }
            for (Atendimentos atendimentosCollectionNewAtendimentos : atendimentosCollectionNew) {
                if (!atendimentosCollectionOld.contains(atendimentosCollectionNewAtendimentos)) {
                    Tipologias oldIdTipologiaOfAtendimentosCollectionNewAtendimentos = atendimentosCollectionNewAtendimentos.getIdTipologia();
                    atendimentosCollectionNewAtendimentos.setIdTipologia(tipologias);
                    atendimentosCollectionNewAtendimentos = em.merge(atendimentosCollectionNewAtendimentos);
                    if (oldIdTipologiaOfAtendimentosCollectionNewAtendimentos != null && !oldIdTipologiaOfAtendimentosCollectionNewAtendimentos.equals(tipologias)) {
                        oldIdTipologiaOfAtendimentosCollectionNewAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionNewAtendimentos);
                        oldIdTipologiaOfAtendimentosCollectionNewAtendimentos = em.merge(oldIdTipologiaOfAtendimentosCollectionNewAtendimentos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipologias.getId();
                if (findTipologias(id) == null) {
                    throw new NonexistentEntityException("The tipologias with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipologias tipologias;
            try {
                tipologias = em.getReference(Tipologias.class, id);
                tipologias.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipologias with id " + id + " no longer exists.", enfe);
            }
            Collection<Atendimentos> atendimentosCollection = tipologias.getAtendimentosCollection();
            for (Atendimentos atendimentosCollectionAtendimentos : atendimentosCollection) {
                atendimentosCollectionAtendimentos.setIdTipologia(null);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
            }
            em.remove(tipologias);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipologias> findTipologiasEntities() {
        return findTipologiasEntities(true, -1, -1);
    }

    public List<Tipologias> findTipologiasEntities(int maxResults, int firstResult) {
        return findTipologiasEntities(false, maxResults, firstResult);
    }

    private List<Tipologias> findTipologiasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipologias.class));
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

    public Tipologias findTipologias(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipologias.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipologiasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipologias> rt = cq.from(Tipologias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
