/**
 * 
 */
package com.vcw.falecpv.web.ctrl.common;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * 
 */
@Named
@SessionScoped
public class MessageCommonCtrl implements Serializable {

	private static final long serialVersionUID = 5227877393881106116L;
	
	public enum Message{INFO,OK,ERROR,WARNING};
	
	protected static final String ICONO_INFO = "fas fa-info ms-info";
	protected static final String ICONO_OK = "fa-regular fa-circle-check ms-ok";
	protected static final String ICONO_ERROR = "fas fa-solid fa-ban ms-error";
	protected static final String ICONO_WARNING = "fa-solid fa-triangle-exclamation ms-warning";
	
	private String mensaje;
	private String titulo;
	private String icono;
	
	/**
	 * 
	 */
	public MessageCommonCtrl() {
		
	}
	
	public void loadMessage_action() {
		AppJsfUtil.showModalRender("dlgCommonMessage","dlgCommonMessage_main");
	}
	
	public void crearMensaje(String titulo,String mensaje,Message message) {
		this.titulo = titulo;
		this.mensaje = mensaje;
		switch (message) {
		case OK:
			this.icono = ICONO_OK;
			break;
		case ERROR:
			this.icono = ICONO_ERROR;	
			break;
		case WARNING:
			this.icono = ICONO_WARNING;
			break;
		case INFO:
			this.icono = ICONO_INFO;
			break;

		default:
			break;
		}
		loadMessage_action();
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
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
