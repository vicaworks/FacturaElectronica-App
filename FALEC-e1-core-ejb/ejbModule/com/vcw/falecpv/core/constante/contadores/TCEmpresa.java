/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCEmpresa implements TablaContadorBaseEnum {
	
	EMPRESA("EMPRESA");
	
	private String nombreTabla;

	private TCEmpresa(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
