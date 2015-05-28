package br.gov.ce.fortaleza.sesec.primefaces.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.gov.ce.fortaleza.sesec.entities.Bairros;
import br.gov.ce.fortaleza.sesec.entities.Ser;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import br.gov.ce.fortaleza.sesec.jpa.controller.BairrosJpaController;
import br.gov.ce.fortaleza.sesec.jpa.controller.SerJpaController;

@ManagedBean
@ViewScoped
public class BairrosBySerDropdownView implements Serializable {

    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private String bairro;
    private String ser;
    private Map<String, String> bairros;
    private Map<String, String> sers;
    private List<Bairros> b;
    private List<Ser> s;
    private BairrosJpaController bjc = null;
    private SerJpaController sjc = null;
    //private Map[] mapSER;
    private Map<String, String>[] mapSER;

    @PostConstruct
    public void init() {
        bairros = new HashMap<String, String>();
        bjc = new BairrosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        b = bjc.findBairrosEntities();
        for (int i = 0; i <= b.size() - 1; i++) {
            bairros.put(b.get(i).getNome(), b.get(i).getNome());
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {
            Logger.getLogger(BairrosBySerDropdownView.class.getName()).log(Level.SEVERE, null, ex);
        }
        Map<String, String> map = new HashMap<String, String>();
        sjc = new SerJpaController(Persistence.createEntityManagerFactory("ipePU"));
        s = sjc.findSerEntities();
        mapSER = new HashMap[s.size()];
        for (int i = 0; i <= s.size() - 1; i++) {
            mapSER[i] = new HashMap<String, String>();
            mapSER[i].put(s.get(i).getNome(), s.get(i).getNome());
        }
          try {
            Thread.sleep(30);
        } catch (InterruptedException ex) {
            Logger.getLogger(BairrosBySerDropdownView.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int i = 0; i <= b.size() - 1; i++) {
            for (int j = 0; j <= s.size() - 1; j++) {
                if (b.get(i).getIdSer().getId() == s.get(j).getId()) {
                    data.put(b.get(i).getNome(), mapSER[j]);
                }
            }


        }
        System.out.println("Passou pelo bean BairrosBySer");
        
    }

    public List<Bairros> getB() {
        return b;
    }

    public void setB(List<Bairros> b) {
        this.b = b;
    }

    public List<Ser> getS() {
        return s;
    }

    public void setS(List<Ser> s) {
        this.s = s;
    }

    public BairrosJpaController getBjc() {
        return bjc;
    }

    public void setBjc(BairrosJpaController bjc) {
        this.bjc = bjc;
    }

    public SerJpaController getSjc() {
        return sjc;
    }

    public void setSjc(SerJpaController sjc) {
        this.sjc = sjc;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getSer() {
        return ser;
    }

    public void setSer(String ser) {
        this.ser = ser;
    }

    public Map<String, String> getBairros() {
        return bairros;
    }

    public Map<String, String> getSers() {
        return sers;
    }

    public void onBairroChange() {
        if (bairro != null && !bairro.equals("")) {
            sers = data.get(bairro);
        } else {
            sers = new HashMap<String, String>();
        }
    }

    public void displayLocation() {
        FacesMessage msg;
        if (ser != null && bairro != null) {
            msg = new FacesMessage("Selected", ser + " of " + bairro);
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid", "Ser is not selected.");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}