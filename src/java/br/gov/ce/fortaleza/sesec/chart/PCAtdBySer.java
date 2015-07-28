/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.chart;

import br.gov.ce.fortaleza.sesec.jsf.controller.AtendimentosController;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import javax.persistence.Persistence;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@ManagedBean(name = "pcAtdBySer")
@SessionScoped
public class PCAtdBySer {

    private PieChartModel pieModel;
    List<Atendimentos> atendimentos;
    private AtendimentosJpaController jpaController = null;

    public List getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List atendimentos) {
        this.atendimentos = atendimentos;
    }

    public PCAtdBySer() {
        //createPieModelByResponsible();
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public PieChartModel getAtdBySerCurrentDate() {


        atendimentos = getJpaController().findByCurrentDate();
        pieModel = new PieChartModel();
        List<String> nomes = new ArrayList<String>();
        Map<String, Integer> map = new TreeMap<String, Integer>();
        if (atendimentos != null && atendimentos.size() > 0) {
            for (int i = 0; i < atendimentos.size(); i++) {
                nomes.add(atendimentos.get(i).getIdBairro().getIdSer().getNome());
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
        } else {
            pieModel.set("Sem ocorrências nesta data.", 100);
        }

//        pieModel = new PieChartModel();
//        pieModel.set("Sem ocorrências nesta data.", 100);
        pieModel.setShowDataLabels(true);
        pieModel.setLegendPosition("w");

        return pieModel;
    }
}
