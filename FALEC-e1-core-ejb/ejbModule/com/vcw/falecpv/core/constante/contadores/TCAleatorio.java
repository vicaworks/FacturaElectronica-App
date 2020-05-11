/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author Jorge Toaza
 *
 */
public enum TCAleatorio implements TablaContadorBaseEnum {
	
	ALEATORIOFACTURA("ALEATORIOFACTURA"),
	ALEATORIORECIBO("ALEATORIORECIBO"),
	ALEATORIONOTACREDITO("ALEATORIONOTACREDITO"),
	ALEATORIONOTADEBITO("ALEATORIONOTADEBITO"),
	ALEATORIOGUIAREMISION("ALEATORIOGUIAREMISION"),
	ALEATORIORETENCION("ALEATORIORETENCION");
	
	private String nombreTabla;

	private TCAleatorio(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
