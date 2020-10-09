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
public class ComprobanteResultado extends EnviarComprobanteSRIDecorador {

	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobanteResultado(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		System.out.println("============== IMPL COMPROBANTE DEVUELTO CON ERRRORES ===============");
		
		// 1. armar xml
		
		// 2. validar 
		
		// 5. enviar sri
		
		// 6. cambiar de estado en respuesta al SRI
		
		
		
		return super.enviarComprobante(parametros);
	}
	
	

}
