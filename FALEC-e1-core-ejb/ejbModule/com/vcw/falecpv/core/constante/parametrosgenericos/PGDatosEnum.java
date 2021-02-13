/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGDatosEnum implements ParametroGenericoBaseEnum {

	DATOS_IVA("38"),
	DATOS_ICE("39");

	private String id;

	private PGDatosEnum(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}


}
