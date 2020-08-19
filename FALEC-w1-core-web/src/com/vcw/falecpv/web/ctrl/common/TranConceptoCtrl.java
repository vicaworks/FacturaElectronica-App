/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.util.Date;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.TransaccionTipoEnum;
import com.vcw.falecpv.core.modelo.persistencia.Transaccionconcepto;
import com.vcw.falecpv.core.servicio.TransaccionconceptoServicio;
import com.vcw.falecpv.core.servicio.TransacciontipoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.ctrl.cajachica.CajaChicaCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class TranConceptoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1961007777883715224L;

	@EJB
	private TransacciontipoServicio transacciontipoServicio;
	
	@EJB
	private TransaccionconceptoServicio transaccionconceptoServicio;
	
	private String callModule;
	private String updateView;
	private Transaccionconcepto transaccionconcepto;
	private CajaChicaCtrl cajaChicaCtrl;
	
	/**
	 * 
	 */
	public TranConceptoCtrl() {
	}

	@Override
	public void guardar() {
		try {
			
			// validar nombre
			if (transaccionconceptoServicio.getTransaccionconceptoDao().existeConcepto(transaccionconcepto.getNombre(),
					transaccionconcepto.getIdtransaccionconcepto(),
					AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa())) {
				
				AppJsfUtil.addErrorMessage("frmTrxConcepto", "ERROR", "YA EXISTE.");
				return;
				
				
			}
			
			
			transaccionconcepto.setIdusuario(AppJsfUtil.getUsuario().getIdusuario());
			transaccionconcepto.setUpdated(new Date());
			transaccionconceptoServicio.guardarFacade(transaccionconcepto,AppJsfUtil.getEstablecimiento().getIdestablecimiento());
			
			boolean flag = false;
			
			switch (callModule) {
			case "CAJACHICA":
				cajaChicaCtrl.getTransaccionSelected().setTransaccionconcepto(transaccionconcepto);
				cajaChicaCtrl.consultarTransaccionConcepto();
				cajaChicaCtrl.consultar();
				cajaChicaCtrl.totalizar();
				break;

			default:
				break;
			}
			
			if(!flag) {
				AppJsfUtil.hideModal("dlgTrxConcepto");
			}else {
				AppJsfUtil.addInfoMessage("frmTrxConceptoCC", "OK", "REGISTRO GUARDADO CORRECTAMENTE");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmTrxConceptoCC", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void nuevo() {
		try {
			nuevaTransaccionConcepto();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("frmTrxConceptoCC", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void nuevaTransaccionConcepto() throws DaoException {
		switch (callModule) {
		case "CAJACHICA":
			
			transaccionconcepto = new Transaccionconcepto();
			transaccionconcepto.setEmpresa(AppJsfUtil.getEstablecimiento().getEmpresa());
			transaccionconcepto.setEstado(EstadoRegistroEnum.ACTIVO.getInicial());
			transaccionconcepto.setTransacciontipo(transacciontipoServicio.consultarByPk(TransaccionTipoEnum.CAJA_CHICA.getId()));
			
			break;

		default:
			break;
		}
	}
	
	public void nuevoEditar(String id) {
		try {
			
			if(id==null) {
				nuevaTransaccionConcepto();
			}else {
				transaccionconcepto = transaccionconceptoServicio.consultarByPk(id);
			}
			
			if(transaccionconcepto==null) {
				nuevaTransaccionConcepto();
			}
			
			AppJsfUtil.showModalRender("dlgTrxConcepto", "frmTrxConcepto");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	 * @return the transaccionconcepto
	 */
	public Transaccionconcepto getTransaccionconcepto() {
		return transaccionconcepto;
	}

	/**
	 * @param transaccionconcepto the transaccionconcepto to set
	 */
	public void setTransaccionconcepto(Transaccionconcepto transaccionconcepto) {
		this.transaccionconcepto = transaccionconcepto;
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
	 * @return the cajaChicaCtrl
	 */
	public CajaChicaCtrl getCajaChicaCtrl() {
		return cajaChicaCtrl;
	}

	/**
	 * @param cajaChicaCtrl the cajaChicaCtrl to set
	 */
	public void setCajaChicaCtrl(CajaChicaCtrl cajaChicaCtrl) {
		this.cajaChicaCtrl = cajaChicaCtrl;
	}
	

}
