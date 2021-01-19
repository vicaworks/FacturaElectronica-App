/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Logtransferenciasri;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.LogtransferenciasriServicio;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class LogTransferCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1190160648198011005L;

	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private LogtransferenciasriServicio logtransferenciasriServicio;
	
	
	private String callModule;
	private String callForm;
	private String updateView;
	private Cabecera cabeceraSelected;
	private List<Logtransferenciasri> logtransferenciasriList;
	
	
	/**
	 * 
	 */
	public LogTransferCtrl() {
	}

	@Override
	public void buscar() {
		try {
			consultar(cabeceraSelected.getIdcabecera());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formLogTrans", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}

	
	private void consultar(String idcabecera) throws DaoException {
		cabeceraSelected = null;
		logtransferenciasriList = null;
		cabeceraSelected = cabeceraServicio.consultarByPk(idcabecera);
		logtransferenciasriList = logtransferenciasriServicio.getByIdCabecera(idcabecera);
	}
	
	
	public void cargarFormulario(String idCabecera) {
		try {
			
			consultar(idCabecera);
			
			if(logtransferenciasriList==null || logtransferenciasriList.isEmpty()) {
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "REGISTRO ERRORES TRANSFERENCIA SRI", msg.getString("mensaje.log.noexisteerror"));
				
		        PrimeFaces.current().dialog().showMessageDynamic(message,true);
		        
			}else {
				AppJsfUtil.showModalRender("dlLogTransferencia", "formLogTrans");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the cabeceraSelected
	 */
	public Cabecera getCabeceraSelected() {
		return cabeceraSelected;
	}

	/**
	 * @param cabeceraSelected the cabeceraSelected to set
	 */
	public void setCabeceraSelected(Cabecera cabeceraSelected) {
		this.cabeceraSelected = cabeceraSelected;
	}

	/**
	 * @return the logtransferenciasriList
	 */
	public List<Logtransferenciasri> getLogtransferenciasriList() {
		return logtransferenciasriList;
	}

	/**
	 * @param logtransferenciasriList the logtransferenciasriList to set
	 */
	public void setLogtransferenciasriList(List<Logtransferenciasri> logtransferenciasriList) {
		this.logtransferenciasriList = logtransferenciasriList;
	}

}
