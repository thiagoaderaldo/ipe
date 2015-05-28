/*
 <p:lineChart id="filled" value="#{chartBean.categoryModel}" legendPosition="ne" fill="true"
 title="Filled" style="height:300px;margin-top:20px" xaxisLabel="Year" yaxisLabel="Births" />

 <p:lineChart id="stacked" value="#{chartBean.categoryModel}" legendPosition="ne" stacked="true" fill="true""
 title="Stacked and Filled" style="height:300px;margin-top:20px" xaxisLabel="Year" yaxisLabel="Births" />
                    
 */
package br.gov.ce.fortaleza.sesec.primefaces.exemple.chart;

import java.io.Serializable;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author Primefaces Team
 */
public class AreaChartBean implements Serializable{

    private CartesianChartModel categoryModel;

    public AreaChartBean() {
        createCategoryModel();
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");

        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");

        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 135);
        girls.set("2008", 120);

        categoryModel.addSeries(boys);
        categoryModel.addSeries(girls);
    }
}
