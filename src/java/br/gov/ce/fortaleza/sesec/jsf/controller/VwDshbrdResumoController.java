package br.gov.ce.fortaleza.sesec.jsf.controller;

import br.gov.ce.fortaleza.sesec.entities.VwDshbrdResumo;
import br.gov.ce.fortaleza.sesec.jpa.controller.VwDshbrdResumoJpaController;
import br.gov.ce.fortaleza.sesec.jsf.controller.util.PaginationHelper;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.persistence.Persistence;

@ManagedBean(name = "vwDshbrdResumoController")
@SessionScoped
public class VwDshbrdResumoController implements Serializable {

    private VwDshbrdResumo current;
    private DataModel items = null;
    private VwDshbrdResumoJpaController jpaController = null;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public VwDshbrdResumoController() {
    }

    public VwDshbrdResumo getSelected() {
        if (current == null) {
            current = new VwDshbrdResumo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private VwDshbrdResumoJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new VwDshbrdResumoJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }


    public VwDshbrdResumo getVwDshbrdResumo(){
        current = getJpaController().findVwDshbrdResumo();
        
        return current;
    }
    
    
}
