/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGVentasPopUpEnum implements ParametroGenericoBaseEnum {
	
	GUIA_REMISION("1"),
	NOTA_DEBITO("2");
	
	private String id;
	
	
	private PGVentasPopUpEnum(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
