/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author Jorge Toaza
 *
 */
public enum TCCliente implements TablaContadorBaseEnum {
	
	CLIENTE("CLIENTE");
	
	private String nombreTabla;

	private TCCliente(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
