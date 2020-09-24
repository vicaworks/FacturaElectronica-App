/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class CotizacionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4680408942516741116L;
	
	
	private String criterioBusqueda;
	private Date desde;
	private Date hasta;
	private String estado;
	private String comprobanteRender = "INICIO";
	private List<Cabecera> proformaList;
	private BigDecimal totalCotizacion = BigDecimal.ZERO;
	private BigDecimal totalArchivados = BigDecimal.ZERO;
	private BigDecimal totalfacturados = BigDecimal.ZERO;
	private BigDecimal totalSeguimiento = BigDecimal.ZERO;
	
	/**
	 * 
	 */
	public CotizacionCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			consultar();
			totalizar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultar()throws DaoException{
		
	}
	
	private void totalizar() {
		totalCotizacion = BigDecimal.ZERO;
		totalArchivados = BigDecimal.ZERO;
		totalfacturados = BigDecimal.ZERO;
		totalSeguimiento = BigDecimal.ZERO;
	}
	
	public void refrescar() {
		try {
			
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void switchProforma(String proforma) {
		comprobanteRender = proforma;
		System.out.println(proforma);
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the comprobanteRender
	 */
	public String getComprobanteRender() {
		return comprobanteRender;
	}

	/**
	 * @param comprobanteRender the comprobanteRender to set
	 */
	public void setComprobanteRender(String comprobanteRender) {
		this.comprobanteRender = comprobanteRender;
	}

	/**
	 * @return the proformaList
	 */
	public List<Cabecera> getProformaList() {
		return proformaList;
	}

	/**
	 * @param proformaList the proformaList to set
	 */
	public void setProformaList(List<Cabecera> proformaList) {
		this.proformaList = proformaList;
	}

	/**
	 * @return the totalCotizacion
	 */
	public BigDecimal getTotalCotizacion() {
		return totalCotizacion;
	}

	/**
	 * @param totalCotizacion the totalCotizacion to set
	 */
	public void setTotalCotizacion(BigDecimal totalCotizacion) {
		this.totalCotizacion = totalCotizacion;
	}

	/**
	 * @return the totalArchivados
	 */
	public BigDecimal getTotalArchivados() {
		return totalArchivados;
	}

	/**
	 * @param totalArchivados the totalArchivados to set
	 */
	public void setTotalArchivados(BigDecimal totalArchivados) {
		this.totalArchivados = totalArchivados;
	}

	/**
	 * @return the totalfacturados
	 */
	public BigDecimal getTotalfacturados() {
		return totalfacturados;
	}

	/**
	 * @param totalfacturados the totalfacturados to set
	 */
	public void setTotalfacturados(BigDecimal totalfacturados) {
		this.totalfacturados = totalfacturados;
	}

	/**
	 * @return the totalSeguimiento
	 */
	public BigDecimal getTotalSeguimiento() {
		return totalSeguimiento;
	}

	/**
	 * @param totalSeguimiento the totalSeguimiento to set
	 */
	public void setTotalSeguimiento(BigDecimal totalSeguimiento) {
		this.totalSeguimiento = totalSeguimiento;
	}

}
