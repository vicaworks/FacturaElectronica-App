/**
 * 
 */
package com.vcw.falecpv.web.ctrl.comprobantes.incoming;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class CompRecRetencionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7254601440848632731L;

	private Date desde;
	private Date hasta;
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	public CompRecRetencionCtrl() {
	}

	@PostConstruct
	private void init() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -21);
			comprobanteRecibidoList = null;
			comprobanteRecibidoSeleccionList = null;
			super.totalizarComprobantesRecibidos();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			AppJsfUtil.limpiarFiltrosDataTable("formMain:fsvRetencion:compRetSriDT");
			super.consultarComprobanteRecibido(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa(), GenTipoDocumentoEnum.RETENCION, desde, hasta, criterioBusqueda);
			super.totalizarComprobantesRecibidos();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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

}
