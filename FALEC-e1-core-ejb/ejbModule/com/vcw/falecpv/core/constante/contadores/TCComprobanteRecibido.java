/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author isitk
 *
 */
public enum TCComprobanteRecibido implements TablaContadorBaseEnum {
	
	COMPROBANTERECIBIDO("COMPROBANTERECIBIDO");
	
	private String nombreTabla;

	private TCComprobanteRecibido(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
