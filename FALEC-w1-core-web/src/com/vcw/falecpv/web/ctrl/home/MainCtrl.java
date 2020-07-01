/**
 * 
 */
package com.vcw.falecpv.web.ctrl.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;

import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.dto.VentasDto;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class MainCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3288465203727050008L;

	private BarChartModel barModel;
	private List<VentasDto> ventasDtoList;
	
	/**
	 * 
	 */
	public MainCtrl() {
	}
	
	@PostConstruct
    public void init() {
		createBarModel();
		createVentasDiarias();
	}
	
	private void createVentasDiarias() {
		
		ventasDtoList = new ArrayList<>();
		ventasDtoList.add(new VentasDto("1", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("2", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("3", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("4", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("5", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("6", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("7", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("8", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("9", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("10", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("11", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("12", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("13", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("14", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("15", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("16", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("17", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("18", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("19", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("20", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("21", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("22", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("23", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("24", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("25", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("26", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		ventasDtoList.add(new VentasDto("27", "MACIAS FRANCO JONATHAN JOEL", new Date(), 1.60d));
		
	}

	private void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();
         
        BarChartDataSet barDataSet = new BarChartDataSet();
        barDataSet.setLabel("Ventas Mensuales");
         
        List<Number> values = new ArrayList<>();
        values.add(65);
        values.add(59);
        values.add(80);
        values.add(81);
        values.add(56);
        values.add(55);
        values.add(40);
        barDataSet.setData(values);
         
        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        barDataSet.setBackgroundColor(bgColor);
         
        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        barDataSet.setBorderColor(borderColor);
        barDataSet.setBorderWidth(1);
         
        data.addChartDataSet(barDataSet);
         
        List<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        data.setLabels(labels);
        barModel.setData(data);
         
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
         
//        Title title = new Title();
//        title.setDisplay(true);
//        title.setText("Ventas Mensuales");
//        title.setFontSize(20);
//        options.setTitle(title);
 
        Legend legend = new Legend();
        legend.setDisplay(true);
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);
 
        barModel.setOptions(options);
    }

	/**
	 * @return the barModel
	 */
	public BarChartModel getBarModel() {
		return barModel;
	}

	/**
	 * @param barModel the barModel to set
	 */
	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	/**
	 * @return the ventasDtoList
	 */
	public List<VentasDto> getVentasDtoList() {
		return ventasDtoList;
	}

	/**
	 * @param ventasDtoList the ventasDtoList to set
	 */
	public void setVentasDtoList(List<VentasDto> ventasDtoList) {
		this.ventasDtoList = ventasDtoList;
	}
	
	
}
