/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TipoPagoEnum {
	
	ADQUISICION("C"),FACTURACION("F");
	
	private String formulario;
	
	private TipoPagoEnum(String formulario) {
		this.formulario = formulario;
	}

	/**
	 * @return the formulario
	 */
	public String getFormulario() {
		return formulario;
	}
	
}
