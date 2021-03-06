/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.jpa.controller;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.gov.ce.fortaleza.sesec.entities.Estatus;
import br.gov.ce.fortaleza.sesec.entities.Tipologias;
import br.gov.ce.fortaleza.sesec.entities.Ser;
import br.gov.ce.fortaleza.sesec.entities.Bairros;
import br.gov.ce.fortaleza.sesec.entities.Encaminhamentos;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import br.gov.ce.fortaleza.sesec.jpa.controller.exceptions.NonexistentEntityException;
import br.gov.ce.fortaleza.sesec.util.DateManager;

/**
 *
 * @author thiago
 */
public class AtendimentosJpaController implements Serializable {

    public AtendimentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Atendimentos atendimentos) {
        if (atendimentos.getEncaminhamentosCollection() == null) {
            atendimentos.setEncaminhamentosCollection(new ArrayList<Encaminhamentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estatus idEstatus = atendimentos.getIdEstatus();
            if (idEstatus != null) {
                idEstatus = em.getReference(idEstatus.getClass(), idEstatus.getId());
                atendimentos.setIdEstatus(idEstatus);
            }
            Tipologias idTipologia = atendimentos.getIdTipologia();
            if (idTipologia != null) {
                idTipologia = em.getReference(idTipologia.getClass(), idTipologia.getId());
                atendimentos.setIdTipologia(idTipologia);
            }
            Bairros idBairro = atendimentos.getIdBairro();
            if (idBairro != null) {
                idBairro = em.getReference(idBairro.getClass(), idBairro.getId());
                atendimentos.setIdBairro(idBairro);
            }
            Collection<Encaminhamentos> attachedEncaminhamentosCollection = new ArrayList<Encaminhamentos>();
            for (Encaminhamentos encaminhamentosCollectionEncaminhamentosToAttach : atendimentos.getEncaminhamentosCollection()) {
                encaminhamentosCollectionEncaminhamentosToAttach = em.getReference(encaminhamentosCollectionEncaminhamentosToAttach.getClass(), encaminhamentosCollectionEncaminhamentosToAttach.getId());
                attachedEncaminhamentosCollection.add(encaminhamentosCollectionEncaminhamentosToAttach);
            }
            atendimentos.setEncaminhamentosCollection(attachedEncaminhamentosCollection);
            em.persist(atendimentos);
            if (idEstatus != null) {
                idEstatus.getAtendimentosCollection().add(atendimentos);
                idEstatus = em.merge(idEstatus);
            }
            if (idTipologia != null) {
                idTipologia.getAtendimentosCollection().add(atendimentos);
                idTipologia = em.merge(idTipologia);
            }
            if (idBairro != null) {
                idBairro.getAtendimentosCollection().add(atendimentos);
                idBairro = em.merge(idBairro);
            }
            for (Encaminhamentos encaminhamentosCollectionEncaminhamentos : atendimentos.getEncaminhamentosCollection()) {
                encaminhamentosCollectionEncaminhamentos.getAtendimentosCollection().add(atendimentos);
                encaminhamentosCollectionEncaminhamentos = em.merge(encaminhamentosCollectionEncaminhamentos);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Atendimentos atendimentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atendimentos persistentAtendimentos = em.find(Atendimentos.class, atendimentos.getId());
            Estatus idEstatusOld = persistentAtendimentos.getIdEstatus();
            Estatus idEstatusNew = atendimentos.getIdEstatus();
            Tipologias idTipologiaOld = persistentAtendimentos.getIdTipologia();
            Tipologias idTipologiaNew = atendimentos.getIdTipologia();
            Bairros idBairroOld = persistentAtendimentos.getIdBairro();
            Bairros idBairroNew = atendimentos.getIdBairro();
            Collection<Encaminhamentos> encaminhamentosCollectionOld = persistentAtendimentos.getEncaminhamentosCollection();
            Collection<Encaminhamentos> encaminhamentosCollectionNew = atendimentos.getEncaminhamentosCollection();
            if (idEstatusNew != null) {
                idEstatusNew = em.getReference(idEstatusNew.getClass(), idEstatusNew.getId());
                atendimentos.setIdEstatus(idEstatusNew);
            }
            if (idTipologiaNew != null) {
                idTipologiaNew = em.getReference(idTipologiaNew.getClass(), idTipologiaNew.getId());
                atendimentos.setIdTipologia(idTipologiaNew);
            }
            if (idBairroNew != null) {
                idBairroNew = em.getReference(idBairroNew.getClass(), idBairroNew.getId());
                atendimentos.setIdBairro(idBairroNew);
            }
            Collection<Encaminhamentos> attachedEncaminhamentosCollectionNew = new ArrayList<Encaminhamentos>();
            for (Encaminhamentos encaminhamentosCollectionNewEncaminhamentosToAttach : encaminhamentosCollectionNew) {
                encaminhamentosCollectionNewEncaminhamentosToAttach = em.getReference(encaminhamentosCollectionNewEncaminhamentosToAttach.getClass(), encaminhamentosCollectionNewEncaminhamentosToAttach.getId());
                attachedEncaminhamentosCollectionNew.add(encaminhamentosCollectionNewEncaminhamentosToAttach);
            }
            encaminhamentosCollectionNew = attachedEncaminhamentosCollectionNew;
            atendimentos.setEncaminhamentosCollection(encaminhamentosCollectionNew);
            atendimentos = em.merge(atendimentos);
            if (idEstatusOld != null && !idEstatusOld.equals(idEstatusNew)) {
                idEstatusOld.getAtendimentosCollection().remove(atendimentos);
                idEstatusOld = em.merge(idEstatusOld);
            }
            if (idEstatusNew != null && !idEstatusNew.equals(idEstatusOld)) {
                idEstatusNew.getAtendimentosCollection().add(atendimentos);
                idEstatusNew = em.merge(idEstatusNew);
            }
            if (idTipologiaOld != null && !idTipologiaOld.equals(idTipologiaNew)) {
                idTipologiaOld.getAtendimentosCollection().remove(atendimentos);
                idTipologiaOld = em.merge(idTipologiaOld);
            }
            if (idTipologiaNew != null && !idTipologiaNew.equals(idTipologiaOld)) {
                idTipologiaNew.getAtendimentosCollection().add(atendimentos);
                idTipologiaNew = em.merge(idTipologiaNew);
            }
            if (idBairroOld != null && !idBairroOld.equals(idBairroNew)) {
                idBairroOld.getAtendimentosCollection().remove(atendimentos);
                idBairroOld = em.merge(idBairroOld);
            }
            if (idBairroNew != null && !idBairroNew.equals(idBairroOld)) {
                idBairroNew.getAtendimentosCollection().add(atendimentos);
                idBairroNew = em.merge(idBairroNew);
            }
            for (Encaminhamentos encaminhamentosCollectionOldEncaminhamentos : encaminhamentosCollectionOld) {
                if (!encaminhamentosCollectionNew.contains(encaminhamentosCollectionOldEncaminhamentos)) {
                    encaminhamentosCollectionOldEncaminhamentos.getAtendimentosCollection().remove(atendimentos);
                    encaminhamentosCollectionOldEncaminhamentos = em.merge(encaminhamentosCollectionOldEncaminhamentos);
                }
            }
            for (Encaminhamentos encaminhamentosCollectionNewEncaminhamentos : encaminhamentosCollectionNew) {
                if (!encaminhamentosCollectionOld.contains(encaminhamentosCollectionNewEncaminhamentos)) {
                    encaminhamentosCollectionNewEncaminhamentos.getAtendimentosCollection().add(atendimentos);
                    encaminhamentosCollectionNewEncaminhamentos = em.merge(encaminhamentosCollectionNewEncaminhamentos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = atendimentos.getId();
                if (findAtendimentos(id) == null) {
                    throw new NonexistentEntityException("The atendimentos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atendimentos atendimentos;
            try {
                atendimentos = em.getReference(Atendimentos.class, id);
                atendimentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atendimentos with id " + id + " no longer exists.", enfe);
            }
            Estatus idEstatus = atendimentos.getIdEstatus();
            if (idEstatus != null) {
                idEstatus.getAtendimentosCollection().remove(atendimentos);
                idEstatus = em.merge(idEstatus);
            }
            Tipologias idTipologia = atendimentos.getIdTipologia();
            if (idTipologia != null) {
                idTipologia.getAtendimentosCollection().remove(atendimentos);
                idTipologia = em.merge(idTipologia);
            }
            Bairros idBairro = atendimentos.getIdBairro();
            if (idBairro != null) {
                idBairro.getAtendimentosCollection().remove(atendimentos);
                idBairro = em.merge(idBairro);
            }
            Collection<Encaminhamentos> encaminhamentosCollection = atendimentos.getEncaminhamentosCollection();
            for (Encaminhamentos encaminhamentosCollectionEncaminhamentos : encaminhamentosCollection) {
                encaminhamentosCollectionEncaminhamentos.getAtendimentosCollection().remove(atendimentos);
                encaminhamentosCollectionEncaminhamentos = em.merge(encaminhamentosCollectionEncaminhamentos);
            }
            em.remove(atendimentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Atendimentos> findAtendimentosEntities() {
        return findAtendimentosEntities(true, -1, -1);
    }

    public List<Atendimentos> findAtendimentosEntities(int maxResults, int firstResult) {
        return findAtendimentosEntities(false, maxResults, firstResult);
    }

    private List<Atendimentos> findAtendimentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atendimentos.class));
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

    public Atendimentos findAtendimentos(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atendimentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtendimentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atendimentos> rt = cq.from(Atendimentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * *************************************************************************
     * Os método abaixo permite a busca do atendimento passando como parametro
     * duas datas.
     **************************************************************************
     */
    public List<Atendimentos> findAtendimentoBetweenDates(String data1, String data2) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findByBetweenDate1AndDate2",
                        Atendimentos.class).
                        setParameter("data1", DateManager.StringToDateUtil("dd/MM/yyyy", data1)).
                        setParameter("data2", DateManager.StringToDateUtil("dd/MM/yyyy", data2));
            } catch (ParseException ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
     /**
     * *************************************************************************
     * Os método abaixo permitem a busca pelo ESTATUS do atendimento.
     **************************************************************************
     */
    public List<Atendimentos> findAtendimentoByEstatus(String estatus) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                Estatus e = new Estatus(Integer.parseInt(estatus));
                q = em.createNamedQuery("Atendimentos.findAtendimentosByEstatus",
                        Atendimentos.class).
                        setParameter("idEstatus", e);

            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * *************************************************************************
     * Os método abaixo permitem a busca pelo RESPONSAVEL do atendimento.
     **************************************************************************
     */
    public List<Atendimentos> findAtendimentoByResponsavel(String responsavel) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findAtendimentoLikeResponsavel",
                        Atendimentos.class).
                        setParameter("responsavel", "%" + responsavel + "%");

            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }

    /**
     * *************************************************************************
     * Os método abaixo permitem a busca pelo EQUIPAMENTO do atendimento.
     **************************************************************************
     */
    public List<Atendimentos> findAtendimentoByEquipamento(String equipamento) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findAtendimentoLikeEquipamento",
                        Atendimentos.class).
                        setParameter("nomeDoDispositivo", "%" + equipamento + "%");

            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }
    public List<Atendimentos> findAtendimentoByEquipe(String equipe) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findByEquipe",
                        Atendimentos.class).
                        setParameter("equipe", equipe);
            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }
    public List<Atendimentos> findAtendimentosWhereEquipeIsNull() {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findAtendimentosWhereEquipeIsNull",
                        Atendimentos.class);
                System.out.println("Query em findAtendimentosWhereEquipeIsNull" + q.toString());
            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }

    public List<Atendimentos> findAtendimentoByProtocolo(String protocolo) {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findByProtocolo",
                        Atendimentos.class).
                        setParameter("protocolo", protocolo);                
            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<Atendimentos> list = (List <Atendimentos>) q.getResultList();
            System.out.println("ResultList: " + list.get(0).getDescInicial());
            return q.getResultList();

        } finally {
            em.close();
        }
    }
    
    public List<Atendimentos> findByCurrentDate() {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findByCurrentDate",
                        Atendimentos.class);

            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }
    
    public List<Atendimentos> findByCurrentMonth() {
        EntityManager em = getEntityManager();
        try {
            Query q = null;
            try {
                q = em.createNamedQuery("Atendimentos.findByCurrentMonth",
                        Atendimentos.class);

            } catch (Exception ex) {
                Logger.getLogger(AtendimentosJpaController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return q.getResultList();

        } finally {
            em.close();
        }
    }
    
}
