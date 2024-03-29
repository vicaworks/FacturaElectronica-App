/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCEmpresa implements TablaContadorBaseEnum {
	
	EMPRESA("EMPRESA"),
	PARAMETRO_EMPRESA("PARAMETROEMPRESA"),
	PERFIL_USUARIO("SEGPERFILUSUARIO"),
	TAREA_ETIQUETA("ETIQUETA"),
	TAREA_CABECERA("TAREACABECERA");
	
	private String nombreTabla;

	private TCEmpresa(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
