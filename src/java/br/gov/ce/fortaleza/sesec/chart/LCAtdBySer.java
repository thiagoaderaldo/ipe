/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.fortaleza.sesec.chart;

import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import br.gov.ce.fortaleza.sesec.util.DateManager;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Persistence;
import org.apache.batik.css.engine.value.css2.SrcManager;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author thiago
 */
@ManagedBean(name = "lcAtdBySer")
@SessionScoped
public class LCAtdBySer implements Serializable {

    private LineChartModel lineModel;
    private AtendimentosJpaController jpaController;
    List<Atendimentos> atendimentos;

    public LCAtdBySer() {
    }

    
    @PostConstruct
//    public void init() {
//        createLineModels();
//    }

    
    public LineChartModel getLineModel() {
        return getCurrentMonth();
    }

    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

//    private void createLineModels() {
//        lineModel = initLinearModel();
//        lineModel.setTitle("Qtd. Ocorrências x Regional - Mês de Julho");
////        lineModel.setLegendPosition("");
//        Axis xAxis = lineModel.getAxis(AxisType.X);
//        xAxis.setLabel("Dias");
//        xAxis.setMin(Integer.parseInt("1"));
//        xAxis.setMax(Integer.parseInt("31"));
//        
//        Axis yAxis = lineModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Qtd. Ocorrências");
//        yAxis.setMin(0);
//        yAxis.setMax(atendimentos.size() - 1);
//
//        lineModel.setAnimate(true);
//
//
//    }

    private LineChartModel getCurrentMonth() {
        atendimentos = getJpaController().findByCurrentMonth();
        List<String> nomes = new ArrayList<String>();
        Map<String, Integer> map = new TreeMap<String, Integer>();
        lineModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();

        if (atendimentos != null && atendimentos.size() > 0) {
            for (Atendimentos atendimento : atendimentos) {
                nomes.add(DateManager.DateUtilToString("dd", atendimento.getDataSolicitacao()));
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
                series1.set(nome, map.get(nome));
//                System.out.println("Data: " + nome + " Valor: " + map.get(nome));
//                series1.setLabel(nome);
            }
        } else {
            series1.set("Sem ocorrências nesta data.", 100);
        }

        //Preenche a série do gráfico.
        lineModel.addSeries(series1);
        //Preenche o título do gráfico.
        lineModel.setTitle("Qtd. Ocorrências x Regional - Mês de Julho");
        //Configurações do eixo X.
        lineModel.getAxis(AxisType.X).setLabel("Dias");
        lineModel.getAxis(AxisType.X).setTickAngle(-50);
        lineModel.getAxis(AxisType.X).setMin(Integer.parseInt("1"));
        lineModel.getAxis(AxisType.X).setMax(Integer.parseInt("31"));
        lineModel.getAxis(AxisType.X).setTickFormat("%'d");
        //Configurações do eixo Y.
        lineModel.getAxis(AxisType.Y).setLabel("Qtd. Ocorrências");
//        lineModel.getAxis(AxisType.Y).setTickAngle(-50);
        lineModel.getAxis(AxisType.Y).setMin(0);
        lineModel.getAxis(AxisType.Y).setMax(atendimentos.size() - 1);
        lineModel.getAxis(AxisType.Y).setTickFormat("%'d");
        //Habilita a animação do gráfico.
        lineModel.setAnimate(true);
        return lineModel;
    }
}