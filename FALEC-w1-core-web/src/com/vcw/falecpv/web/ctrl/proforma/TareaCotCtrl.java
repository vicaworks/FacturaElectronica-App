/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.modelo.persistencia.Tareaetiqueta;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.TareacabeceraServicio;
import com.vcw.falecpv.core.servicio.TareaetiquetaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class TareaCotCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334408502335495024L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	@EJB
	private TareaetiquetaServicio tareaetiquetaServicio;
	
	@EJB
	private TareacabeceraServicio tareacabeceraServicio;
	
	
	private String callModule;
	private String callForm;
	private String updateView;
	private Cabecera cotizacionSelected;
	private List<Tareaetiqueta> tareaetiquetaList;
	private Tareacabecera tareacabeceraSelected = new Tareacabecera();
	

	/**
	 * 
	 */
	public TareaCotCtrl() {
		
	}

	@PostConstruct
	private void init() {
		try {
			establecimientoFacade(establecimientoServicio, false);
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void nuevaTarea() {
		try {
			tareacabeceraSelected = new Tareacabecera();
			tareacabeceraSelected.setUsuario(AppJsfUtil.getUsuario());
			tareacabeceraSelected.setCabecera(cotizacionSelected);
			tareacabeceraSelected.setEstado("PENDIENTE");
			tareacabeceraSelected.setPrioridad("BAJA");
			tareacabeceraSelected.setFechalimite(new Date());
			
			consultarEtiquetas();
			AppJsfUtil.showModalRender("dlgCotizacionTarea", "frmCotizacionTarea");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	

	public void consultarEtiquetas() throws DaoException {
		tareaetiquetaList = null;
		tareaetiquetaList = tareaetiquetaServicio.getEtiquetas(establecimientoMain.getEmpresa().getIdempresa());
	}
	
	@Override
	public void guardar() {
		try {
			
			if(FechaUtil.comparaFechas(new Date(), tareacabeceraSelected.getFechalimite())>0) {
				AppJsfUtil.addErrorMessage("frmCotizacionTarea:t-fecha", "", "NO PUEDE SER MENOR A LA ACTUAL");
				return;
			}
			
			// valor de la prioridad
			switch (tareacabeceraSelected.getPrioridad()) {
			case "IMPORTANTE":
				tareacabeceraSelected.setPrioridadvalor(1);
				break;
			case "ALTA":
				tareacabeceraSelected.setPrioridadvalor(2);
				break;
			case "MEDIA":
				tareacabeceraSelected.setPrioridadvalor(3);
				break;
			case "BAJA":
				tareacabeceraSelected.setPrioridadvalor(4);
				break;

			default:
				break;
			}
			
			tareacabeceraSelected.setIdEstablecimiento(cotizacionSelected.getEstablecimiento().getIdestablecimiento());
			if(tareacabeceraSelected.getIdtareacabecera()==null) {
				tareacabeceraServicio.crear(tareacabeceraSelected);
			}else {
				tareacabeceraServicio.actualizar(tareacabeceraSelected);
			}
			
			AppJsfUtil.addInfoMessage("frmCotizacionTarea", "OK", "REGISTRO GUARDADO CORRECTAMENTE.");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCotizacionTarea", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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

	/**
	 * @return the updateView
	 */
	public String getUpdateView() {
		return updateView;
	}

	/**
	 * @param updateView the updateView to set
	 */
	public void setUpdateView(String updateView) {
		this.updateView = updateView;
	}

	/**
	 * @return the cotizacionSelected
	 */
	public Cabecera getCotizacionSelected() {
		return cotizacionSelected;
	}

	/**
	 * @param cotizacionSelected the cotizacionSelected to set
	 */
	public void setCotizacionSelected(Cabecera cotizacionSelected) {
		this.cotizacionSelected = cotizacionSelected;
	}

	/**
	 * @return the tareaetiquetaList
	 */
	public List<Tareaetiqueta> getTareaetiquetaList() {
		return tareaetiquetaList;
	}

	/**
	 * @param tareaetiquetaList the tareaetiquetaList to set
	 */
	public void setTareaetiquetaList(List<Tareaetiqueta> tareaetiquetaList) {
		this.tareaetiquetaList = tareaetiquetaList;
	}

	/**
	 * @return the tareacabeceraSelected
	 */
	public Tareacabecera getTareacabeceraSelected() {
		return tareacabeceraSelected;
	}

	/**
	 * @param tareacabeceraSelected the tareacabeceraSelected to set
	 */
	public void setTareacabeceraSelected(Tareacabecera tareacabeceraSelected) {
		this.tareacabeceraSelected = tareacabeceraSelected;
	}
	
}
