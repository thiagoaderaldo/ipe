package br.gov.ce.fortaleza.sesec.controller;

import br.gov.ce.fortaleza.sesec.entities.VwQtdTotalAtdCurrentDate;
import br.gov.ce.fortaleza.sesec.controller.util.JsfUtil;
import br.gov.ce.fortaleza.sesec.controller.util.PaginationHelper;
import br.gov.ce.fortaleza.sesec.jpa.controller.VwQtdTotalAtdCurrentDateJpaController;

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

@ManagedBean(name = "vwQtdTotalAtdCurrentDateController")
@SessionScoped
public class VwQtdTotalAtdCurrentDateController implements Serializable {

    private VwQtdTotalAtdCurrentDate current;
    private DataModel items = null;
    private VwQtdTotalAtdCurrentDateJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public VwQtdTotalAtdCurrentDateController() {
    }

    public VwQtdTotalAtdCurrentDate getSelected() {
        if (current == null) {
            current = new VwQtdTotalAtdCurrentDate();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VwQtdTotalAtdCurrentDateJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VwQtdTotalAtdCurrentDateJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getVwQtdTotalAtdCurrentDateCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findVwQtdTotalAtdCurrentDateEntities(getPageSize(), getPageFirstItem()));
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
        current = (VwQtdTotalAtdCurrentDate) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new VwQtdTotalAtdCurrentDate();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwQtdTotalAtdCurrentDateCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (VwQtdTotalAtdCurrentDate) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwQtdTotalAtdCurrentDateUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (VwQtdTotalAtdCurrentDate) getItems().getRowData();
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
            getJpaController().destroy(current.getQtdAtd());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle-views").getString("VwQtdTotalAtdCurrentDateDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle-views").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getVwQtdTotalAtdCurrentDateCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findVwQtdTotalAtdCurrentDateEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findVwQtdTotalAtdCurrentDateEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findVwQtdTotalAtdCurrentDateEntities(), true);
    }

    @FacesConverter(forClass = VwQtdTotalAtdCurrentDate.class)
    public static class VwQtdTotalAtdCurrentDateControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VwQtdTotalAtdCurrentDateController controller = (VwQtdTotalAtdCurrentDateController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "vwQtdTotalAtdCurrentDateController");
            return controller.getJpaController().findVwQtdTotalAtdCurrentDate(getKey(value));
        }

        long getKey(String value) {
            long key;
            key = Long.parseLong(value);
            return key;
        }

        String getStringKey(long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VwQtdTotalAtdCurrentDate) {
                VwQtdTotalAtdCurrentDate o = (VwQtdTotalAtdCurrentDate) object;
                return getStringKey(o.getQtdAtd());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + VwQtdTotalAtdCurrentDate.class.getName());
            }
        }
    }
}
