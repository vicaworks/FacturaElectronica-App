/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.vcw.falecpv.web.common.BaseCtrl;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class ConfiguracionCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4739887206473145827L;
	
	private String opcionRender = "PERFIL";
	

	/**
	 * 
	 */
	public ConfiguracionCtrl() {
		
	}
	
	public void switchOpcion(String opcion) {
		this.opcionRender = opcion;
	}

	/**
	 * @return the opcionRender
	 */
	public String getOpcionRender() {
		return opcionRender;
	}

	/**
	 * @param opcionRender the opcionRender to set
	 */
	public void setOpcionRender(String opcionRender) {
		this.opcionRender = opcionRender;
	}


}
