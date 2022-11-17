/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Detalle;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class BuscarRemplazarCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1190160648198011005L;	
	
	private String callModule;
	private String callForm;
	private String updateView;
	private List<Detalle> detalleList;
	private String cadenaBuscar;
	private String cadenaRemplazar;
	private boolean palabraCompleta = false;	
	
	/**
	 * 
	 */
	public BuscarRemplazarCtrl() {
		
	}

	public void remplazar() {
		try {
			
			switch (callModule) {
			case "FACTURA":
				remplazarDetalle();
				break;

			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmFacRemplazar", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}	
	
	private void remplazarDetalle() {
		
		if(detalleList==null || detalleList.isEmpty()) return;
		
		for (Detalle det : detalleList) {
			if(!palabraCompleta) {
				det.setDescripcion(det.getDescripcion().replace(cadenaBuscar, cadenaRemplazar));
			}else {
				String cadenaFinal = "";
				for (String cadena : det.getDescripcion().split(" ")) {
					cadenaFinal += cadena.replace(cadenaBuscar, cadenaRemplazar) + " ";
				}
				det.setDescripcion(cadenaFinal);
			}
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
	 * @return the cadenaBuscar
	 */
	public String getCadenaBuscar() {
		return cadenaBuscar;
	}

	/**
	 * @param cadenaBuscar the cadenaBuscar to set
	 */
	public void setCadenaBuscar(String cadenaBuscar) {
		this.cadenaBuscar = cadenaBuscar;
	}

	/**
	 * @return the cadenaRemplazar
	 */
	public String getCadenaRemplazar() {
		return cadenaRemplazar;
	}

	/**
	 * @param cadenaRemplazar the cadenaRemplazar to set
	 */
	public void setCadenaRemplazar(String cadenaRemplazar) {
		this.cadenaRemplazar = cadenaRemplazar;
	}

	/**
	 * @return the palabraCompleta
	 */
	public boolean isPalabraCompleta() {
		return palabraCompleta;
	}

	/**
	 * @param palabraCompleta the palabraCompleta to set
	 */
	public void setPalabraCompleta(boolean palabraCompleta) {
		this.palabraCompleta = palabraCompleta;
	}

	/**
	 * @return the detalleList
	 */
	public List<Detalle> getDetalleList() {
		return detalleList;
	}

	/**
	 * @param detalleList the detalleList to set
	 */
	public void setDetalleList(List<Detalle> detalleList) {
		this.detalleList = detalleList;
	}
}
