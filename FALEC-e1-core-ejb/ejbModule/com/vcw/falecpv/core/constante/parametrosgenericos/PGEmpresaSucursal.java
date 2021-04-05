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
	SUCURSAL("2"),
	ESTADO_BORRADOR("3"),
	PLANTILLA_FACTURA("4"),
	PLANTILLA_RETENCION("5"),
	PLANTILLA_NOTA_CREDITO("6"),
	PLANTILLA_NOTA_DEBITO("7"),
	PLANTILLA_GUIA_REMISION("8"),
	PLANTILLA_COTIZACION("28"),
	PLANTILLA_LIQ_COMPRA("9"),
	PLANTILLA_RECIBO("10"),
	PERMISO_MODIFICACION("22");
	
	private String id;
	
	
	private PGEmpresaSucursal(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param identificadorTipoComprobante
	 * @return
	 */
	public static PGEmpresaSucursal getEnumPlantillaByIdentificador(String identificadorTipoComprobante) {
		
		
		switch (identificadorTipoComprobante) {
		case "01":
			// Factura
			return PGEmpresaSucursal.PLANTILLA_FACTURA;
		case "07":
			// Retencion
			return PGEmpresaSucursal.PLANTILLA_RETENCION;
		case "04":
			// Nota Credito
			return PGEmpresaSucursal.PLANTILLA_NOTA_CREDITO;
		case "05":
			// Nota Debito
			return PGEmpresaSucursal.PLANTILLA_NOTA_DEBITO;
		case "06":
			// Guia Remision
			return PGEmpresaSucursal.PLANTILLA_GUIA_REMISION;
		case "03":
			// Liquidacion de compra
			return PGEmpresaSucursal.PLANTILLA_LIQ_COMPRA;
		case "00":
			// Recibo			
			return PGEmpresaSucursal.PLANTILLA_RECIBO;
		case "99":
			// Recibo			
			return PGEmpresaSucursal.PLANTILLA_COTIZACION;
		}
		
		return null;
	}
	
}
