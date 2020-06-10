/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCGuiaRemision implements TablaContadorBaseEnum {
	
	TRANSPORTISTA("TRANSPORTISTA");
	
	private String nombreTabla;

	private TCGuiaRemision(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
