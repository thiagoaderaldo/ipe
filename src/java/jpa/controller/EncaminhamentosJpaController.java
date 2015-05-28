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
import br.gov.ce.fortaleza.sesec.entities.Encaminhamentos;
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
public class EncaminhamentosJpaController implements Serializable {

    public EncaminhamentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Encaminhamentos encaminhamentos) {
        if (encaminhamentos.getAtendimentosCollection() == null) {
            encaminhamentos.setAtendimentosCollection(new ArrayList<Atendimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Atendimentos> attachedAtendimentosCollection = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionAtendimentosToAttach : encaminhamentos.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentosToAttach = em.getReference(atendimentosCollectionAtendimentosToAttach.getClass(), atendimentosCollectionAtendimentosToAttach.getId());
                attachedAtendimentosCollection.add(atendimentosCollectionAtendimentosToAttach);
            }
            encaminhamentos.setAtendimentosCollection(attachedAtendimentosCollection);
            em.persist(encaminhamentos);
            for (Atendimentos atendimentosCollectionAtendimentos : encaminhamentos.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentos.getEncaminhamentosCollection().add(encaminhamentos);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Encaminhamentos encaminhamentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Encaminhamentos persistentEncaminhamentos = em.find(Encaminhamentos.class, encaminhamentos.getId());
            Collection<Atendimentos> atendimentosCollectionOld = persistentEncaminhamentos.getAtendimentosCollection();
            Collection<Atendimentos> atendimentosCollectionNew = encaminhamentos.getAtendimentosCollection();
            Collection<Atendimentos> attachedAtendimentosCollectionNew = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionNewAtendimentosToAttach : atendimentosCollectionNew) {
                atendimentosCollectionNewAtendimentosToAttach = em.getReference(atendimentosCollectionNewAtendimentosToAttach.getClass(), atendimentosCollectionNewAtendimentosToAttach.getId());
                attachedAtendimentosCollectionNew.add(atendimentosCollectionNewAtendimentosToAttach);
            }
            atendimentosCollectionNew = attachedAtendimentosCollectionNew;
            encaminhamentos.setAtendimentosCollection(atendimentosCollectionNew);
            encaminhamentos = em.merge(encaminhamentos);
            for (Atendimentos atendimentosCollectionOldAtendimentos : atendimentosCollectionOld) {
                if (!atendimentosCollectionNew.contains(atendimentosCollectionOldAtendimentos)) {
                    atendimentosCollectionOldAtendimentos.getEncaminhamentosCollection().remove(encaminhamentos);
                    atendimentosCollectionOldAtendimentos = em.merge(atendimentosCollectionOldAtendimentos);
                }
            }
            for (Atendimentos atendimentosCollectionNewAtendimentos : atendimentosCollectionNew) {
                if (!atendimentosCollectionOld.contains(atendimentosCollectionNewAtendimentos)) {
                    atendimentosCollectionNewAtendimentos.getEncaminhamentosCollection().add(encaminhamentos);
                    atendimentosCollectionNewAtendimentos = em.merge(atendimentosCollectionNewAtendimentos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = encaminhamentos.getId();
                if (findEncaminhamentos(id) == null) {
                    throw new NonexistentEntityException("The encaminhamentos with id " + id + " no longer exists.");
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
            Encaminhamentos encaminhamentos;
            try {
                encaminhamentos = em.getReference(Encaminhamentos.class, id);
                encaminhamentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The encaminhamentos with id " + id + " no longer exists.", enfe);
            }
            Collection<Atendimentos> atendimentosCollection = encaminhamentos.getAtendimentosCollection();
            for (Atendimentos atendimentosCollectionAtendimentos : atendimentosCollection) {
                atendimentosCollectionAtendimentos.getEncaminhamentosCollection().remove(encaminhamentos);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
            }
            em.remove(encaminhamentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Encaminhamentos> findEncaminhamentosEntities() {
        return findEncaminhamentosEntities(true, -1, -1);
    }

    public List<Encaminhamentos> findEncaminhamentosEntities(int maxResults, int firstResult) {
        return findEncaminhamentosEntities(false, maxResults, firstResult);
    }

    private List<Encaminhamentos> findEncaminhamentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Encaminhamentos.class));
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

    public Encaminhamentos findEncaminhamentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Encaminhamentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getEncaminhamentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Encaminhamentos> rt = cq.from(Encaminhamentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
