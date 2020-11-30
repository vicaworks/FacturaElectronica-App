/**
 * 
 */
package com.vcw.falecpv.core.constante.parametrosgenericos;

/**
 * @author cristianvillarreal
 *
 */
public enum PGPlantillasEnum implements ParametroGenericoBaseEnum {

	PLANTILLA_FACTURA("16"), PLANTILLA_RETENCION("17"), PLANTILLA_NOTA_CREDITO("18"), PLANTILLA_NOTA_DEBITO("19"),
	PLANTILLA_GUIA_REMISION("20"), PLANTILLA_LIQ_COMPRA("21"),PATH_LOGO("22"),PATH_LOGO_BLANCO("23");

	private String id;

	private PGPlantillasEnum(String id) {
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
	public static PGPlantillasEnum getEnumPlantillaByIdentificador(String identificadorTipoComprobante) {

		switch (identificadorTipoComprobante) {
		case "01":
			// Factura
			return PGPlantillasEnum.PLANTILLA_FACTURA;
		case "07":
			// Retencion
			return PGPlantillasEnum.PLANTILLA_RETENCION;
		case "04":
			// Nota Credito
			return PGPlantillasEnum.PLANTILLA_NOTA_CREDITO;
		case "05":
			// Nota Debito
			return PGPlantillasEnum.PLANTILLA_NOTA_DEBITO;
		case "08":
			// Guia Remision
			return PGPlantillasEnum.PLANTILLA_GUIA_REMISION;
		case "03":
			// Liquidacion de compra
			return PGPlantillasEnum.PLANTILLA_LIQ_COMPRA;
		}

		return null;
	}

}