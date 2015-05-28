package controller;

import bean.Pesquisa;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import jpa.controller.AtendimentosJpaController;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;
import jpa.controller.BairrosJpaController;
import jpa.controller.SerJpaController;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import br.gov.ce.fortaleza.sesec.util.DateManager;
import br.gov.ce.fortaleza.sesec.util.FacesUtils;
import br.gov.ce.fortaleza.sesec.util.Protocolo;
import javax.faces.component.html.HtmlOutputText;

@ManagedBean(name = "atendimentosController")
@SessionScoped
public class AtendimentosController implements Serializable {

    private Atendimentos current;
    private DataModel items = null;
    private AtendimentosJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private InputText protocoloInputText;
    Date data1, data2;
    private Pesquisa pesquisa;
    List searchedList;
    private Calendar searchDate1, searchDate2;
    private HtmlInputText hitEstatus, hitResponsavel, hitEquipamento;
    private SelectOneMenu SOMBairros, SOMSer;
    String estatus, responsavel, equipamento, equipe;
    private BairrosJpaController bjc = null;
    private SerJpaController sjc = null;
    private HtmlOutputText hotAgentesEquipe;

    public HtmlOutputText getHotAgentesEquipe() {
        return hotAgentesEquipe;
    }

    public void setHotAgentesEquipe(HtmlOutputText hotAgentesEquipe) {
        this.hotAgentesEquipe = hotAgentesEquipe;
    }

    public SelectOneMenu getSOMSer() {
        return SOMSer;
    }

    public void setSOMSer(SelectOneMenu SOMSer) {
        this.SOMSer = SOMSer;
    }

    public SelectOneMenu getSOMBairros() {
        return SOMBairros;
    }

    public void setSOMBairros(SelectOneMenu SOMBairros) {
        this.SOMBairros = SOMBairros;
    }

    public HtmlInputText getHitEstatus() {
        return hitEstatus;
    }

    public void setHitEstatus(HtmlInputText hitEstatus) {
        this.hitEstatus = hitEstatus;
    }

    public HtmlInputText getHitResponsavel() {
        return hitResponsavel;
    }

    public void setHitResponsavel(HtmlInputText hitResponsavel) {
        this.hitResponsavel = hitResponsavel;
    }

    public HtmlInputText getHitEquipamento() {
        return hitEquipamento;
    }

    public void setHitEquipamento(HtmlInputText hitEquipamento) {
        this.hitEquipamento = hitEquipamento;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public Date getData1() {
        return data1;
    }

    public void setData1(Date data1) {
        this.data1 = data1;
    }

    public Date getData2() {
        return data2;
    }

    public void setData2(Date data2) {
        this.data2 = data2;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public List getSearchedList() {
        return searchedList;
    }

    public void setSearchedList(List searchedList) {
        this.searchedList = searchedList;
    }

    public Calendar getSearchDate1() {
        return searchDate1;
    }

    public void setSearchDate1(Calendar searchDate1) {
        this.searchDate1 = searchDate1;
    }

    public Calendar getSearchDate2() {
        return searchDate2;
    }

    public void setSearchDate2(Calendar searchDate2) {
        this.searchDate2 = searchDate2;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public AtendimentosController() {
        this.pesquisa = new Pesquisa();
        this.pesquisa.setOpcao(1);
    }

    public Atendimentos getSelected() {
        if (current == null) {
            current = new Atendimentos();
            selectedItemIndex = -1;
        }
        return current;
    }

    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findAtendimentosEntities(getPageSize(), getPageFirstItem()));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Atendimentos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Atendimentos();
        selectedItemIndex = -1;
        return "/admin/atendimentos/Create";
    }

    public String create() {
        try {
            createProtocolo();
            bjc = new BairrosJpaController(Persistence.createEntityManagerFactory("ipePU"));
            current.setIdBairro(bjc.findBairrosByNome(getSOMBairros().getValue().toString()));
            sjc = new SerJpaController(Persistence.createEntityManagerFactory("ipePU"));
            current.setIdSer(sjc.findSerByNome(getSOMSer().getValue().toString()));
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("AtendimentosCreated"));
            JsfUtil.addWarnMessage("Protocolo gerado: " + current.getProtocolo());
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Atendimentos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String prepareEditEquipe() {
        current = (Atendimentos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "/admin/equipe/Create";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("AtendimentosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured") + " Descrição do erro: " + e.getMessage());
            return null;
        }
    }

    public String destroy() {
        current = (Atendimentos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getJpaController().destroy(current.getId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("AtendimentosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
        }
    }



    private void updateCurrentItem() {
        int count = getJpaController().getAtendimentosCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findAtendimentosEntities(1, selectedItemIndex).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(getJpaController().findAtendimentosEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findAtendimentosEntities(), true);
    }

    @FacesConverter(forClass = Atendimentos.class)
    public static class AtendimentosControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AtendimentosController controller = (AtendimentosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "atendimentosController");
            return controller.getJpaController().findAtendimentos(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Atendimentos) {
                Atendimentos o = (Atendimentos) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Atendimentos.class.getName());
            }
        }
    }

