/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.VwDshbrdResumo;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class VwDshbrdResumoJpaController implements Serializable {

    public VwDshbrdResumoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(VwDshbrdResumo vwDshbrdResumo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(vwDshbrdResumo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVwDshbrdResumo(vwDshbrdResumo.getQtdOcrAbertas()) != null) {
                throw new PreexistingEntityException("VwDshbrdResumo " + vwDshbrdResumo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(VwDshbrdResumo vwDshbrdResumo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            vwDshbrdResumo = em.merge(vwDshbrdResumo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigInteger id = vwDshbrdResumo.getQtdOcrAbertas();
                if (findVwDshbrdResumo(id) == null) {
                    throw new NonexistentEntityException("The vwDshbrdResumo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigInteger id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            VwDshbrdResumo vwDshbrdResumo;
            try {
                vwDshbrdResumo = em.getReference(VwDshbrdResumo.class, id);
                vwDshbrdResumo.getQtdOcrAbertas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vwDshbrdResumo with id " + id + " no longer exists.", enfe);
            }
            em.remove(vwDshbrdResumo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<VwDshbrdResumo> findVwDshbrdResumoEntities() {
        return findVwDshbrdResumoEntities(true, -1, -1);
    }

    public List<VwDshbrdResumo> findVwDshbrdResumoEntities(int maxResults, int firstResult) {
        return findVwDshbrdResumoEntities(false, maxResults, firstResult);
    }

    private List<VwDshbrdResumo> findVwDshbrdResumoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(VwDshbrdResumo.class));
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

    public VwDshbrdResumo findVwDshbrdResumo(BigInteger id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(VwDshbrdResumo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVwDshbrdResumoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<VwDshbrdResumo> rt = cq.from(VwDshbrdResumo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public VwDshbrdResumo findVwDshbrdResumo() {
       EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("VwDshbrdResumo.findAll",
                        VwDshbrdResumo.class);
            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return (VwDshbrdResumo)q.getSingleResult();
        } finally {
            em.close();
        }
     }
    
}
