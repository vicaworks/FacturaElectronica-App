/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.util.HashMap;

import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;

/**
 * @author cristianvillarreal
 *
 */
public class ComprobanteRecibido extends EnviarComprobanteSRIDecorador {

	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobanteRecibido(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		
		System.out.println("============== IMPL COMPROBANTE RECIBIDO ===============");
		
		return super.enviarComprobante(parametros);
	}
	
	

}
