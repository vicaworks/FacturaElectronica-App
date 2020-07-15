/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;

import javax.faces.application.FacesMessage;

import org.primefaces.PrimeFaces;

import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.helper.ComprobanteHelper;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
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
	
	protected MessageWebUtil msg = new MessageWebUtil();
	protected String estado;
	
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
	
}
