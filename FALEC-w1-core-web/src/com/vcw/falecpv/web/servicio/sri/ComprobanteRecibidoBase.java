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
public class ComprobanteRecibidoBase extends EnviarComprobanteSRIDecorador {

	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobanteRecibidoBase(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		
		System.out.println("============== IMPL COMPROBANTE RECIBIDO ===============");
		
		// 5. enviar sri
		
		// 6. cambiar estado
		
		return super.enviarComprobante(parametros);
	}
	
	

}
