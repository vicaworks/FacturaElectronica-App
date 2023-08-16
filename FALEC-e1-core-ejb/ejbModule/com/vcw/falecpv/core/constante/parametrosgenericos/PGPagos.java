/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGPagos implements ParametroGenericoBaseEnum {
	
	REPORTE_PAGOS_NOINCLUIR("9"),
	PAGOS_TRANSFERENCIA("44"),
	PAGOS_EFECTIVO("43"),
	PAGOS_TARJETA("45"),
	PAGOS_CREDITO("46");
	
	private String id;
	
	
	private PGPagos(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
