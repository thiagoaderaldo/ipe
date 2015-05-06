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
import entities.Atendimentos;
import entities.Bairros;
import entities.Ser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.controller.exceptions.IllegalOrphanException;
import jpa.controller.exceptions.NonexistentEntityException;

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
        if (ser.getAtendimentosCollection() == null) {
            ser.setAtendimentosCollection(new ArrayList<Atendimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Atendimentos> attachedAtendimentosCollection = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionAtendimentosToAttach : ser.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentosToAttach = em.getReference(atendimentosCollectionAtendimentosToAttach.getClass(), atendimentosCollectionAtendimentosToAttach.getId());
                attachedAtendimentosCollection.add(atendimentosCollectionAtendimentosToAttach);
            }
            ser.setAtendimentosCollection(attachedAtendimentosCollection);
            em.persist(ser);
            for (Atendimentos atendimentosCollectionAtendimentos : ser.getAtendimentosCollection()) {
                Ser oldIdSerOfAtendimentosCollectionAtendimentos = atendimentosCollectionAtendimentos.getIdSer();
                atendimentosCollectionAtendimentos.setIdSer(ser);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
                if (oldIdSerOfAtendimentosCollectionAtendimentos != null) {
                    oldIdSerOfAtendimentosCollectionAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionAtendimentos);
                    oldIdSerOfAtendimentosCollectionAtendimentos = em.merge(oldIdSerOfAtendimentosCollectionAtendimentos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ser ser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ser persistentSer = em.find(Ser.class, ser.getId());
            Collection<Atendimentos> atendimentosCollectionOld = persistentSer.getAtendimentosCollection();
            Collection<Atendimentos> atendimentosCollectionNew = ser.getAtendimentosCollection();
            List<String> illegalOrphanMessages = null;
            for (Atendimentos atendimentosCollectionOldAtendimentos : atendimentosCollectionOld) {
                if (!atendimentosCollectionNew.contains(atendimentosCollectionOldAtendimentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Atendimentos " + atendimentosCollectionOldAtendimentos + " since its idSer field is not nullable.");
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
            ser.setAtendimentosCollection(atendimentosCollectionNew);
            ser = em.merge(ser);
            for (Atendimentos atendimentosCollectionNewAtendimentos : atendimentosCollectionNew) {
                if (!atendimentosCollectionOld.contains(atendimentosCollectionNewAtendimentos)) {
                    Ser oldIdSerOfAtendimentosCollectionNewAtendimentos = atendimentosCollectionNewAtendimentos.getIdSer();
                    atendimentosCollectionNewAtendimentos.setIdSer(ser);
                    atendimentosCollectionNewAtendimentos = em.merge(atendimentosCollectionNewAtendimentos);
                    if (oldIdSerOfAtendimentosCollectionNewAtendimentos != null && !oldIdSerOfAtendimentosCollectionNewAtendimentos.equals(ser)) {
                        oldIdSerOfAtendimentosCollectionNewAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionNewAtendimentos);
                        oldIdSerOfAtendimentosCollectionNewAtendimentos = em.merge(oldIdSerOfAtendimentosCollectionNewAtendimentos);
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<Atendimentos> atendimentosCollectionOrphanCheck = ser.getAtendimentosCollection();
            for (Atendimentos atendimentosCollectionOrphanCheckAtendimentos : atendimentosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ser (" + ser + ") cannot be destroyed since the Atendimentos " + atendimentosCollectionOrphanCheckAtendimentos + " in its atendimentosCollection field has a non-nullable idSer field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
