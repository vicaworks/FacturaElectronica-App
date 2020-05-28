/**
 * 
 */
package com.vcw.falecpv.web.ctrl.reporte;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.dto.TotalesDto;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.ConsultaVentaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class RepFacAnuCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5541322574433690245L;
	
	@EJB
	private ConsultaVentaServicio consultaVentaServicio;

	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private List<VentasQuery> ventasQueryList;
	private TotalesDto totalesDto;
	
	/**
	 * 
	 */
	public RepFacAnuCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarMeses(hasta, -3);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain3", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			ventasQueryList = null;
			ventasQueryList = consultaVentaServicio.getFacturasAnuladas(
					AppJsfUtil.getEstablecimiento().getIdestablecimiento(), criterioBusqueda, desde, hasta); 
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain3", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void totalizar() {
		
	}
	
	public StreamedContent getFile() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain3", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
		return null;
	}

	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
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
	 * @return the ventasQueryList
	 */
	public List<VentasQuery> getVentasQueryList() {
		return ventasQueryList;
	}

	/**
	 * @param ventasQueryList the ventasQueryList to set
	 */
	public void setVentasQueryList(List<VentasQuery> ventasQueryList) {
		this.ventasQueryList = ventasQueryList;
	}

	/**
	 * @return the totalesDto
	 */
	public TotalesDto getTotalesDto() {
		return totalesDto;
	}

	/**
	 * @param totalesDto the totalesDto to set
	 */
	public void setTotalesDto(TotalesDto totalesDto) {
		this.totalesDto = totalesDto;
	}

}
