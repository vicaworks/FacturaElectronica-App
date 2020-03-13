/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCProducto implements TablaContadorBaseEnum {
	
	PRODUCTO("PRODUCTO");
	
	private String nombreTabla;

	private TCProducto(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
