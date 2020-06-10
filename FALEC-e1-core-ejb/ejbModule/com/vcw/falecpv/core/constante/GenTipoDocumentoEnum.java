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
	
	FACTURA("01","FAC"),RECIBO("00","REC"),RETENCION("07","RET"),NOTA_CREDITO("04","NC"),NOTA_DEBITO("","ND"),GUIA_REMISION("08","GR");
	
	private String identificador;
	private String inicial;
	
	private GenTipoDocumentoEnum(String identificador, String inicial) {
		this.identificador = identificador;
		this.inicial = inicial;
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
	
	/**
	 * @param identificador
	 * @return
	 */
	public static GenTipoDocumentoEnum getEnumByIdentificador(String identificador) {
		GenTipoDocumentoEnum gt = Stream.of(GenTipoDocumentoEnum.values()).filter(x->x.getIdentificador().equals(identificador)).findFirst().get();
		if (gt!=null) {
			return gt;
		}
		return null;
	}
	
	public static String getinicialByIdentificador(String identificador) {
		GenTipoDocumentoEnum gt = Stream.of(GenTipoDocumentoEnum.values()).filter(x->x.getIdentificador().equals(identificador)).findFirst().get();
		if (gt!=null) {
			return gt.getInicial();
		}
		return "";
	}

	/**
	 * @return the inicial
	 */
	public String getInicial() {
		return inicial;
	}
	
	
}
