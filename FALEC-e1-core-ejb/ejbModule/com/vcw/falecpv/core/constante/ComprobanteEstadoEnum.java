/**
 * 
 */
package com.vcw.falecpv.core.constante;

import java.util.stream.Stream;

/**
 * @author cristianvillarreal
 *
 */
public enum ComprobanteEstadoEnum {
	
	BORRADOR("markGreen"),
	ERROR("markRed"),
	PENDIENTE("markGreen"),
	RECIBIDO("markGreen"),
	ERROR_SRI("markRed"),
	AUTORIZADO("markBlack"),
	ANULADO("markBlack"),
	RETENCION("markOrange"),
	REGISTRADO("markGreen");
	
	private String style;

	private ComprobanteEstadoEnum(String style) {
		this.style = style;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estado
	 * @return
	 */
	public static String getStyleEstado(String estado) {
		ComprobanteEstadoEnum estadoEnum = Stream.of(ComprobanteEstadoEnum.values()).filter(x->x.toString().equals(estado)).findFirst().get();
		if(estadoEnum!=null) {
			return estadoEnum.getStyle();
		}
		return "";
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param estado
	 * @return
	 */
	public static ComprobanteEstadoEnum getByEstado(String estado) {
		ComprobanteEstadoEnum estadoEnum = Stream.of(ComprobanteEstadoEnum.values()).filter(x->x.toString().equals(estado)).findFirst().get();
		if(estadoEnum!=null) {
			return estadoEnum;
		}
		return null;
	}

}
