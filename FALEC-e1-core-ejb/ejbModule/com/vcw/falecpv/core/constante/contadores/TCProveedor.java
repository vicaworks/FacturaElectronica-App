/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCProveedor implements TablaContadorBaseEnum {
	
	PROVEEDOR("PROVEEDOR");
	
	private String nombreTabla;

	private TCProveedor(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
