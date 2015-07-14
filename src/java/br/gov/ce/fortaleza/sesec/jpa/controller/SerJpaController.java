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
import br.gov.ce.fortaleza.sesec.entities.Bairros;
import br.gov.ce.fortaleza.sesec.entities.Ser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.IllegalOrphanException;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author thiago
 */
public class SerJpaController implements Serializable {

    public SerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ser ser) {
        if (ser.getBairrosCollection() == null) {
            ser.setBairrosCollection(new ArrayList<Bairros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Bairros> attachedBairrosCollection = new ArrayList<Bairros>();
            for (Bairros bairrosCollectionBairrosToAttach : ser.getBairrosCollection()) {
                bairrosCollectionBairrosToAttach = em.getReference(bairrosCollectionBairrosToAttach.getClass(), bairrosCollectionBairrosToAttach.getId());
                attachedBairrosCollection.add(bairrosCollectionBairrosToAttach);
            }
            ser.setBairrosCollection(attachedBairrosCollection);
            em.persist(ser);
            for (Bairros bairrosCollectionBairros : ser.getBairrosCollection()) {
                Ser oldIdSerOfBairrosCollectionBairros = bairrosCollectionBairros.getIdSer();
                bairrosCollectionBairros.setIdSer(ser);
                bairrosCollectionBairros = em.merge(bairrosCollectionBairros);
                if (oldIdSerOfBairrosCollectionBairros != null) {
                    oldIdSerOfBairrosCollectionBairros.getBairrosCollection().remove(bairrosCollectionBairros);
                    oldIdSerOfBairrosCollectionBairros = em.merge(oldIdSerOfBairrosCollectionBairros);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ser ser) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ser persistentSer = em.find(Ser.class, ser.getId());
            Collection<Bairros> bairrosCollectionOld = persistentSer.getBairrosCollection();
            Collection<Bairros> bairrosCollectionNew = ser.getBairrosCollection();
            Collection<Bairros> attachedBairrosCollectionNew = new ArrayList<Bairros>();
            for (Bairros bairrosCollectionNewBairrosToAttach : bairrosCollectionNew) {
                bairrosCollectionNewBairrosToAttach = em.getReference(bairrosCollectionNewBairrosToAttach.getClass(), bairrosCollectionNewBairrosToAttach.getId());
                attachedBairrosCollectionNew.add(bairrosCollectionNewBairrosToAttach);
            }
            bairrosCollectionNew = attachedBairrosCollectionNew;
            ser.setBairrosCollection(bairrosCollectionNew);
            ser = em.merge(ser);
            for (Bairros bairrosCollectionOldBairros : bairrosCollectionOld) {
                if (!bairrosCollectionNew.contains(bairrosCollectionOldBairros)) {
                    bairrosCollectionOldBairros.setIdSer(null);
                    bairrosCollectionOldBairros = em.merge(bairrosCollectionOldBairros);
                }
            }
            for (Bairros bairrosCollectionNewBairros : bairrosCollectionNew) {
                if (!bairrosCollectionOld.contains(bairrosCollectionNewBairros)) {
                    Ser oldIdSerOfBairrosCollectionNewBairros = bairrosCollectionNewBairros.getIdSer();
                    bairrosCollectionNewBairros.setIdSer(ser);
                    bairrosCollectionNewBairros = em.merge(bairrosCollectionNewBairros);
                    if (oldIdSerOfBairrosCollectionNewBairros != null && !oldIdSerOfBairrosCollectionNewBairros.equals(ser)) {
                        oldIdSerOfBairrosCollectionNewBairros.getBairrosCollection().remove(bairrosCollectionNewBairros);
                        oldIdSerOfBairrosCollectionNewBairros = em.merge(oldIdSerOfBairrosCollectionNewBairros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ser.getId();
                if (findSer(id) == null) {
                    throw new NonexistentEntityException("The ser with id " + id + " no longer exists.");
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
            Ser ser;
            try {
                ser = em.getReference(Ser.class, id);
                ser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ser with id " + id + " no longer exists.", enfe);
            }
            Collection<Bairros> bairrosCollection = ser.getBairrosCollection();
            for (Bairros bairrosCollectionBairros : bairrosCollection) {
                bairrosCollectionBairros.setIdSer(null);
                bairrosCollectionBairros = em.merge(bairrosCollectionBairros);
            }
            em.remove(ser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ser> findSerEntities() {
        return findSerEntities(true, -1, -1);
    }

    public List<Ser> findSerEntities(int maxResults, int firstResult) {
        return findSerEntities(false, maxResults, firstResult);
    }

    private List<Ser> findSerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ser.class));
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

    public Ser findSer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ser.class, id);
        } finally {
            em.close();
        }
    }

    public Ser findSerByNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Ser.findByNome",
                        Ser.class).
                        setParameter("nome", nome);
            } catch (Exception ex) {
                Logger.getLogger(SerJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return (Ser) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getSerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ser> rt = cq.from(Ser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
