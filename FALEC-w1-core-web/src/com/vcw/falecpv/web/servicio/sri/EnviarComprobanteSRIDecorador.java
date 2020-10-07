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
public class EnviarComprobanteSRIDecorador implements EnviarComprobanteSRI {

	private EnviarComprobanteSRI enviarComprobanteSRI;
	
	/**
	 * 
	 */
	public EnviarComprobanteSRIDecorador(EnviarComprobanteSRI enviarComprobanteSRI) {
		this.enviarComprobanteSRI=enviarComprobanteSRI;
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		enviarComprobanteSRI.enviarComprobante(null);
		return null;
	}

}
