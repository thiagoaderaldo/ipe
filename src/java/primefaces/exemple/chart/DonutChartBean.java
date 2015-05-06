/*
 <p:donutChart id="sample" value="#{chartBean.donutModel}" legendPosition="w"
 title="Sample Donut Chart" style="width:400px;height:300px" />

 <p:donutChart id="custom" value="#{chartBean.donutModel}" 
 legendPosition="e" sliceMargin="5" showDataLabels="true" dataFormat="value" shadow="false"
 title="Custom Options" style="width:400px;height:300px" />
                    
 */
package primefaces.exemple.chart;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.primefaces.model.chart.DonutChartModel;

/**
 *
 * @author Primefaces Team
 */
public class DonutChartBean implements Serializable {

    private DonutChartModel donutModel;

    public DonutChartBean() {
        createDonutModel();
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public void createDonutModel() {
        donutModel = new DonutChartModel();

        Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
        circle1.put("Brand 1", 150);
        circle1.put("Brand 2", 400);
        circle1.put("Brand 3", 200);
        circle1.put("Brand 4", 10);
        donutModel.addCircle(circle1);

        Map<String, Number> circle2 = new LinkedHashMap<String, Number>();
        circle2.put("Brand 1", 540);
        circle2.put("Brand 2", 125);
        circle2.put("Brand 3", 702);
        circle2.put("Brand 4", 421);
        donutModel.addCircle(circle2);

        Map<String, Number> circle3 = new LinkedHashMap<String, Number>();
        circle3.put("Brand 1", 40);
        circle3.put("Brand 2", 325);
        circle3.put("Brand 3", 402);
        circle3.put("Brand 4", 421);
        donutModel.addCircle(circle3);
    }
}
