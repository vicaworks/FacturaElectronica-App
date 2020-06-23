/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.TipoPagoFormularioEnum;
import com.vcw.falecpv.web.ctrl.liqcompra.LiqCompraFormCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class TipoPagoCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 909907911431293100L;

	
	private String callModule;
	private String updateView;
	private String callForm;
	private BigDecimal total = BigDecimal.ZERO;
	
	
	
	/**
	 * 
	 */
	public TipoPagoCtrl() {
	}

	
	public void showModal() {
		try {
			
			if(total.doubleValue()>0d) {
				AppJsfUtil.showModalRender("dlgListaTipoPago", "frmOpcTipoPago");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage(callForm, "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void aplicarPago(TipoPagoFormularioEnum tipoPagoFormularioEnum) {
		try {
			
			switch (callModule) {
			case "LIQCOMPRA":
				LiqCompraFormCtrl liqCompraFormCtrl = (LiqCompraFormCtrl) AppJsfUtil.getManagedBean("liqCompraFormCtrl");
				liqCompraFormCtrl.aplicarPago(tipoPagoFormularioEnum);
				break;

			default:
				break;
			}
			
			AppJsfUtil.hideModal("dlgListaTipoPago");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmOpcTipoPago", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
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
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
