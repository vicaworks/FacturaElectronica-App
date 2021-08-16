/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Adquisicion;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Pago;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.ctrl.adquisicion.AdquisicionFrmCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

import java.io.Serializable;
import java.util.List;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class PagoCompCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6522494094259594867L;
	
	@EJB
	private TipopagoServicio tipopagoServicio;

	
	private String callModule;
	private String callForm;
	private String updateView;
	private Cabecera cabeceraSelected;
	private Adquisicion adquisicionSelected;
	private List<Pago> pagoList;
	private List<Tipopago> tipoPagoList;
	
	
	// controlers
	private AdquisicionFrmCtrl adquisicionFrmCtrl;
	
	/**
	 * 
	 */
	public PagoCompCtrl() {
	}

	@PostConstruct
	private void init() {
		
	}
	
	private void consultarTipoPago() throws DaoException{
		tipoPagoList = null;
		tipoPagoList = tipopagoServicio.getALL();
	}
	
	public void loadPantalla() {
		try {
			
			switch (callModule) {
			case "ADQUISICION":
				
				cabeceraSelected = new Cabecera();
				cabeceraSelected.setCliente(adquisicionSelected.getCliente());
				cabeceraSelected.setTotalsinimpuestos(adquisicionSelected.getSubtotal());
				cabeceraSelected.setTotaldescuento(adquisicionSelected.getTotaldescuento());
				cabeceraSelected.setTotalice(adquisicionSelected.getTotalice());
				cabeceraSelected.setTotaliva(adquisicionSelected.getTotaliva());
				cabeceraSelected.setTotalconimpuestos(adquisicionSelected.getTotalfactura());
				
				
				break;

			default:
				break;
			}
			consultarTipoPago();
			tipoPagoList.get(0).setActivo(true);
			AppJsfUtil.showModalRender("dlgListaPago", "frmListPago");
			
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
	 * @return the adquisicionFrmCtrl
	 */
	public AdquisicionFrmCtrl getAdquisicionFrmCtrl() {
		return adquisicionFrmCtrl;
	}

	/**
	 * @param adquisicionFrmCtrl the adquisicionFrmCtrl to set
	 */
	public void setAdquisicionFrmCtrl(AdquisicionFrmCtrl adquisicionFrmCtrl) {
		this.adquisicionFrmCtrl = adquisicionFrmCtrl;
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
	 * @return the adquisicionSelected
	 */
	public Adquisicion getAdquisicionSelected() {
		return adquisicionSelected;
	}

	/**
	 * @param adquisicionSelected the adquisicionSelected to set
	 */
	public void setAdquisicionSelected(Adquisicion adquisicionSelected) {
		this.adquisicionSelected = adquisicionSelected;
	}

	/**
	 * @return the tipoPagoList
	 */
	public List<Tipopago> getTipoPagoList() {
		return tipoPagoList;
	}

	/**
	 * @param tipoPagoList the tipoPagoList to set
	 */
	public void setTipoPagoList(List<Tipopago> tipoPagoList) {
		this.tipoPagoList = tipoPagoList;
	}
	
}
