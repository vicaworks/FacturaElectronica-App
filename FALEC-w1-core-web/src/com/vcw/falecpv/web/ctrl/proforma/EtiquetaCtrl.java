/**
 * 
 */
package com.vcw.falecpv.web.ctrl.proforma;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.modelo.persistencia.Etiqueta;
import com.vcw.falecpv.core.servicio.EtiquetaServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class EtiquetaCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1644243704111619373L;
	
	@EJB
	private EtiquetaServicio tareaetiquetaServicio;
	
	private Etiqueta tareaetiquetaSelected;
	private String callModule;
	private String callForm;
	private String updateView;
	private TareaCotCtrl tareaCotCtrl;
	private String etiquetaModulo;

	/**
	 * 
	 */
	public EtiquetaCtrl() {
	}
	
	public void agregarEtiqueta() {
		try {
			
			tareaetiquetaSelected = new Etiqueta();
			tareaetiquetaSelected.setIdEstablecimiento(establecimientoMain.getIdestablecimiento());
			tareaetiquetaSelected.setEmpresa(establecimientoMain.getEmpresa());
			tareaetiquetaSelected.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			tareaetiquetaSelected.setModulo(etiquetaModulo);
			
			AppJsfUtil.showModalRender("dlgCotizacionTareaEtqueta", "frmCotizacionTareaEtiqueta");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void guardar() {
		try {
			if(tareaetiquetaSelected.getEtiqueta()==null || tareaetiquetaSelected.getEtiqueta().trim().length()==0) {
				AppJsfUtil.addErrorMessage("frmCotizacionTareaEtiqueta:t-nuevaetiqueta", "", "REQUERIDO");
				return;
			}
			
			if (tareaetiquetaServicio.existeEtiquetas(establecimientoMain.getEmpresa().getIdempresa(), tareaetiquetaSelected.getIdetiqueta(),tareaetiquetaSelected.getEtiqueta())) {
				AppJsfUtil.addErrorMessage("frmCotizacionTareaEtiqueta:t-nuevaetiqueta", "", "YA EXISTE");
				return;
			}
			
			if(tareaetiquetaSelected.getIdetiqueta()!=null) {
				tareaetiquetaServicio.actualizar(tareaetiquetaSelected);
			}else {
				tareaetiquetaServicio.crear(tareaetiquetaSelected);
			}
			
			switch (callModule) {
			case "PROFORMA":
				tareaCotCtrl.getTareacabeceraSelected().setEtiqueta(tareaetiquetaSelected);
				tareaCotCtrl.consultarEtiquetas();
				break;
			case "PROFORMA_LISTA":
				CotizacionCtrl cotizacionCtrl = (CotizacionCtrl)AppJsfUtil.getManagedBean("cotizacionCtrl");
				cotizacionCtrl.consultarEtiquetas();
				cotizacionCtrl.getProformaSelected().setEtiqueta(tareaetiquetaSelected);
				break;
			default:
				break;
			}
			
			AppJsfUtil.hideModal("dlgCotizacionTareaEtqueta");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmCotizacionTareaEtiqueta", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	/**
	 * @return the tareaetiquetaSelected
	 */
	public Etiqueta getTareaetiquetaSelected() {
		return tareaetiquetaSelected;
	}

	/**
	 * @param tareaetiquetaSelected the tareaetiquetaSelected to set
	 */
	public void setTareaetiquetaSelected(Etiqueta tareaetiquetaSelected) {
		this.tareaetiquetaSelected = tareaetiquetaSelected;
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
	 * @return the tareaCotCtrl
	 */
	public TareaCotCtrl getTareaCotCtrl() {
		return tareaCotCtrl;
	}

	/**
	 * @param tareaCotCtrl the tareaCotCtrl to set
	 */
	public void setTareaCotCtrl(TareaCotCtrl tareaCotCtrl) {
		this.tareaCotCtrl = tareaCotCtrl;
	}

	/**
	 * @return the etiquetaModulo
	 */
	public String getEtiquetaModulo() {
		return etiquetaModulo;
	}

	/**
	 * @param etiquetaModulo the etiquetaModulo to set
	 */
	public void setEtiquetaModulo(String etiquetaModulo) {
		this.etiquetaModulo = etiquetaModulo;
	}

}
