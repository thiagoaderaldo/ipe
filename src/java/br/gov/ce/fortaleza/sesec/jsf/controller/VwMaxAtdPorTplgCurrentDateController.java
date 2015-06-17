package br.gov.ce.fortaleza.sesec.jsf.controller;

import br.gov.ce.fortaleza.sesec.entities.VwMaxAtdPorTplgCurrentDate;
import br.gov.ce.fortaleza.sesec.jsf.controller.util.JsfUtil;
import br.gov.ce.fortaleza.sesec.jsf.controller.util.PaginationHelper;
import br.gov.ce.fortaleza.sesec.jpa.controller.VwMaxAtdPorTplgCurrentDateJpaController;

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

@ManagedBean(name = "vwMaxAtdPorTplgCurrentDateController")
@SessionScoped
public class VwMaxAtdPorTplgCurrentDateController implements Serializable {

    private VwMaxAtdPorTplgCurrentDate current;
    private DataModel items = null;
    private VwMaxAtdPorTplgCurrentDateJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public VwMaxAtdPorTplgCurrentDateController() {
    }

    public VwMaxAtdPorTplgCurrentDate getSelected() {
        if (current == null) {
            current = new VwMaxAtdPorTplgCurrentDate();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VwMaxAtdPorTplgCurrentDateJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VwMaxAtdPorTplgCurrentDateJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getVwMaxAtdPorTplgCurrentDateCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findVwMaxAtdPorTplgCurrentDateEntities(getPageSize(), getPageFirstItem()));
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
        current = (VwMaxAtdPorTplgCurrentDate) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new VwMaxAtdPorTplgCurrentDate();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwMaxAtdPorTplgCurrentDateCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (VwMaxAtdPorTplgCurrentDate) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwMaxAtdPorTplgCurrentDateUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (VwMaxAtdPorTplgCurrentDate) getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwMaxAtdPorTplgCurrentDateDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getVwMaxAtdPorTplgCurrentDateCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findVwMaxAtdPorTplgCurrentDateEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findVwMaxAtdPorTplgCurrentDateEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findVwMaxAtdPorTplgCurrentDateEntities(), true);
    }

    @FacesConverter(forClass = VwMaxAtdPorTplgCurrentDate.class)
    public static class VwMaxAtdPorTplgCurrentDateControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VwMaxAtdPorTplgCurrentDateController controller = (VwMaxAtdPorTplgCurrentDateController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vwMaxAtdPorTplgCurrentDateController");
            return controller.getJpaController().findVwMaxAtdPorTplgCurrentDate(getKey(value));
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
            if (object instanceof VwMaxAtdPorTplgCurrentDate) {
                VwMaxAtdPorTplgCurrentDate o = (VwMaxAtdPorTplgCurrentDate) object;
                return getStringKey(o.getIdTipologia());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + VwMaxAtdPorTplgCurrentDate.class.getName());
            }
        }
    }
}
