package controller;

import entities.Grupos;
import controller.util.JsfUtil;
import controller.util.PaginationHelper;
import jpa.controller.GruposJpaController;

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

@ManagedBean(name = "gruposController")
@SessionScoped
public class GruposController implements Serializable {

    private Grupos current;
    private DataModel items = null;
    private GruposJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public GruposController() {
    }

    public Grupos getSelected() {
        if (current == null) {
            current = new Grupos();
            selectedItemIndex = -1;
        }
        return current;
    }

    private GruposJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new GruposJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getJpaController().getGruposCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getJpaController().findGruposEntities(getPageSize(), getPageFirstItem()));
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
        current = (Grupos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Grupos();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getJpaController().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("GruposCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Grupos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getJpaController().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("GruposUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Grupos) getItems().getRowData();
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
            getJpaController().destroy(current.getGrupoId());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/pt_br").getString("GruposDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/pt_br").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getJpaController().getGruposCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getJpaController().findGruposEntities(1, selectedItemIndex).get(0);
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
        return JsfUtil.getSelectItems(getJpaController().findGruposEntities(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(getJpaController().findGruposEntities(), true);
    }

    @FacesConverter(forClass = Grupos.class)
    public static class GruposControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            GruposController controller = (GruposController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "gruposController");
            return controller.getJpaController().findGrupos(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Grupos) {
                Grupos o = (Grupos) object;
                return getStringKey(o.getGrupoId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Grupos.class.getName());
            }
        }
    }
}
