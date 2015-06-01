/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.VwMaxAtdPorRgnlCurrentDate;
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
public class VwMaxAtdPorRgnlCurrentDateJpaController implements Serializable {

    public VwMaxAtdPorRgnlCurrentDateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VwMaxAtdPorRgnlCurrentDate vwMaxAtdPorRgnlCurrentDate) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vwMaxAtdPorRgnlCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVwMaxAtdPorRgnlCurrentDate(vwMaxAtdPorRgnlCurrentDate.getIdSer()) != null) {
                throw new PreexistingEntityException("VwMaxAtdPorRgnlCurrentDate " + vwMaxAtdPorRgnlCurrentDate + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwMaxAtdPorRgnlCurrentDate vwMaxAtdPorRgnlCurrentDate) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vwMaxAtdPorRgnlCurrentDate = em.merge(vwMaxAtdPorRgnlCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vwMaxAtdPorRgnlCurrentDate.getIdSer();
                if (findVwMaxAtdPorRgnlCurrentDate(id) == null) {
                    throw new NonexistentEntityException("The vwMaxAtdPorRgnlCurrentDate with id " + id + " no longer exists.");
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
            VwMaxAtdPorRgnlCurrentDate vwMaxAtdPorRgnlCurrentDate;
            try {
                vwMaxAtdPorRgnlCurrentDate = em.getReference(VwMaxAtdPorRgnlCurrentDate.class, id);
                vwMaxAtdPorRgnlCurrentDate.getIdSer();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwMaxAtdPorRgnlCurrentDate with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwMaxAtdPorRgnlCurrentDate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VwMaxAtdPorRgnlCurrentDate> findVwMaxAtdPorRgnlCurrentDateEntities() {
        return findVwMaxAtdPorRgnlCurrentDateEntities(true, -1, -1);
    }

    public List<VwMaxAtdPorRgnlCurrentDate> findVwMaxAtdPorRgnlCurrentDateEntities(int maxResults, int firstResult) {
        return findVwMaxAtdPorRgnlCurrentDateEntities(false, maxResults, firstResult);
    }

    private List<VwMaxAtdPorRgnlCurrentDate> findVwMaxAtdPorRgnlCurrentDateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwMaxAtdPorRgnlCurrentDate.class));
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

    public VwMaxAtdPorRgnlCurrentDate findVwMaxAtdPorRgnlCurrentDate(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwMaxAtdPorRgnlCurrentDate.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwMaxAtdPorRgnlCurrentDateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwMaxAtdPorRgnlCurrentDate> rt = cq.from(VwMaxAtdPorRgnlCurrentDate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
