/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCCategoria implements TablaContadorBaseEnum {
	
	CATEGORIA("CATEGORIA");
	
	private String nombreTabla;

	private TCCategoria(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
