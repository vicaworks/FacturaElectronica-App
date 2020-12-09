/**
 * 
 */
package com.vcw.falecpv.web.servicio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.web.servicio.sri.ComprobantePendiente;
import com.vcw.falecpv.web.servicio.sri.ComprobanteRecibidoBase;
import com.vcw.falecpv.web.servicio.sri.ComprobanteResultado;
import com.vcw.falecpv.web.servicio.sri.EnviarComprobanteSRI;
import com.vcw.falecpv.web.servicio.sri.ImplEnviarComprobanteSRI;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SriDispacher {

	@Inject
	private SriUtilServicio sriUtilServicio;
	
	// estados para enviar los documentos al SRI	
	private static final List<ComprobanteEstadoEnum> estadosEnviosri = Arrays.asList(new ComprobanteEstadoEnum[] {ComprobanteEstadoEnum.PENDIENTE,ComprobanteEstadoEnum.ERROR_SRI});
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param cabecera
	 * @throws EnviarComprobanteSRIException
	 * @throws DaoException 
	 */
	public void comprobanteSriDispacher(Cabecera cabecera)throws EnviarComprobanteSRIException, DaoException {
		
		// 1 validaciones iniciales del comprobante
		HashMap<String, Object> resultado = sriUtilServicio.validacionInicial(cabecera.getIdcabecera());
		if((boolean)resultado.get("valInicial")==false) {
			System.out.println(resultado.toString());
			return;
		}
		
		// 2. analizar los estados para enviar al SRI
		if(ComprobanteEstadoEnum.getByEstado(cabecera.getEstado())!=null && estadosEnviosri.contains(ComprobanteEstadoEnum.getByEstado(cabecera.getEstado()))) {
			
			// si existe constante estado y esta los estados para envio SRI
			
			EnviarComprobanteSRI enviarComprobanteSRI = new ImplEnviarComprobanteSRI();
			HashMap<String, Object> parametros = new HashMap<>(); 
			
			switch (ComprobanteEstadoEnum.getByEstado(cabecera.getEstado())) {
			case PENDIENTE:
				
				EnviarComprobanteSRI comprobantePendiente = new ComprobantePendiente(enviarComprobanteSRI);
				resultado = comprobantePendiente.enviarComprobante(parametros);
				break;
			case RECIBIDO_SRI:
				EnviarComprobanteSRI comprobanteRecibido = new ComprobanteRecibidoBase(enviarComprobanteSRI);
				resultado = comprobanteRecibido.enviarComprobante(parametros);
				break;
			case ERROR_SRI:
				EnviarComprobanteSRI omprobanteDevuelto = new ComprobanteResultado(enviarComprobanteSRI);
				resultado = omprobanteDevuelto.enviarComprobante(parametros);
				break;	
			default:
				break;
			}
			
		}
		
	}
	
}
