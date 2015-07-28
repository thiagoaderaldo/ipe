/*
 * Use the code bellow to use charts with JSF 2.x
 
 <p:barChart id="basic" value="#{chartBean.categoryModel}" legendPosition="ne"
 title="Basic Bar Chart" min="0" max="200" style="height:300px"/>

 <p:barChart id="horizontal" value="#{chartBean.categoryModel}" legendPosition="se" style="height:300px"
 title="Horizontal Bar Chart" orientation="horizontal" min="0" max="200"/>

 <p:barChart id="stacked" value="#{chartBean.categoryModel}" legendPosition="ne" style="height:300px"
 title="Stacked Bar Chart" stacked="true" barMargin="50" min="0" max="300"/>
                    
 */
package br.gov.ce.fortaleza.sesec.chart;

/**
 *
 * @author Thiago Aderaldo, powered by Primefaces Team.
 */
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import br.gov.ce.fortaleza.sesec.util.DateManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Persistence;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@ManagedBean(name = "bcAtdBySer")
@SessionScoped
public class BCAtdBySer implements Serializable {

   private BarChartModel barModel;
//   private HorizontalBarChartModel horizontalBarModel;
    List<Atendimentos> atendimentos;
    private AtendimentosJpaController jpaController = null;

    public BCAtdBySer() {
    }

    public BarChartModel getCategoryModel() {
        return barModel;
    }

    private AtendimentosJpaController getJpaController() {
        if (jpaController == null) {
            jpaController = new AtendimentosJpaController(Persistence.createEntityManagerFactory("ipePU"));
        }
        return jpaController;
    }

    public CartesianChartModel getLastSevenAtdBySer() {
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date current_DATE = new Date();

        Date startingDate = current_DATE;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(startingDate);
        gc.add(Calendar.DAY_OF_YEAR, -7);
        Date lastSevenDay = gc.getTime();
//        System.out.println("Data atual: " + current_DATE + " \nSete dias antes: " + lastSevenDay);

        atendimentos = getJpaController().findAtendimentoBetweenDates(DateManager.DateUtilToString("dd/MM/yyyy", lastSevenDay), 
                DateManager.DateUtilToString("dd/MM/yyyy", current_DATE));
        barModel = new BarChartModel();
        ChartSeries chartSeries = new ChartSeries();
        List<String> nomes = new ArrayList<String>();
        Map<String, Integer> map = new TreeMap<String, Integer>();

        if (atendimentos != null && atendimentos.size() > 0) {
            for (Atendimentos atendimento : atendimentos) {
                nomes.add(atendimento.getIdBairro().getIdSer().getNome());
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
                 * System.out.println();*/;
                chartSeries.set(nome, map.get(nome));
            }
        }else{
            chartSeries.set("Sem ocorrência no período", 100);
        }

        barModel.addSeries(chartSeries);
        barModel.setAnimate(true);
        return barModel;

    }
}
