/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TipoComprobanteEnum {
	
	ADQUICIION("C"),FACTURACION("F"),RETENCION("R");
	
	private String sigla;
	
	private TipoComprobanteEnum(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}
	
	
}