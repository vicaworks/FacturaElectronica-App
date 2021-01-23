/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGJobEnum implements ParametroGenericoBaseEnum {

	SRI_MINUTOS("26"),
	SRI_JOB_HABILITADO("27"),
	SRI_ESTADOS("28"),
	SRI_MINUTOS_AUTORIZACION("29");

	private String id;

	private PGJobEnum(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}


}
