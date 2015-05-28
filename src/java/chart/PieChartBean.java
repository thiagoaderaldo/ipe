/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chart;

import controller.AtendimentosController;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import jpa.controller.AtendimentosJpaController;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@ManagedBean(name = "pieChartBean")
@SessionScoped
public class PieChartBean {

    private PieChartModel pieModel;
    List<Atendimentos> atendimentos;

    public List getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List atendimentos) {
        this.atendimentos = atendimentos;
    }

    public PieChartBean() {
        //createPieModelByResponsible();
    }
   
    public PieChartModel getPieModel() {
        return pieModel;
    }

    public PieChartModel getPieModelByResponsible() {

        AtendimentosJpaController ac = new AtendimentosJpaController(null);
        atendimentos = ac.findAtendimentosEntities();
        pieModel = new PieChartModel();
        List<String> nomes = new ArrayList<String>();
        Map<String, Integer> map = new TreeMap<String, Integer>();

        for (int i = 0; i < atendimentos.size(); i++) {
            nomes.add(atendimentos.get(i).getResponsavel());
        }

        for (String nome : nomes) {
            Integer count = map.get(nome);
            if (count == null) {
                count = 0;
            }
            map.put(nome, count + 1);
        }

        //Agora, para exibir todos os nomes repetidos e quantas vezes se repetiram:
        for (String nome : map.keySet()) {
            /*System.out.printf("O nome %s se repetiu %s vezes.", nome, map.get(nome));
             * System.out.println();*/
            pieModel.set(nome, map.get(nome));
        }
        
        return pieModel;
    }
}
