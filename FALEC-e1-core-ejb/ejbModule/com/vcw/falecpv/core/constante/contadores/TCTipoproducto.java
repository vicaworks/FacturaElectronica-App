/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCTipoproducto implements TablaContadorBaseEnum {
	
	TIPOPRODUCTO("TIPOPRODUCTO");
	
	private String nombreTabla;

	private TCTipoproducto(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
