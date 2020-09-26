/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author Jorge Toaza
 *
 */
public enum TCTransaccion implements TablaContadorBaseEnum {
	
	TRANSACCION("TRANSACCION"),TRANSACCION_TIPO("TRANSACCIONTIPO"),TRANSACCION_CONCEPTO("TRANSACCIONCONCEPTO");
	
	private String nombreTabla;

	private TCTransaccion(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
