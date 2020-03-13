/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCFabricante implements TablaContadorBaseEnum {
	
	FABRICANTE("FABRICANTE");
	
	private String nombreTabla;

	private TCFabricante(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
