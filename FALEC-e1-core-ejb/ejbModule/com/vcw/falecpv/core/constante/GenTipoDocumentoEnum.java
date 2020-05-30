/**
 * 
 */
package com.vcw.falecpv.core.constante;

import java.util.stream.Stream;

/**
 * @author cristianvillarreal
 *
 */
public enum GenTipoDocumentoEnum {
	
	FACTURA("01"),RECIBO("00"),RETENCION("07"),NOTA_CREDITO(""),NOTA_DEBITO("");
	
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param identificador
	 * @return
	 */
	public static String getByIdentificador(String identificador) {
		GenTipoDocumentoEnum gt = Stream.of(GenTipoDocumentoEnum.values()).filter(x->x.getIdentificador().equals(identificador)).findFirst().get();
		if (gt!=null) {
			return gt.toString();
		}
		return "";
	}
	
	
}
