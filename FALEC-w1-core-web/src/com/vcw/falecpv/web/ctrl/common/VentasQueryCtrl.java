/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.query.ResumenCabeceraQuery;
import com.vcw.falecpv.core.servicio.query.ConsultaGeneralComprobanteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.guiarem.GuiaRemFormCtrl;
import com.vcw.falecpv.web.ctrl.comprobantes.nd.NotaDebitoFrmCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class VentasQueryCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7891286431084319426L;
	
	@EJB
	private ConsultaGeneralComprobanteServicio consultaGeneralComprobanteServicio;
	
	
	private Date desde;
	private Date hasta;
	private String criterio;
	private List<ResumenCabeceraQuery> resumenCabeceraQueryList;
	private ResumenCabeceraQuery resumenCabeceraQuery;
	private String callModule;
	private String callForm;
	private String viewUpdate;

	/**
	 * 
	 */
	public VentasQueryCtrl() {
	}
	
	@PostConstruct
	private void init() {
		try {
//			hasta = new Date();
//			desde = FechaUtil.agregarDias(hasta, -30);
//			consultar();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formVentasQuery", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void cargarFormulario() {
		try {
			hasta = new Date();
			desde = FechaUtil.agregarDias(hasta, -30);
			criterio = null;
			resumenCabeceraQuery = null;
			consultar();
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('formVentasQuery:intVentasQueryCriterio');");
			AppJsfUtil.showModalRender("dlgListaVentas", "formVentasQuery");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void consultar() throws DaoException {
		resumenCabeceraQueryList = null;
		resumenCabeceraQueryList = consultaGeneralComprobanteServicio.getComprovantesVentasByDateCriteria(AppJsfUtil.getEstablecimiento().getIdestablecimiento(), criterio, desde, hasta,callModule);
	}
	
	@Override
	public void buscar() {
		try {
			
			consultar();
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formVentasQuery", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			
			switch (callModule) {
			case "GUIA_REMISION":
				
				GuiaRemFormCtrl guiaRemFormCtrl = (GuiaRemFormCtrl) AppJsfUtil.getManagedBean("guiaRemFormCtrl");
				String novedades = guiaRemFormCtrl.asignarFactura(resumenCabeceraQuery);
				if(novedades!=null) {
					AppJsfUtil.addErrorMessage("formVentasQuery", "ERROR", novedades);
				}else {
					AppJsfUtil.hideModal("dlgListaVentas");
					AppJsfUtil.showModalRender("dlgDestinatario", "frmDestinatario");
					AppJsfUtil.executeJavaScript("PrimeFaces.focus('frmDestinatario:intGrDestMotTraslado');");
				}
				break;
			case "NOTA_DEBITO":
				NotaDebitoFrmCtrl notaDebitoFrmCtrl = (NotaDebitoFrmCtrl) AppJsfUtil.getManagedBean("notaDebitoFrmCtrl");
				notaDebitoFrmCtrl.nuevoByFacturaEmitida(resumenCabeceraQuery.getIdcabecera());
				AppJsfUtil.hideModal("dlgListaVentas");
				break;
			default:
				break;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formVentasQuery", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the criterio
	 */
	public String getCriterio() {
		return criterio;
	}

	/**
	 * @param criterio the criterio to set
	 */
	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}

	/**
	 * @return the resumenCabeceraQueryList
	 */
	public List<ResumenCabeceraQuery> getResumenCabeceraQueryList() {
		return resumenCabeceraQueryList;
	}

	/**
	 * @param resumenCabeceraQueryList the resumenCabeceraQueryList to set
	 */
	public void setResumenCabeceraQueryList(List<ResumenCabeceraQuery> resumenCabeceraQueryList) {
		this.resumenCabeceraQueryList = resumenCabeceraQueryList;
	}

	/**
	 * @return the resumenCabeceraQuery
	 */
	public ResumenCabeceraQuery getResumenCabeceraQuery() {
		return resumenCabeceraQuery;
	}

	/**
	 * @param resumenCabeceraQuery the resumenCabeceraQuery to set
	 */
	public void setResumenCabeceraQuery(ResumenCabeceraQuery resumenCabeceraQuery) {
		this.resumenCabeceraQuery = resumenCabeceraQuery;
	}

	/**
	 * @return the callModule
	 */
	public String getCallModule() {
		return callModule;
	}

	/**
	 * @param callModule the callModule to set
	 */
	public void setCallModule(String callModule) {
		this.callModule = callModule;
	}

	/**
	 * @return the viewUpdate
	 */
	public String getViewUpdate() {
		return viewUpdate;
	}

	/**
	 * @param viewUpdate the viewUpdate to set
	 */
	public void setViewUpdate(String viewUpdate) {
		this.viewUpdate = viewUpdate;
	}

	/**
	 * @return the callForm
	 */
	public String getCallForm() {
		return callForm;
	}

	/**
	 * @param callForm the callForm to set
	 */
	public void setCallForm(String callForm) {
		this.callForm = callForm;
	}

	
}
