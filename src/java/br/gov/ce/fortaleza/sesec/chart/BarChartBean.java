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
 * @author Primefaces
 */
import br.gov.ce.fortaleza.sesec.controller.AtendimentosController;
import br.gov.ce.fortaleza.sesec.entities.Atendimentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import br.gov.ce.fortaleza.sesec.jpa.controller.AtendimentosJpaController;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Thiago Aderaldo Lessa
 */
@ManagedBean(name = "barChartBean")
@SessionScoped
public class BarChartBean implements Serializable {

    private CartesianChartModel categoryModel;
    List<Atendimentos> atendimentos;

    public BarChartBean() {
        
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public CartesianChartModel getCategoryModelByResponsible() {
        
        AtendimentosJpaController ac = new AtendimentosJpaController(null);
        atendimentos = ac.findAtendimentosEntities();
        categoryModel = new CartesianChartModel();
        ChartSeries responsavel = new ChartSeries();
        List<String> nomes = new ArrayList<String>();
        Map<String, Integer> map = new TreeMap<String, Integer>();
        
        responsavel.setLabel("Responsavel");

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
            responsavel.set(nome, map.get(nome));
        }
        

        
        categoryModel.addSeries(responsavel);
        
        return categoryModel;
        
    }
    
   
}
