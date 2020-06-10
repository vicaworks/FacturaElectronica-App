/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author isitk
 *
 */
public enum TCParametroEmpresa implements TablaContadorBaseEnum {
	
	PARAMETROEMPRESA("PARAMETROEMPRESA");
	
	private String nombreTabla;

	private TCParametroEmpresa(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
