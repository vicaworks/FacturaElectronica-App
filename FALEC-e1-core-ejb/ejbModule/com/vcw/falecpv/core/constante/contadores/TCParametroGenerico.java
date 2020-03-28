/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author isitk
 *
 */
public enum TCParametroGenerico implements TablaContadorBaseEnum {
	
	PARAMETROGENERICO("PARAMETROGENERICO");
	
	private String nombreTabla;

	private TCParametroGenerico(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
