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
	
	BORRADOR("markBlue"),
	ERROR("markRed"),
	PENDIENTE("markYellow"),
	RECIBIDO_SRI("markGreen"),
	ERROR_SRI("markRed"),
//	AUTORIZADO("markBlack"),
//	AUTORIZADO("markBlueOcean"),
	AUTORIZADO("markGreen"),
	ANULADO("markGray"),
	RETENCION("markOrange"),
	REGISTRADO("markGreen"),
	RECHAZADO_SRI("markRed");
	
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
		ComprobanteEstadoEnum estadoEnum = Stream.of(ComprobanteEstadoEnum.values()).filter(x->x.toString().equals(estado)).findFirst().orElse(null);
		if(estadoEnum!=null) {
			return estadoEnum;
		}
		return null;
	}

}
