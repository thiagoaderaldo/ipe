/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.VwDshbrdIndic;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author thiago
 */
public class VwDshbrdIndicJpaController implements Serializable {

    public VwDshbrdIndicJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VwDshbrdIndic vwDshbrdIndic) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vwDshbrdIndic);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVwDshbrdIndic(vwDshbrdIndic.getIdTipologia()) != null) {
                throw new PreexistingEntityException("VwDshbrdIndic " + vwDshbrdIndic + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwDshbrdIndic vwDshbrdIndic) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vwDshbrdIndic = em.merge(vwDshbrdIndic);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vwDshbrdIndic.getIdTipologia();
                if (findVwDshbrdIndic(id) == null) {
                    throw new NonexistentEntityException("The vwDshbrdIndic with id " + id + " no longer exists.");
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
            VwDshbrdIndic vwDshbrdIndic;
            try {
                vwDshbrdIndic = em.getReference(VwDshbrdIndic.class, id);
                vwDshbrdIndic.getIdTipologia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwDshbrdIndic with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwDshbrdIndic);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VwDshbrdIndic> findVwDshbrdIndicEntities() {
        return findVwDshbrdIndicEntities(true, -1, -1);
    }

    public List<VwDshbrdIndic> findVwDshbrdIndicEntities(int maxResults, int firstResult) {
        return findVwDshbrdIndicEntities(false, maxResults, firstResult);
    }

    private List<VwDshbrdIndic> findVwDshbrdIndicEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwDshbrdIndic.class));
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

    public VwDshbrdIndic findVwDshbrdIndic(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwDshbrdIndic.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwDshbrdIndicCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwDshbrdIndic> rt = cq.from(VwDshbrdIndic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
