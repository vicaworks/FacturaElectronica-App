/**
 * 
 */
package com.vcw.falecpv.web.ctrl.tools;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;

/**
 * 
 */
@Named
@ViewScoped
public class UtilsMakoCtrl implements Serializable {

	private static final long serialVersionUID = -2618886079233887714L;

	/**
	 * 
	 */
	public UtilsMakoCtrl() {
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estado
	 * @return
	 */
	public String getComprobanteStyle(String estado) {
		try {
			return ComprobanteEstadoEnum.getStyleEstado(estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