    public InputText getProtocoloInputText() {
        return protocoloInputText;
    }

    public void setProtocoloInputText(InputText protocoloInputText) {
        this.protocoloInputText = protocoloInputText;
        Protocolo protocolo = new Protocolo();
        this.protocoloInputText.setValue(protocolo.horarioComoProtocolo().toString());
    }

    public void createProtocolo() {
        Protocolo protocolo = new Protocolo();
        current.setProtocolo(protocolo.horarioComoProtocolo().toString());
    }

    /**
     * *************************************************************************
     * Os métodos abaixo permitem a busca de atendimento passando como
     * parâmetros duas DATAS.
     * *************************************************************************
     */
    private List<Atendimentos> getAtendimentosBetweenDates(String date1, String date2) {
        return getJpaController().findAtendimentoBetweenDates(date1, date2);
    }

    public String searchedListAtendimentoBetweenDates() throws ParseException {
        if (!DateManager.verificarValidadeDeData(DateManager.DateUtilToString("dd/MM/yyyy", data1))
                || !DateManager.verificarValidadeDeData(DateManager.DateUtilToString("dd/MM/yyyy", data2))) {
            FacesUtils.mensErro("errorMessages",
                    "Uma das datas digitadas não é uma data válida.");
            return null;
        } else {
            recreateModel();
            //os campos de buscas são limpos para evitar que o usuário mantenha
            //o valor preenchido na próxima vez que acessar a página.
            this.searchDate1 = new Calendar();
            this.searchDate2 = new Calendar();
            data1 = new Date();
            data2 = new Date();
            //redireciona para a página de resultados.
            return "/admin/atendimentos/searchedlist.xhtml?faces-redirect=true";
        }

    }

