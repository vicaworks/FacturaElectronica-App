/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author Jorge Toaza
 *
 */
public enum TCBanco implements TablaContadorBaseEnum {
	
	BANCOS("BANCOS");
	
	private String nombreTabla;

	private TCBanco(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
