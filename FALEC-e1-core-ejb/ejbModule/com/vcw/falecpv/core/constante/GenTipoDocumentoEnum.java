/**
 * 
 */
package com.vcw.falecpv.core.constante;

/**
 * @author cristianvillarreal
 *
 */
public enum GenTipoDocumentoEnum {
	
	FACTURA("01"),RECIBO("-1"),RETENCION(""),NOTA_CREDITO(""),NOTA_DEBITO("");
	
	private String identificador;
	

	private GenTipoDocumentoEnum(String identificador) {
		this.identificador = identificador;
	}
	
	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}
	
}
