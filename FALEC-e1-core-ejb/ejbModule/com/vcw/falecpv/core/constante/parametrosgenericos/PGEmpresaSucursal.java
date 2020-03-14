/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGEmpresaSucursal implements ParametroGenericoBaseEnum {
	
	GENERAR_SUCURSAL("1"),
	SUCURSAL("2");
	
	private String id;
	
	
	private PGEmpresaSucursal(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
