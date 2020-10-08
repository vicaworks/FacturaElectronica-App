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
public class ComprobantePendiente extends EnviarComprobanteSRIDecorador {

	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobantePendiente(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		
		
		System.out.println("============== IMPL COMPROBANTE PENDIENTE ===============");
		
		// 1. armar xml
		
		// 2. validar 
		
		// 3. consultar ride
		
		// 4. enviar email
		
		// 5. enviar sri
		
		// 6. cambiar de estado en respuesta al SRI
		
		return super.enviarComprobante(parametros);
	}
	

}
