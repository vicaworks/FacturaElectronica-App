/**
 * 
 */
package com.vcw.falecpv.core.constante;

import java.util.stream.Stream;

/**
 * @author cristianvillarreal
 *
 */
public enum TipoPagoFormularioEnum {
	
	CHEQUE("CHQ",true), EFECTIVO("EFEC",false), TARJETA_CREDITO("TC",true), TARJETA_DEBITO("TD",true), CREDITO("CRE",true), TRANSFERENCIA("TRAN",true),
	DEPOSITO("DEP",true);
	
	private String codInterno;
	private boolean repetir;

	

	private TipoPagoFormularioEnum(String codInterno, boolean repetir) {
		this.codInterno = codInterno;
		this.repetir = repetir;
	}

	/**
	 * @return the codInterno
	 */
	public String getCodInterno() {
		return codInterno;
	}

	/**
	 * @return the repetir
	 */
	public boolean isRepetir() {
		return repetir;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param nombre
	 * @return
	 */
	public static TipoPagoFormularioEnum getByName(String nombre) {
		return Stream.of(TipoPagoFormularioEnum.values()).filter(x->x.toString().equals(nombre)).findFirst().get();
	}
	
	

}
