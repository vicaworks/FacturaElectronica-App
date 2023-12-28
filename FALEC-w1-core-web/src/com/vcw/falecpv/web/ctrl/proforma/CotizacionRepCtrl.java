/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.query.EstadisticoQuery;
import com.vcw.falecpv.core.servicio.CotizacionServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.common.MessageCommonCtrl.Message;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CotizacionRepCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5974032059050605612L;
	
	@EJB
	private CotizacionServicio cotizacionServicio;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;

	
	private Date desde;
	private Date hasta;
	private List<EstadisticoQuery> facturadoContadorList;
	private PieChartModel pieFacturadoContador;
	private List<EstadisticoQuery> facturadoValorList;
	private PieChartModel pieFacturadoValor;

	private List<EstadisticoQuery> facturadoVendedorContadorList;
	private BarChartModel barFacturadoVendedorContador;
	private List<EstadisticoQuery> facturadoVendedorValorList;
	private BarChartModel barFacturadoVendedorValor;
	/**
	 * 
	 */
	public CotizacionRepCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
			hasta = new Date();
			Calendar cl = Calendar.getInstance();
			cl.setTime(hasta);
			cl.set(Calendar.DATE, 1);
			desde = cl.getTime();
			consultarFacturado();
			consultarFacturadoVendedor();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	public Establecimiento getEstablecimiento() {
		return ((CotizacionCtrl)AppJsfUtil.getManagedBean("cotizacionCtrl")).getEstablecimientoMain();
	}

	public void consultarFacturado()throws DaoException{
		// == facturado contador
		List<EstadisticoQuery> e1 = cotizacionServicio.getFacturadoContador(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), 
				getEstablecimiento().getIdestablecimiento(), 
				desde, 
				hasta);
		
		EstadisticoQuery eArchivado = e1.stream().filter(x->x.getLabel1().equals("ARCHIVADO")).findFirst().orElse(null);
		if(eArchivado==null) {
			eArchivado = new EstadisticoQuery();
			eArchivado.setLabel1("ARCHIVADO");
			eArchivado.setValor1(BigDecimal.ZERO);
		}
		EstadisticoQuery eSeguimiento = e1.stream().filter(x->x.getLabel1().equals("SEGUIMIENTO")).findFirst().orElse(null);
		if(eSeguimiento==null) {
			eSeguimiento = new EstadisticoQuery();
			eSeguimiento.setLabel1("SEGUIMIENTO");
			eSeguimiento.setValor1(BigDecimal.ZERO);
		}
		EstadisticoQuery eFacturado = e1.stream().filter(x->x.getLabel1().equals("FACTURADO")).findFirst().orElse(null);
		if(eFacturado==null) {
			eFacturado = new EstadisticoQuery();
			eFacturado.setLabel1("FACTURADO");
			eFacturado.setValor1(BigDecimal.ZERO);
		}
		facturadoContadorList = new ArrayList<>();
		facturadoContadorList.add(eSeguimiento);
		facturadoContadorList.add(eArchivado);
		facturadoContadorList.add(eFacturado);
		
		// armar chart
		pieFacturadoContador = new PieChartModel();
		ChartData data = new ChartData();
		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();
		for (EstadisticoQuery q : facturadoContadorList) {
			values.add(q.getValor1());
		}
		dataSet.setData(values);
		List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(195, 155, 211)");
        bgColors.add("rgb(127, 179, 213)");
        bgColors.add("rgb(36, 113, 163)");
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        for (EstadisticoQuery q : facturadoContadorList) {
			labels.add(q.getLabel1());
		}
        data.setLabels(labels);
        pieFacturadoContador.setData(data);
        PieChartOptions pco = new PieChartOptions();
