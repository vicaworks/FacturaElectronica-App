/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCUsuario implements TablaContadorBaseEnum {
	
	USUARIO("USUARIO");
	
	private String nombreTabla;

	private TCUsuario(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
