package br.gov.ce.fortaleza.sesec.jsf.controller;

import br.gov.ce.fortaleza.sesec.entities.VwDshbrdIndic;
import br.gov.ce.fortaleza.sesec.jsf.controller.util.JsfUtil;
import br.gov.ce.fortaleza.sesec.jsf.controller.util.PaginationHelper;
import br.gov.ce.fortaleza.sesec.jpa.controller.VwDshbrdIndicJpaController;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.Persistence;

@ManagedBean(name = "vwDshbrdIndicController")
@SessionScoped
public class VwDshbrdIndicController implements Serializable {

    private VwDshbrdIndic current;
    private DataModel items = null;
    private VwDshbrdIndicJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public VwDshbrdIndicController() {
    }

    public VwDshbrdIndic getSelected() {
        if (current == null) {
            current = new VwDshbrdIndic();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VwDshbrdIndicJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VwDshbrdIndicJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getVwDshbrdIndicCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findVwDshbrdIndicEntities(getPageSize(), getPageFirstItem()));
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
        current = (VwDshbrdIndic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new VwDshbrdIndic();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwDshbrdIndicCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (VwDshbrdIndic) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwDshbrdIndicUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (VwDshbrdIndic) getItems().getRowData();
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
            getJpaController().destroy(current.getIdTipologia());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwDshbrdIndicDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getVwDshbrdIndicCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findVwDshbrdIndicEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findVwDshbrdIndicEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findVwDshbrdIndicEntities(), true);
    }

    @FacesConverter(forClass = VwDshbrdIndic.class)
    public static class VwDshbrdIndicControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VwDshbrdIndicController controller = (VwDshbrdIndicController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vwDshbrdIndicController");
            return controller.getJpaController().findVwDshbrdIndic(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VwDshbrdIndic) {
                VwDshbrdIndic o = (VwDshbrdIndic) object;
                return getStringKey(o.getIdTipologia());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + VwDshbrdIndic.class.getName());
            }
        }
    }
    
    public String prepareViewForDashbrd() {
        current = getJpaController().findVwDshbrdIndicEntities().get(0);
        return "null";
    }
}
