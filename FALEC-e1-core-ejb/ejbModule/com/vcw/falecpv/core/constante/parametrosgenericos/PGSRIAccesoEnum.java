/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGSRIAccesoEnum implements ParametroGenericoBaseEnum {
	
	SRI_WSDL_APROBACION_PRODUCCION("10"),
	SRI_WSDL_APROBACION_PRUEBAS("12"),
	SRI_WSDL_RECEPCION_PRODUCCION("11"),
	SRI_WSDL_RECEPCION_PRUEBAS("13"),
	SRI_HOST_PRUEBAS("15"),
	SRI_HOST_PRODUCCION("14");
	
	private String id;
	
	
	private PGSRIAccesoEnum(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

}
