/**
 * 
 */
package com.vcw.falecpv.core.constante.contadores;

/**
 * @author cristianvillarreal
 *
 */
public enum TCComprobanteEnum implements TablaContadorBaseEnum {
	
	RETENCIONDETALLE("RETENCIONDETALLE"),
	CABECERA("CABECERA"),
	DETALLE("DETALLE"),
	DETALLEIMPUESTO("DETALLEIMPUESTO"),
	PAGO("PAGO"),
	TOTALIMPUESTO("TOTALIMPUESTO"),
	INFOADICIONAL("INFOADICIONAL"),
	DESTINATARIO("DESTINATARIO"),
	DETALLEDESTINATARIO("DETALLEDESTINATARIO"),
	MOTIVO("MOTIVO"),
	IMPUESTORETENCION("IMPUESTORETENCION");
	
	private String nombreTabla;

	private TCComprobanteEnum(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}

}
