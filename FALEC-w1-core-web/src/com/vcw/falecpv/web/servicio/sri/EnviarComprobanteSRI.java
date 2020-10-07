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
public interface EnviarComprobanteSRI {

	/**
	 * @author cristianvillarreal
	 * 
	 * @param parametros
	 * @return
	 * @throws EnviarComprobanteSRIException
	 */
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)throws EnviarComprobanteSRIException;
	
}
