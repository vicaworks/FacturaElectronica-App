/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author Jorge Toaza
 *
 */
public enum TCRetencion implements TablaContadorBaseEnum {
	
	RETENCION_IMPUESTO("RETENCIONIMPUESTO"),
	RETENCION_IMPUESTO_DETALLE("RETENCIONIMPUESTODET");
	
	private String nombreTabla;

	private TCRetencion(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
