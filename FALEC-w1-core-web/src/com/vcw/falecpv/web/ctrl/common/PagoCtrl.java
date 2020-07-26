/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.query.VentasQuery;
import com.vcw.falecpv.core.servicio.PagoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PagoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1954399330489712126L;

	@EJB
	private PagoServicio pagoServicio;
	
	private List<Pago> pagoList;
	private VentasQuery ventasQuery;
	
	/**
	 * 
	 */
	public PagoCtrl() {
	}
	
	public void cargarPagos() {
		try {
			
			pagoList = null;
			pagoList = pagoServicio.getPagoDao().getByIdCabecera(ventasQuery.getIdcabecera());
			
			AppJsfUtil.showModalRender("dlgResumenPagos", "frmResPagos");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}


	/**
	 * @return the pagoList
	 */
	public List<Pago> getPagoList() {
		return pagoList;
	}



	/**
	 * @param pagoList the pagoList to set
	 */
	public void setPagoList(List<Pago> pagoList) {
		this.pagoList = pagoList;
	}

	/**
	 * @return the pagoServicio
	 */
	public PagoServicio getPagoServicio() {
		return pagoServicio;
	}

	/**
	 * @param pagoServicio the pagoServicio to set
	 */
	public void setPagoServicio(PagoServicio pagoServicio) {
		this.pagoServicio = pagoServicio;
	}

	/**
	 * @return the ventasQuery
	 */
	public VentasQuery getVentasQuery() {
		return ventasQuery;
	}

	/**
	 * @param ventasQuery the ventasQuery to set
	 */
	public void setVentasQuery(VentasQuery ventasQuery) {
		this.ventasQuery = ventasQuery;
	}

}
