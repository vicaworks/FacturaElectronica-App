/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.util.HashMap;

import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.helper.SriAccesoHelper;
import com.vcw.falecpv.core.modelo.dto.SriAccesoDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.sri.ws.ClienteWsSriServicio;
import com.vcw.sri.ws.autorizacion.RespuestaComprobante;

/**
 * @author cristianvillarreal
 *
 */
public class ComprobanteErrorSri extends EnviarComprobanteSRIDecorador {

	private CabeceraServicio cabeceraServicio;
	private SriAccesoHelper sriAccesoHelper;
	private EstablecimientoServicio establecimientoServicio;
	
	
	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobanteErrorSri(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		System.out.println("============== IMPL COMPROBANTE DEVUELTO CON ERRRORES ===============");
		
		cabeceraServicio = (CabeceraServicio)parametros.get("cabeceraServicio");
		sriAccesoHelper = (SriAccesoHelper)parametros.get("sriAccesoHelper");
		establecimientoServicio = (EstablecimientoServicio)parametros.get("establecimientoServicio");
		
		Cabecera c = (Cabecera) parametros.get("cabecera");
		
		// 1. consultar estado SRI
		RespuestaComprobante rs = null;
		try {
			
			Establecimiento e = establecimientoServicio.consultarByPk(c.getEstablecimiento().getIdestablecimiento());
			SriAccesoDto sriAccesoDto = sriAccesoHelper.consultarDatosAcceso("APROBACION", e.getAmbiente().equals("2"));
			ClienteWsSriServicio wsAutorizacion = new ClienteWsSriServicio(sriAccesoDto.getWsdl());
			rs = wsAutorizacion.autorizacionComprobanteFacade(c.getClaveacceso());
			HashMap<String, Object> resultado = new HashMap<>();
			if(rs.getNumeroComprobantes()!=null && Integer.parseInt(rs.getNumeroComprobantes())==0) {
				c.setEstado(ComprobanteEstadoEnum.PENDIENTE.toString());
				cabeceraServicio.actualizar(c);
				
			}else if(rs.getNumeroComprobantes()!=null && Integer.parseInt(rs.getNumeroComprobantes())==1) {
				c.setEstado(ComprobanteEstadoEnum.RECIBIDO_SRI.toString());
				cabeceraServicio.actualizar(c);
			}
			
			resultado.put("cabecera", c);
			resultado.put("ponerCola", true);
			
			return resultado;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new EnviarComprobanteSRIException(e);
		}
		
	}

}
