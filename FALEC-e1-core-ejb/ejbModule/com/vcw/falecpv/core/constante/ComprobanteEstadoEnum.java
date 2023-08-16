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
	
	BORRADOR("label-message label-message--info"),//markBlue
	ERROR("label-message label-message--error"),//markRed
	PENDIENTE("label-message label-message--warning"),//markYellow
	RECIBIDO_SRI("label-message label-message--success"),//markGreen
	ERROR_SRI("label-message label-message--error"),//markRed
//	AUTORIZADO("markBlack"),
//	AUTORIZADO("markBlueOcean"),
	AUTORIZADO("label-message label-message--success"),//markGreen
	ANULADO("label-message label-message--anulado"),//markGray
	RETENCION("label-message label-message--warning"),//markOrange
	REGISTRADO("label-message label-message--warning"),//markGreen
	RECHAZADO_SRI("label-message label-message--error");//markRed
	
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
