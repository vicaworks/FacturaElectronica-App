/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCAdquisicion implements TablaContadorBaseEnum {
	
	ADQUISICION("ADQUISICION"),
	ADQUISICIONDETALLE("ADQUISICIONDETALLE"),
	PAGODETALLE("PAGODETALLE");
	
	private String nombreTabla;

	private TCAdquisicion(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
