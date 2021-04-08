/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGenericos implements ParametroGenericoBaseEnum {
	
	PERFIL_PREDEFINIDO_DEFECTO("42");
	
	private String id;
	
	
	private PGenericos(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