    public PaginationHelper getSearchedAtendimentoPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(searchedList);
                }
            };
        }
        return pagination;
    }

    public DataModel getSearchedItems() {
        if (items == null) {
            items = getSearchedAtendimentoPagination().createPageDataModel();
        }
        return items;
    }
    //FIM DO BLOCO

    //FIM DO BLOCO
    /**
     * *************************************************************************
     * Os métodos abaixo permitem a busca pelo ESTATUS do atendimento.
     * *************************************************************************
     */
    public List<Atendimentos> getAtendimentosByEstatus(String estatus) {
        return getJpaController().findAtendimentoByEstatus(estatus);
    }

    public String searchedListAtendimentoByEstatus(String estatus) throws ParseException {
        recreateModel();
        //return "/atendimentos/searchedatendimentosbyestatus.xhtml?faces-redirect=true";
        return "/admin/atendimentos/searchedlist.xhtml?faces-redirect=true";
    }

    public PaginationHelper getSearchedAtendimentoByEstatusPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getAtendimentosByEstatus(estatus));
                }
            };
        }
        return pagination;
    }

    public DataModel getSearchedItemsByEstatus() {
        if (items == null) {
            items = getSearchedAtendimentoByEstatusPagination().createPageDataModel();
        }
        return items;
    }

    //FIM DO BLOCO
    /**
     * *************************************************************************
     * Os métodos abaixo permitem a busca pelo RESPONSÁVEL do atendimento.
     * *************************************************************************
     */
    public List<Atendimentos> getAtendimentosByResponsavel(String responsavel) {
        return getJpaController().findAtendimentoByResponsavel(responsavel);
    }

    public String searchedListAtendimentoByResponsavel() throws ParseException {
        recreateModel();
        this.hitResponsavel.setValue("");
        //return "/atendimentos/searchedatendimentosbyestatus.xhtml?faces-redirect=true";
        return "/admin/atendimentos/searchedlist.xhtml?faces-redirect=true";
    }

    public PaginationHelper getSearchedAtendimentoByResponsavelPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getAtendimentosByResponsavel(responsavel));
                }
            };
        }
        return pagination;
    }

    //FIM DO BLOCO
    /**
     * *************************************************************************
     * Os métodos abaixo permitem a busca pelo EQUIPAMENTO do atendimento.
     * *************************************************************************
     */
    public List<Atendimentos> getAtendimentosByEquipamento(String equipamento) {
        return getJpaController().findAtendimentoByEquipamento(equipamento);
    }

    public String searchedListAtendimentoByEquipamento() throws ParseException {
        recreateModel();
        this.hitEquipamento.setValue("");
        //return "/atendimentos/searchedatendimentosbyestatus.xhtml?faces-redirect=true";
        return "/admin/atendimentos/searchedlist.xhtml?faces-redirect=true";
    }

    public PaginationHelper getSearchedAtendimentoByEquipamentoPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getAtendimentosByEquipamento(equipamento));
                }
            };
        }
        return pagination;
    }

    //FIM DO BLOCO
    /**
     * *************************************************************************
     * Realiza o controle das buscas.
     * *************************************************************************
     */
    public String searchesController() {
        switch (pesquisa.getOpcao()) {
            case 1: {
                try {
                    pesquisa = new Pesquisa();
                    searchedList = getAtendimentosBetweenDates(DateManager.DateUtilToString("dd/MM/yyyy", data1),
                            DateManager.DateUtilToString("dd/MM/yyyy", data2));
                    return searchedListAtendimentoBetweenDates();
                } catch (ParseException ex) {
                    Logger.getLogger(AtendimentosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            case 2: {
                try {
                    pesquisa = new Pesquisa();
                    searchedList = getAtendimentosByEstatus(estatus);
                    return searchedListAtendimentoByEstatus(estatus);

                } catch (ParseException ex) {
                    Logger.getLogger(AtendimentosController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            case 3: {
                try {
                    pesquisa = new Pesquisa();
                    searchedList = getAtendimentosByResponsavel(responsavel);
                    return searchedListAtendimentoByResponsavel();

                } catch (ParseException ex) {
                    Logger.getLogger(AtendimentosController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            case 4: {
                try {
                    pesquisa = new Pesquisa();
                    searchedList = getAtendimentosByEquipamento(equipamento);
                    return searchedListAtendimentoByEquipamento();

                } catch (ParseException ex) {
                    Logger.getLogger(AtendimentosController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

        return null;
    }

    /**
     * *************************************************************************
     * Os métodos abaixo permitem a busca pelo EQUIPE do atendimento.
     * *************************************************************************
     */
    public List<Atendimentos> getAtendimentoByEquipe(String equipe) {

        return getJpaController().findAtendimentoByEquipe(equipe);
    }
    
    public List<Atendimentos> getAtendimentosWhereEquipeIsNull() {

        return getJpaController().findAtendimentosWhereEquipeIsNull();
    }

    public String searchedListAtendimentoByEquipe() {
        recreateModel();
        //return "/atendimentos/searchedatendimentosbyestatus.xhtml?faces-redirect=true";
        return "/admin/atendimentos/list_atendimentos_sem_equipe.xhtml?faces-redirect=true";
    }

    public PaginationHelper getSearchedAtendimentoByEquipePagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getAtendimentoByEquipe(equipe));
                }
            };
        }
        return pagination;
    }

    public DataModel getSearchedItemsByEquipe() {
        if (items == null) {
            items = getSearchedAtendimentoByEquipePagination().createPageDataModel();
        }
        return items;
    }
    
        public String equipeVoid() {
        searchedList = getAtendimentosWhereEquipeIsNull();

        return searchedListAtendimentoByEquipe();
    }
}
