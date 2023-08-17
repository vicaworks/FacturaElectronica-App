/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCGrupoCategoria implements TablaContadorBaseEnum {
	
	GRUPO_CATEGORIA("GRUPO_CATEGORIA");
	
	private String nombreTabla;

	private TCGrupoCategoria(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
