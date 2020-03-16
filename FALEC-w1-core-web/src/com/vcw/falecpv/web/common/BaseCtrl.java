/**
 * 
 */
package com.vcw.falecpv.web.common;

import java.io.Serializable;

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
	
	
	
}
