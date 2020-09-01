/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

import org.primefaces.PrimeFaces;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Infoadicional;
import com.vcw.falecpv.core.modelo.persistencia.Tipopago;
import com.vcw.falecpv.core.servicio.TipopagoServicio;
import com.vcw.falecpv.web.ctrl.common.MessageCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;
import com.vcw.falecpv.web.util.MessageWebUtil;

/**
 * @author cristianvillarreal
 *
 */
public abstract class BaseCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6523368854199739151L;
	@EJB
	private TipopagoServicio tipopagoServicio;
	
	protected MessageWebUtil msg = new MessageWebUtil();
	protected String estado;
	protected List<Infoadicional> infoadicionalList;
	protected Infoadicional infoadicionalSelected;
	private List<Tipopago> tipopagoList;
	private Tipopago tipopagoSelected;
	protected MessageCtrl messageCtrl = (MessageCtrl) AppJsfUtil.getManagedBean("messageCtrl");
	
	/**
	 * 
	 */
	public BaseCtrl() {
		
	}
	
	public void limpiar() {
	}
	
	public void buscar() {
	}
	
	public void refrescar() {
	}
	
	public void eliminar() {
	}
	
	public void guardar() {
	}
	
	public void editar() {
	}
	
	public void nuevo() {
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void inicializarSecuencia(Cabecera cabecera) {
		cabecera.setEditarSecuencial(false);
		cabecera.setSecuencialEstablecimiento(TextoUtil.leftPadTexto(AppJsfUtil.getEstablecimiento().getCodigoestablecimiento(),3,"0"));
		cabecera.setSecuencialCaja(TextoUtil.leftPadTexto(AppJsfUtil.getEstablecimiento().getPuntoemision(),3,"0"));
		cabecera.setSecuencialNumero(TextoUtil.leftPadTexto("0",9,"0"));
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void editarSecuencial(Cabecera cabecera,String focus) {
			
			cabecera.setEditarSecuencial(true);
			
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "IMPORTANTE",
				"Al asignar un nuevo secuencial del documento, el sistema valida que no se haya utilizado anteriormente, posterior a usar esta opción se debe actulizar la secuencia, en configuración establecimiento, para que el sistema continúe, generando automáticamnete.");
	        PrimeFaces.current().dialog().showMessageDynamic(message,true);
	        AppJsfUtil.executeJavaScript("PrimeFaces.focus('" + focus + "');");
			
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 */
	protected void noEditarSecuencial(Cabecera cabecera) {
			
		cabecera.setEditarSecuencial(false);
		cabecera.setSecuencialNumero(TextoUtil.leftPadTexto("0", 7, "0"));
			
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param numDoc
	 * @return
	 */
	public String getFormatoNumDocumento(String numDoc) {
		return ComprobanteHelper.formatNumDocumento(numDoc);
	}
	
	public void agregarInfoAdicional(){
		try {
			
			if(infoadicionalList==null) {
				infoadicionalList = new ArrayList<>();
			}
			
			Infoadicional info = new Infoadicional();
			infoadicionalList.add(info);
			
			int cont = 0;
			for (Infoadicional infoadicional : infoadicionalList) {
				infoadicional.setIdinfoadicional("M"+cont++);
			}
			
			AppJsfUtil.executeJavaScript("PrimeFaces.focus('formMain:j_idt181:"  + (infoadicionalList.size()-1) + ":inpInfoAdicionalNombre')");
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void eliminarInfoAdicional() {
		try {
			if(infoadicionalList!=null && infoadicionalSelected!=null) {
				infoadicionalList.remove(infoadicionalSelected);
			}
			
			infoadicionalSelected = null;
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void populateTipoPago() throws DaoException {
		tipopagoList = null;
		tipopagoList = tipopagoServicio.getALL();
		tipopagoSelected = null;
		if(tipopagoList.size()>0) {
			tipopagoSelected = tipopagoList.get(0);
		}
	}
	
	/**
	 * @return the infoadicionalSelected
	 */
	public Infoadicional getInfoadicionalSelected() {
		return infoadicionalSelected;
	}

	/**
	 * @param infoadicionalSelected the infoadicionalSelected to set
	 */
	public void setInfoadicionalSelected(Infoadicional infoadicionalSelected) {
		this.infoadicionalSelected = infoadicionalSelected;
	}

	/**
	 * @return the infoadicionalList
	 */
	public List<Infoadicional> getInfoadicionalList() {
		return infoadicionalList;
	}

	/**
	 * @param infoadicionalList the infoadicionalList to set
	 */
	public void setInfoadicionalList(List<Infoadicional> infoadicionalList) {
		this.infoadicionalList = infoadicionalList;
	}

	/**
	 * @return the tipopagoList
	 */
	public List<Tipopago> getTipopagoList() {
		return tipopagoList;
	}

	/**
	 * @param tipopagoList the tipopagoList to set
	 */
	public void setTipopagoList(List<Tipopago> tipopagoList) {
		this.tipopagoList = tipopagoList;
	}

	/**
	 * @return the tipopagoSelected
	 */
	public Tipopago getTipopagoSelected() {
		return tipopagoSelected;
	}

	/**
	 * @param tipopagoSelected the tipopagoSelected to set
	 */
	public void setTipopagoSelected(Tipopago tipopagoSelected) {
		this.tipopagoSelected = tipopagoSelected;
	}

	/**
	 * @return the tipopagoServicio
	 */
	public TipopagoServicio getTipopagoServicio() {
		return tipopagoServicio;
	}

	/**
	 * @param tipopagoServicio the tipopagoServicio to set
	 */
	public void setTipopagoServicio(TipopagoServicio tipopagoServicio) {
		this.tipopagoServicio = tipopagoServicio;
	}

	/**
	 * @return the msg
	 */
	public MessageWebUtil getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(MessageWebUtil msg) {
		this.msg = msg;
	}

	/**
	 * @return the messageCtrl
	 */
	public MessageCtrl getMessageCtrl() {
		return messageCtrl;
	}

	/**
	 * @param messageCtrl the messageCtrl to set
	 */
	public void setMessageCtrl(MessageCtrl messageCtrl) {
		this.messageCtrl = messageCtrl;
	}
	
}
