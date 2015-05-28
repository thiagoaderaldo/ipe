/*
 * Use the code bellow to use charts with JSF 2.x
 
 <p:barChart id="basic" value="#{chartBean.categoryModel}" legendPosition="ne"
 title="Basic Bar Chart" min="0" max="200" style="height:300px"/>

 <p:barChart id="horizontal" value="#{chartBean.categoryModel}" legendPosition="se" style="height:300px"
 title="Horizontal Bar Chart" orientation="horizontal" min="0" max="200"/>

 <p:barChart id="stacked" value="#{chartBean.categoryModel}" legendPosition="ne" style="height:300px"
 title="Stacked Bar Chart" stacked="true" barMargin="50" min="0" max="300"/>
                    
 */
package br.gov.ce.fortaleza.sesec.primefaces.exemple.chart;

/**
 *
 * @author Primefaces
 */
import java.io.Serializable;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

public class BarChartBean implements Serializable {

    private CartesianChartModel categoryModel;

    public BarChartBean() {
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
