/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.VwQtdTotalAtdCurrentDate;
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
public class VwQtdTotalAtdCurrentDateJpaController implements Serializable {

    public VwQtdTotalAtdCurrentDateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VwQtdTotalAtdCurrentDate vwQtdTotalAtdCurrentDate) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vwQtdTotalAtdCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVwQtdTotalAtdCurrentDate(vwQtdTotalAtdCurrentDate.getQtdAtd()) != null) {
                throw new PreexistingEntityException("VwQtdTotalAtdCurrentDate " + vwQtdTotalAtdCurrentDate + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwQtdTotalAtdCurrentDate vwQtdTotalAtdCurrentDate) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vwQtdTotalAtdCurrentDate = em.merge(vwQtdTotalAtdCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = vwQtdTotalAtdCurrentDate.getQtdAtd();
                if (findVwQtdTotalAtdCurrentDate(id) == null) {
                    throw new NonexistentEntityException("The vwQtdTotalAtdCurrentDate with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VwQtdTotalAtdCurrentDate vwQtdTotalAtdCurrentDate;
            try {
                vwQtdTotalAtdCurrentDate = em.getReference(VwQtdTotalAtdCurrentDate.class, id);
                vwQtdTotalAtdCurrentDate.getQtdAtd();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwQtdTotalAtdCurrentDate with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwQtdTotalAtdCurrentDate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VwQtdTotalAtdCurrentDate> findVwQtdTotalAtdCurrentDateEntities() {
        return findVwQtdTotalAtdCurrentDateEntities(true, -1, -1);
    }

    public List<VwQtdTotalAtdCurrentDate> findVwQtdTotalAtdCurrentDateEntities(int maxResults, int firstResult) {
        return findVwQtdTotalAtdCurrentDateEntities(false, maxResults, firstResult);
    }

    private List<VwQtdTotalAtdCurrentDate> findVwQtdTotalAtdCurrentDateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwQtdTotalAtdCurrentDate.class));
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

    public VwQtdTotalAtdCurrentDate findVwQtdTotalAtdCurrentDate(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwQtdTotalAtdCurrentDate.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwQtdTotalAtdCurrentDateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwQtdTotalAtdCurrentDate> rt = cq.from(VwQtdTotalAtdCurrentDate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
