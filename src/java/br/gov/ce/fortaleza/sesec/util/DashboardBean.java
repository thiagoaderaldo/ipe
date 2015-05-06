package br.gov.ce.fortaleza.sesec.util;


import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

@ManagedBean (name="dashboardBean")
@SessionScoped
public class DashboardBean implements Serializable {  
  
    private DashboardModel model;  
      
    public DashboardBean() {  
        model = new DefaultDashboardModel();  
        DashboardColumn column1 = new DefaultDashboardColumn();  
        DashboardColumn column2 = new DefaultDashboardColumn();  
        DashboardColumn column3 = new DefaultDashboardColumn();  
        
        column1.addWidget("resumo");
        column1.addWidget("noticias");  
        column2.addWidget("gmapsearch");  
        
        model.addColumn(column1);  
        model.addColumn(column2);   
    }  
      
    public void handleReorder(DashboardReorderEvent event) {  
        FacesMessage message = new FacesMessage();  
        message.setSeverity(FacesMessage.SEVERITY_INFO);  
        message.setSummary("Painel reordenado: " + event.getWidgetId());  
        message.setDetail("Linha: " + (event.getItemIndex()+1) + ", Coluna: " + (event.getColumnIndex()+1));  
          
        addMessage(message);  
    }  
      
      
    private void addMessage(FacesMessage message) {  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
      
    public DashboardModel getModel() {  
        return model;  
    }  
}  