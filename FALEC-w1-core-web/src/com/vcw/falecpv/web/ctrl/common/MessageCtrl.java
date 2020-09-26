/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class MessageCtrl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2966983182951923428L;
	
	private String header;
	private String message;
	private String icono;

	/**
	 * 
	 */
	public MessageCtrl() {
	}

	public void cargarMenssage(String header,String message,String icono) {
		this.header = header;
		this.message = message;
		this.icono = icono;
		AppJsfUtil.showModalRender("dlMessage", "formMessage");
	}
	
	public void hideMenssage() {
		AppJsfUtil.hideModal("dlMessage");
	}
	
	/**
	 * @return the header
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the icono
	 */
	public String getIcono() {
		return icono;
	}

	/**
	 * @param icono the icono to set
	 */
	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	

}
