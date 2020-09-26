/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGPagos implements ParametroGenericoBaseEnum {
	
	REPORTE_PAGOS_NOINCLUIR("9");
	
	private String id;
	
	
	private PGPagos(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
