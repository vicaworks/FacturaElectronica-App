/**
 * 
 */
package com.vcw.falecpv.core.helper;

import java.io.Serializable;

import com.vcw.falecpv.core.constante.EstadoRegistroEnum;

/**
 * @author phidalgo
 *
 */
public class EstatusHelper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4878908862250808890L;

	/**
	 * 
	 */
	public EstatusHelper() {
	}

	/**
	 * @param palabra
	 * @return
	 */
	public static EstadoRegistroEnum getByPalabra(String palabra){
		switch (palabra) {
		case "Activo":
			
			return EstadoRegistroEnum.ACTIVO;

		default:
			return EstadoRegistroEnum.INACTIVO;
		}
	}
	
	/**
	 * @param inicial
	 * @return
	 */
	public static EstadoRegistroEnum getByInicial(String inicial){
		switch (inicial) {
		case "A":
			
			return EstadoRegistroEnum.ACTIVO;

		default:
			return EstadoRegistroEnum.INACTIVO;
		}
	}
	
	/**
	 * @param inicial
	 * @return
	 */
	public static EstadoRegistroEnum getByInteger(Integer estado){
		switch (estado) {
		case 1:
			
			return EstadoRegistroEnum.ACTIVO;

		default:
			return EstadoRegistroEnum.INACTIVO;
		}
	}
	
	public static String pintarEstado(EstadoRegistroEnum estadoRegistroEnum){
		switch (estadoRegistroEnum) {
		case ACTIVO:
			
			return "ACT";

		default:
			return "INA";
		}
	}
	
}
