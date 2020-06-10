/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCKardexproducto implements TablaContadorBaseEnum {
	
	KARDEXPRODUCTO("KARDEXPRODUCTO");
	
	private String nombreTabla;

	private TCKardexproducto(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
