/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCSri implements TablaContadorBaseEnum {
	
	ERRORSRI("ERRORSRI"),
	ESTADOSRI("ESTADOSRI"),
	LOGTRANSFERENCIASRI("LOGTRANSFERENCIASRI");
	
	private String nombreTabla;

	private TCSri(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
