/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGXsdValidacionEnum implements ParametroGenericoBaseEnum {
	
	XSD_FACTURA("3"),
	XSD_LIQUIDACION_COMPRA("5"),
	XSD_NOTA_DEBITO("7"),
	XSD_NOTA_CREDITO("6"),
	XSD_RETENCION("4"),
	XSD_GUIA_REMISION("8");
	
	private String id;
	
	
	private PGXsdValidacionEnum(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
