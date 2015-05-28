/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.search;

import br.gov.ce.fortaleza.sesec.bean.Pesquisa;
import br.gov.ce.fortaleza.sesec.controller.AtendimentosController;
import br.gov.ce.fortaleza.sesec.controller.util.JsfUtil;
import br.gov.ce.fortaleza.sesec.controller.util.PaginationHelper;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.text.ParseException;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlInputText;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.Persistence;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;

/**
 *
 * @author Thiago Aderaldo Lessa.
 */
@ManagedBean(name="searchAtendimento")
public class SearchAtendimento {
    
    private Atendimentos current;
    private AtendimentosController currentController;
    private AtendimentosJpaController jpaController = null;
    private DataModel items = null;
    private PaginationHelper pagination;
    private HtmlInputText searchDate1, searchDate2, searchEstatus;
    String data1, data2, estatus, vaidarcerto;
    private Pesquisa pesquisa;
    private List<Atendimentos> listDataModel;
    private int selectedItemIndex;

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public DataModel getItems() {
        return items;
    }

    public void setItems(DataModel items) {
        this.items = items;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public HtmlInputText getSearchDate1() {
        return searchDate1;
    }

    public void setSearchDate1(HtmlInputText searchDate1) {
        this.searchDate1 = searchDate1;
    }

    public HtmlInputText getSearchDate2() {
        return searchDate2;
    }

    public void setSearchDate2(HtmlInputText searchDate2) {
        this.searchDate2 = searchDate2;
    }

    public HtmlInputText getSearchEstatus() {
        return searchEstatus;
    }

    public void setSearchEstatus(HtmlInputText searchEstatus) {
        this.searchEstatus = searchEstatus;
    }

    public PaginationHelper getPagination() {
        return pagination;
    }

    public void setPagination(PaginationHelper pagination) {
        this.pagination = pagination;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public List<Atendimentos> getListDataModel() {
        return listDataModel;
    }

    public void setListDataModel(List<Atendimentos> listDataModel) {
        this.listDataModel = listDataModel;
    }

    public String getVaidarcerto() {
        return vaidarcerto;
    }

    public void setVaidarcerto(String vaidarcerto) {
        this.vaidarcerto = vaidarcerto;
    }
    
    void recreateModel() {
        items = null;
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
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("AtendimentosCreated"));
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

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("AtendimentosUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
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
    
    public PaginationHelper getSearchedAtendimentoPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getJpaController().getAtendimentosCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    //return new ListDataModel(getListDataModel());
                    return new ListDataModel(getAtendimentosByEstatus(estatus));
                }
            };
        }
        return pagination;
    }
    
    public DataModel getSearchedItems() {
        if (items == null){
            items = getSearchedAtendimentoPagination().createPageDataModel();
        }
        return items;
    }
    
    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("iguanaPU"));
        }
        return jpaController;
    }
    
    public List<Atendimentos> getAtendimentosByEstatus(String estatus){
        currentController = new AtendimentosController();
        System.out.println("Estatus em getAtendimentosByEstatus: " + estatus);
       // return getJpaController().findAtendimentosEntities().
                return null;
    }

    public String searchedListAtendimentoByEstatus(String estatus) throws ParseException{
        recreateModel();
        listDataModel = getAtendimentosByEstatus(estatus);
        return "/atendimentos/searchedlist2";
    }
}
