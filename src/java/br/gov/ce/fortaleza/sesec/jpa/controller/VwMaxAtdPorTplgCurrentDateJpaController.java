/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.VwMaxAtdPorTplgCurrentDate;
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
public class VwMaxAtdPorTplgCurrentDateJpaController implements Serializable {

    public VwMaxAtdPorTplgCurrentDateJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VwMaxAtdPorTplgCurrentDate vwMaxAtdPorTplgCurrentDate) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vwMaxAtdPorTplgCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVwMaxAtdPorTplgCurrentDate(vwMaxAtdPorTplgCurrentDate.getIdTipologia()) != null) {
                throw new PreexistingEntityException("VwMaxAtdPorTplgCurrentDate " + vwMaxAtdPorTplgCurrentDate + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwMaxAtdPorTplgCurrentDate vwMaxAtdPorTplgCurrentDate) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vwMaxAtdPorTplgCurrentDate = em.merge(vwMaxAtdPorTplgCurrentDate);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vwMaxAtdPorTplgCurrentDate.getIdTipologia();
                if (findVwMaxAtdPorTplgCurrentDate(id) == null) {
                    throw new NonexistentEntityException("The vwMaxAtdPorTplgCurrentDate with id " + id + " no longer exists.");
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
            VwMaxAtdPorTplgCurrentDate vwMaxAtdPorTplgCurrentDate;
            try {
                vwMaxAtdPorTplgCurrentDate = em.getReference(VwMaxAtdPorTplgCurrentDate.class, id);
                vwMaxAtdPorTplgCurrentDate.getIdTipologia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwMaxAtdPorTplgCurrentDate with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwMaxAtdPorTplgCurrentDate);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VwMaxAtdPorTplgCurrentDate> findVwMaxAtdPorTplgCurrentDateEntities() {
        return findVwMaxAtdPorTplgCurrentDateEntities(true, -1, -1);
    }

    public List<VwMaxAtdPorTplgCurrentDate> findVwMaxAtdPorTplgCurrentDateEntities(int maxResults, int firstResult) {
        return findVwMaxAtdPorTplgCurrentDateEntities(false, maxResults, firstResult);
    }

    private List<VwMaxAtdPorTplgCurrentDate> findVwMaxAtdPorTplgCurrentDateEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwMaxAtdPorTplgCurrentDate.class));
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

    public VwMaxAtdPorTplgCurrentDate findVwMaxAtdPorTplgCurrentDate(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwMaxAtdPorTplgCurrentDate.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwMaxAtdPorTplgCurrentDateCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwMaxAtdPorTplgCurrentDate> rt = cq.from(VwMaxAtdPorTplgCurrentDate.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
