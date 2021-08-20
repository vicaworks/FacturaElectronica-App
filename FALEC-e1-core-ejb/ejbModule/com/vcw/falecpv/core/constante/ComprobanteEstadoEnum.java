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
	
	BORRADOR("bg-blue-200 text-black"),//markBlue
	ERROR("bg-pink-200 text-black"),//markRed
	PENDIENTE("bg-yellow-100 text-black"),//markYellow
	RECIBIDO_SRI("bg-green-200 text-black"),//markGreen
	ERROR_SRI("bg-pink-200 text-black"),//markRed
//	AUTORIZADO("markBlack"),
//	AUTORIZADO("markBlueOcean"),
	AUTORIZADO("bg-green-200 text-black"),//markGreen
	ANULADO("bg-gray-200 text-black"),//markGray
	RETENCION("bg-orange-300 text-black"),//markOrange
	REGISTRADO("bg-green-200 text-black"),//markGreen
	RECHAZADO_SRI("bg-pink-200 text-black");//markRed
	
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
