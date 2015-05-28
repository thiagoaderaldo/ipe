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
public class BairrosJpaController implements Serializable {

    public BairrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bairros bairros) {
        if (bairros.getAtendimentosCollection() == null) {
            bairros.setAtendimentosCollection(new ArrayList<Atendimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Atendimentos> attachedAtendimentosCollection = new ArrayList<Atendimentos>();
            for (Atendimentos atendimentosCollectionAtendimentosToAttach : bairros.getAtendimentosCollection()) {
                atendimentosCollectionAtendimentosToAttach = em.getReference(atendimentosCollectionAtendimentosToAttach.getClass(), atendimentosCollectionAtendimentosToAttach.getId());
                attachedAtendimentosCollection.add(atendimentosCollectionAtendimentosToAttach);
            }
            bairros.setAtendimentosCollection(attachedAtendimentosCollection);
            em.persist(bairros);
            for (Atendimentos atendimentosCollectionAtendimentos : bairros.getAtendimentosCollection()) {
                Bairros oldIdBairroOfAtendimentosCollectionAtendimentos = atendimentosCollectionAtendimentos.getIdBairro();
                atendimentosCollectionAtendimentos.setIdBairro(bairros);
                atendimentosCollectionAtendimentos = em.merge(atendimentosCollectionAtendimentos);
                if (oldIdBairroOfAtendimentosCollectionAtendimentos != null) {
                    oldIdBairroOfAtendimentosCollectionAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionAtendimentos);
                    oldIdBairroOfAtendimentosCollectionAtendimentos = em.merge(oldIdBairroOfAtendimentosCollectionAtendimentos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bairros bairros) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bairros persistentBairros = em.find(Bairros.class, bairros.getId());
            Collection<Atendimentos> atendimentosCollectionOld = persistentBairros.getAtendimentosCollection();
            Collection<Atendimentos> atendimentosCollectionNew = bairros.getAtendimentosCollection();
            List<String> illegalOrphanMessages = null;
            for (Atendimentos atendimentosCollectionOldAtendimentos : atendimentosCollectionOld) {
                if (!atendimentosCollectionNew.contains(atendimentosCollectionOldAtendimentos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Atendimentos " + atendimentosCollectionOldAtendimentos + " since its idBairro field is not nullable.");
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
            bairros.setAtendimentosCollection(atendimentosCollectionNew);
            bairros = em.merge(bairros);
            for (Atendimentos atendimentosCollectionNewAtendimentos : atendimentosCollectionNew) {
                if (!atendimentosCollectionOld.contains(atendimentosCollectionNewAtendimentos)) {
                    Bairros oldIdBairroOfAtendimentosCollectionNewAtendimentos = atendimentosCollectionNewAtendimentos.getIdBairro();
                    atendimentosCollectionNewAtendimentos.setIdBairro(bairros);
                    atendimentosCollectionNewAtendimentos = em.merge(atendimentosCollectionNewAtendimentos);
                    if (oldIdBairroOfAtendimentosCollectionNewAtendimentos != null && !oldIdBairroOfAtendimentosCollectionNewAtendimentos.equals(bairros)) {
                        oldIdBairroOfAtendimentosCollectionNewAtendimentos.getAtendimentosCollection().remove(atendimentosCollectionNewAtendimentos);
                        oldIdBairroOfAtendimentosCollectionNewAtendimentos = em.merge(oldIdBairroOfAtendimentosCollectionNewAtendimentos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bairros.getId();
                if (findBairros(id) == null) {
                    throw new NonexistentEntityException("The bairros with id " + id + " no longer exists.");
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
            Bairros bairros;
            try {
                bairros = em.getReference(Bairros.class, id);
                bairros.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bairros with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Atendimentos> atendimentosCollectionOrphanCheck = bairros.getAtendimentosCollection();
            for (Atendimentos atendimentosCollectionOrphanCheckAtendimentos : atendimentosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Bairros (" + bairros + ") cannot be destroyed since the Atendimentos " + atendimentosCollectionOrphanCheckAtendimentos + " in its atendimentosCollection field has a non-nullable idBairro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(bairros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bairros> findBairrosEntities() {
        return findBairrosEntities(true, -1, -1);
    }

    public List<Bairros> findBairrosEntities(int maxResults, int firstResult) {
        return findBairrosEntities(false, maxResults, firstResult);
    }

    private List<Bairros> findBairrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bairros.class));
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

    public Bairros findBairros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bairros.class, id);
        } finally {
            em.close();
        }
    }

    public Bairros findBairrosByNome(String nome) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Bairros.findByNome",
                        Bairros.class).
                        setParameter("nome", nome);
                //return em.find(Bairros.class, nome);
            } catch (Exception ex) {
                Logger.getLogger(BairrosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return (Bairros) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getBairrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bairros> rt = cq.from(Bairros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
