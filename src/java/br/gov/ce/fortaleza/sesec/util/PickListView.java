package br.gov.ce.fortaleza.sesec.util;

import br.gov.ce.fortaleza.sesec.jsf.controller.AgentesController;
import br.gov.ce.fortaleza.sesec.entities.Agentes;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import br.gov.ce.fortaleza.sesec.jpa.controller.AgentesJpaController;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

@ManagedBean
public class PickListView {

    private DualListModel<String> agentes;
    private List<Agentes> agenteList = null;
   
    public PickListView() {
        List<String> source = new ArrayList<String>();
        List<String> target = new ArrayList<String>();
        //List<Agentes> agenteList = null;
        AgentesJpaController ac = new AgentesJpaController(Persistence.createEntityManagerFactory("ipePU")); 
        
        //agenteList.add((Agentes)ac.findAgentesEntities());
        agenteList = ac.findAgentesEntities();
        
        for (int i = 0; i<= agenteList.size()-1; i++){
            source.add(agenteList.get(i).getNomeGuerra());
        }
        /*source.add("Amauri");
         * source.add("Teles");
         * source.add("Robson");
         * source.add("Karlos Anderson");
         * source.add("Ana Rosa");*/
//more cities
        agentes  = new DualListModel<String>(source, target);
    }
    

    public DualListModel<String> getAgentes() {
        return agentes;
    }

    public void setAgentes(DualListModel<String> agentes) {
        this.agentes = agentes;
    }
    
    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            //builder.append(((Theme) item).getName()).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
     
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    } 
}