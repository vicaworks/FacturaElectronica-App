/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCEstablecimiento implements TablaContadorBaseEnum {
	
	ESTABLECIMIENTO("ESTABLECIMIENTO");
	
	private String nombreTabla;

	private TCEstablecimiento(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
