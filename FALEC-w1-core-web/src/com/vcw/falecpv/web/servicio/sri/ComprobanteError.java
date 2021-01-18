/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.util.HashMap;

import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.CabeceraServicio;

/**
 * @author cristianvillarreal
 *
 */
public class ComprobanteError extends EnviarComprobanteSRIDecorador {

	private CabeceraServicio cabeceraServicio;
	
	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobanteError(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		try {
			cabeceraServicio = (CabeceraServicio)parametros.get("cabeceraServicio");
			Cabecera c = (Cabecera) parametros.get("cabecera");
			HashMap<String, Object> resultado = new HashMap<>();
			if(c.getEstado().equals(ComprobanteEstadoEnum.ERROR.toString())) {
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				cabeceraServicio.actualizar(c);
				resultado.put("ponerCola", true);
			}else {
				resultado.put("ponerCola", false);
			}
			resultado.put("cabecera", c);
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			throw new EnviarComprobanteSRIException(e);
		}
		
		
		
	}
	
	

}
