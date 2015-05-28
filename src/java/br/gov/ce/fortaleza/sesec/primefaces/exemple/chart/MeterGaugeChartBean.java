/*
 <p:meterGaugeChart id="sample" value="#{chartBean.meterGaugeModel}" style="width:400px;height:250px" title="Simple MeterGauge" label="km/h"/>

 <p:meterGaugeChart id="custom" value="#{chartBean.meterGaugeModel}" showTickLabels="false" labelHeightAdjust="110" intervalOuterRadius="130" 
 seriesColors="66cc66, 93b75f, E7E658, cc6666" style="width:400px;height:250px" title="Custom Options" label="km/h"/>
 */
package br.gov.ce.fortaleza.sesec.primefaces.exemple.chart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.model.chart.MeterGaugeChartModel;

/**
 *
 * @author PrimefacesTeam
 */
public class MeterGaugeChartBean implements Serializable {

    private MeterGaugeChartModel meterGaugeModel;

    public MeterGaugeChartBean() {
        createMeterGaugeModel();
    }

    public void createMeterGaugeModel() {

        List<Number> intervals = new ArrayList<Number>() {
            {
                add(20);
                add(50);
                add(120);
                add(220);
            }
        };

        meterGaugeModel = new MeterGaugeChartModel(140, intervals);
    }
}