//        Legend le = new Legend();
//        le.setPosition("w");
//        pco.setLegend(le);
        pieFacturadoContador.setOptions(pco);
        
		
		// == facturado valor
		e1 = cotizacionServicio.getFacturadoValor(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), 
				getEstablecimiento().getIdestablecimiento(), 
				desde, 
				hasta);
		
		eArchivado = e1.stream().filter(x->x.getLabel1().equals("ARCHIVADO")).findFirst().orElse(null);
		if(eArchivado==null) {
			eArchivado = new EstadisticoQuery();
			eArchivado.setLabel1("ARCHIVADO");
			eArchivado.setValor1(BigDecimal.ZERO);
		}
		eSeguimiento = e1.stream().filter(x->x.getLabel1().equals("SEGUIMIENTO")).findFirst().orElse(null);
		if(eSeguimiento==null) {
			eSeguimiento = new EstadisticoQuery();
			eSeguimiento.setLabel1("SEGUIMIENTO");
			eSeguimiento.setValor1(BigDecimal.ZERO);
		}
		eFacturado = e1.stream().filter(x->x.getLabel1().equals("FACTURADO")).findFirst().orElse(null);
		if(eFacturado==null) {
			eFacturado = new EstadisticoQuery();
			eFacturado.setLabel1("FACTURADO");
			eFacturado.setValor1(BigDecimal.ZERO);
		}
		facturadoValorList = new ArrayList<>();
		facturadoValorList.add(eSeguimiento);
		facturadoValorList.add(eArchivado);
		facturadoValorList.add(eFacturado);
		// armar chart
		pieFacturadoValor = new PieChartModel();
		data = new ChartData();
		dataSet = new PieChartDataSet();
		values = new ArrayList<>();
		for (EstadisticoQuery q : facturadoValorList) {
			values.add(q.getValor1());
		}
		dataSet.setData(values);
		bgColors = new ArrayList<>();
		bgColors.add("rgb(195, 155, 211)");
        bgColors.add("rgb(127, 179, 213)");
        bgColors.add("rgb(36, 113, 163)");
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        labels = new ArrayList<>();
        for (EstadisticoQuery q : facturadoValorList) {
			labels.add(q.getLabel1());
		}
        data.setLabels(labels);
        pieFacturadoValor.setData(data);
		
	}
	
	public void consultarFacturadoVendedor()throws DaoException{
		// == facturado vendedor contador
		barFacturadoVendedorContador = new BarChartModel();
		facturadoVendedorContadorList = cotizacionServicio.getFacturadoVendedorContador(
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), 
				getEstablecimiento().getIdestablecimiento(), 
				desde, 
				hasta);
		// armar chart
		ChartSeries facturados = new ChartSeries();
		facturados.setLabel("FACTURADO");
		for (EstadisticoQuery e : facturadoVendedorContadorList) {
			facturados.set(e.getLabel1(), e.getValor1());
		}
		
		ChartSeries archivados = new ChartSeries();
		archivados.setLabel("ARCHIVADO");
		for (EstadisticoQuery e : facturadoVendedorContadorList) {
			archivados.set(e.getLabel1(), e.getValor2());
		}
		
		ChartSeries seguimiento = new ChartSeries();
		seguimiento.setLabel("SEGUIMIENTO");
		for (EstadisticoQuery e : facturadoVendedorContadorList) {
			seguimiento.set(e.getLabel1(), e.getValor3());
		}
		barFacturadoVendedorContador.addSeries(facturados);
		barFacturadoVendedorContador.addSeries(archivados);
		barFacturadoVendedorContador.addSeries(seguimiento);
		
		barFacturadoVendedorContador.setZoom(true);
		barFacturadoVendedorContador.setShowPointLabels(true);
		barFacturadoVendedorContador.setLegendPosition("ne");

        Axis xAxis = barFacturadoVendedorContador.getAxis(AxisType.X);
        xAxis.setLabel("Vendedor");

        Axis yAxis = barFacturadoVendedorContador.getAxis(AxisType.Y);
        yAxis.setLabel("Cantidad");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
        
        barFacturadoVendedorContador.setSeriesColors("C39BD3,7FB3D5,2471A3");
        
     // == facturado vendedor valor
 		barFacturadoVendedorValor = new BarChartModel();
 		facturadoVendedorValorList = cotizacionServicio.getFacturadoVendedorValor(
 				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), 
 				getEstablecimiento().getIdestablecimiento(), 
 				desde, 
 				hasta);
 	// armar chart
		facturados = new ChartSeries();
		facturados.setLabel("FACTURADO");
		for (EstadisticoQuery e : facturadoVendedorValorList) {
			facturados.set(e.getLabel1(), e.getValor1()!=null?e.getValor1():BigDecimal.ZERO);
		}
		
		archivados = new ChartSeries();
		archivados.setLabel("ARCHIVADO");
		for (EstadisticoQuery e : facturadoVendedorValorList) {
			archivados.set(e.getLabel1(), e.getValor2()!=null?e.getValor2():BigDecimal.ZERO);
		}
		
		seguimiento = new ChartSeries();
		seguimiento.setLabel("SEGUIMIENTO");
		for (EstadisticoQuery e : facturadoVendedorValorList) {
			seguimiento.set(e.getLabel1(), e.getValor3()!=null?e.getValor3():BigDecimal.ZERO);
		}
		barFacturadoVendedorValor.addSeries(facturados);
		barFacturadoVendedorValor.addSeries(archivados);
		barFacturadoVendedorValor.addSeries(seguimiento);
		
		barFacturadoVendedorValor.setZoom(true);
		barFacturadoVendedorValor.setShowPointLabels(true);
		barFacturadoVendedorValor.setLegendPosition("ne");
		
		xAxis = barFacturadoVendedorValor.getAxis(AxisType.X);
        xAxis.setLabel("Vendedor");

        yAxis = barFacturadoVendedorValor.getAxis(AxisType.Y);
        yAxis.setLabel("Valor");
        barFacturadoVendedorValor.setSeriesColors("C39BD3,7FB3D5,2471A3");
	}
	
	public Integer getTotal(List<EstadisticoQuery> eList) {
		return eList==null?0:eList.stream().mapToInt(x->x.getValor1().intValue()).sum();
	}
	
	
	@Override
	public void buscar() {
		try {
			consultarFacturado();
			consultarFacturadoVendedor();
		} catch (Exception e) {
			e.printStackTrace();
			getMessageCommonCtrl().crearMensaje("Error", 
					TextoUtil.imprimirStackTrace(e, 
							AppConfiguracion.getInteger("stacktrace.length")), 
					Message.ERROR);
		}
	}
	
	/**
	 * @return the desde
	 */
	public Date getDesde() {
		return desde;
	}

	/**
	 * @param desde the desde to set
	 */
	public void setDesde(Date desde) {
		this.desde = desde;
	}

	/**
	 * @return the hasta
	 */
	public Date getHasta() {
		return hasta;
	}

	/**
	 * @param hasta the hasta to set
	 */
	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	/**
	 * @return the facturadoContadorList
	 */
	public List<EstadisticoQuery> getFacturadoContadorList() {
		return facturadoContadorList;
	}

	/**
	 * @param facturadoContadorList the facturadoContadorList to set
	 */
	public void setFacturadoContadorList(List<EstadisticoQuery> facturadoContadorList) {
		this.facturadoContadorList = facturadoContadorList;
	}

	/**
	 * @return the facturadoValorList
	 */
	public List<EstadisticoQuery> getFacturadoValorList() {
		return facturadoValorList;
	}

	/**
	 * @param facturadoValorList the facturadoValorList to set
	 */
	public void setFacturadoValorList(List<EstadisticoQuery> facturadoValorList) {
		this.facturadoValorList = facturadoValorList;
	}

	/**
	 * @return the facturadoVendedorContadorList
	 */
	public List<EstadisticoQuery> getFacturadoVendedorContadorList() {
		return facturadoVendedorContadorList;
	}

	/**
	 * @param facturadoVendedorContadorList the facturadoVendedorContadorList to set
	 */
	public void setFacturadoVendedorContadorList(List<EstadisticoQuery> facturadoVendedorContadorList) {
		this.facturadoVendedorContadorList = facturadoVendedorContadorList;
	}

	/**
	 * @return the facturadoVendedorValorList
	 */
	public List<EstadisticoQuery> getFacturadoVendedorValorList() {
		return facturadoVendedorValorList;
	}

	/**
	 * @param facturadoVendedorValorList the facturadoVendedorValorList to set
	 */
	public void setFacturadoVendedorValorList(List<EstadisticoQuery> facturadoVendedorValorList) {
		this.facturadoVendedorValorList = facturadoVendedorValorList;
	}

	/**
	 * @return the pieFacturadoContador
	 */
	public PieChartModel getPieFacturadoContador() {
		return pieFacturadoContador;
	}

	/**
	 * @param pieFacturadoContador the pieFacturadoContador to set
	 */
	public void setPieFacturadoContador(PieChartModel pieFacturadoContador) {
		this.pieFacturadoContador = pieFacturadoContador;
	}

	/**
	 * @return the pieFacturadoValor
	 */
	public PieChartModel getPieFacturadoValor() {
		return pieFacturadoValor;
	}

	/**
	 * @param pieFacturadoValor the pieFacturadoValor to set
	 */
	public void setPieFacturadoValor(PieChartModel pieFacturadoValor) {
		this.pieFacturadoValor = pieFacturadoValor;
	}

	/**
	 * @return the barFacturadoVendedorContador
	 */
	public BarChartModel getBarFacturadoVendedorContador() {
		return barFacturadoVendedorContador;
	}

	/**
	 * @param barFacturadoVendedorContador the barFacturadoVendedorContador to set
	 */
	public void setBarFacturadoVendedorContador(BarChartModel barFacturadoVendedorContador) {
		this.barFacturadoVendedorContador = barFacturadoVendedorContador;
	}

	/**
	 * @return the barFacturadoVendedorValor
	 */
	public BarChartModel getBarFacturadoVendedorValor() {
		return barFacturadoVendedorValor;
	}

	/**
	 * @param barFacturadoVendedorValor the barFacturadoVendedorValor to set
	 */
	public void setBarFacturadoVendedorValor(BarChartModel barFacturadoVendedorValor) {
		this.barFacturadoVendedorValor = barFacturadoVendedorValor;
	}

}
