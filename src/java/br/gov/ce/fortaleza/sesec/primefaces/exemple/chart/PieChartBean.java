/*
<p:pieChart id="sample" value="#{chartBean.pieModel}" legendPosition="w"
                title="Sample Pie Chart" style="width:400px;height:300px" />

<p:pieChart id="custom" value="#{chartBean.pieModel}" legendPosition="e" fill="false" showDataLabels="true"
                title="Custom Options" style="width:400px;height:300px" sliceMargin="5" diameter="150" />
 */
package br.gov.ce.fortaleza.sesec.primefaces.exemple.chart;

import java.io.Serializable;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author Primefaces Team
 */
public class PieChartBean implements Serializable {

    private PieChartModel pieModel;

    public PieChartBean() {
        createPieModel();
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    private void createPieModel() {
        //Exemple:
        pieModel = new PieChartModel();

        pieModel.set("Brand 1", 540);
        pieModel.set("Brand 2", 325);
        pieModel.set("Brand 3", 702);
        pieModel.set("Brand 4", 421);


    }
}
