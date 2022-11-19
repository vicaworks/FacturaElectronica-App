/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.util.Date;
import java.util.HashMap;

import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TipoComprobanteEnum;
import com.vcw.falecpv.core.exception.EnviarComprobanteSRIException;
import com.vcw.falecpv.core.helper.SriAccesoHelper;
import com.vcw.falecpv.core.modelo.dto.SriAccesoDto;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Errorsri;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Estadosri;
import com.vcw.falecpv.core.modelo.persistencia.Logtransferenciasri;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.vcw.falecpv.core.servicio.ErrorsriServicio;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;
import com.vcw.falecpv.core.servicio.EstadosriServicio;
import com.vcw.falecpv.core.servicio.LogtransferenciasriServicio;
import com.vcw.falecpv.web.servicio.SriUtilServicio;
import com.vcw.falecpv.web.servicio.emailcomprobante.EmailComprobanteServicio;
import com.vcw.sri.ws.ClienteWsSriServicio;
import com.vcw.sri.ws.autorizacion.Autorizacion;
import com.vcw.sri.ws.autorizacion.Mensaje;
import com.vcw.sri.ws.autorizacion.RespuestaComprobante;

/**
 * @author cristianvillarreal
 *
 */
@SuppressWarnings("unused")
public class ComprobanteRecibido extends EnviarComprobanteSRIDecorador {
	
	private CabeceraServicio cabeceraServicio;
	private EstadosriServicio estadosriServicio;
	private SriUtilServicio sriUtilServicio;
	private SriAccesoHelper sriAccesoHelper;
	private EstablecimientoServicio establecimientoServicio;
	private ErrorsriServicio errorsriServicio;
	private EmailComprobanteServicio emailComprobanteServicio;
	
	
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
		
		cabeceraServicio = (CabeceraServicio)parametros.get("cabeceraServicio");
		logtransferenciasriServicio = (LogtransferenciasriServicio)parametros.get("logtransferenciasriServicio");
		contadorPkServicio = (ContadorPkServicio)parametros.get("contadorPkServicio");
		estadosriServicio = (EstadosriServicio)parametros.get("estadosriServicio");
		sriUtilServicio = (SriUtilServicio)parametros.get("sriUtilServicio");
		sriAccesoHelper = (SriAccesoHelper)parametros.get("sriAccesoHelper");
		establecimientoServicio = (EstablecimientoServicio)parametros.get("establecimientoServicio");
		errorsriServicio = (ErrorsriServicio)parametros.get("errorsriServicio");
		emailComprobanteServicio = (EmailComprobanteServicio)parametros.get("emailComprobanteServicio");
		
		Cabecera c = (Cabecera) parametros.get("cabecera");
		
		// 1. consultar estado SRI
		RespuestaComprobante rs = null;
		try {
			
			Establecimiento e = establecimientoServicio.consultarByPk(c.getEstablecimiento().getIdestablecimiento());
			SriAccesoDto sriAccesoDto = sriAccesoHelper.consultarDatosAcceso("APROBACION", e.getAmbiente().equals("2"));
			ClienteWsSriServicio wsAutorizacion = new ClienteWsSriServicio(sriAccesoDto.getWsdl());
			rs = wsAutorizacion.autorizacionComprobanteFacade(c.getClaveacceso());
			// 1. revisiar que si exista la clave de acceso
			if(rs.getNumeroComprobantes()!=null && Integer.parseInt(rs.getNumeroComprobantes())==1) {
				
				Autorizacion autorizacion = rs.getAutorizaciones().getAutorizacion().get(0);
				
				if(autorizacion.getEstado().equals("AUTORIZADO")) {
					// AUTORIZADO
					c.setEstado(ComprobanteEstadoEnum.AUTORIZADO.toString());
					c.setFechaautorizacion(autorizacion.getFechaAutorizacion().toGregorianCalendar().getTime());
					c.setNumeroautorizacion(autorizacion.getNumeroAutorizacion());
					c.setEstadoautorizacion("SI");
					// envar email
					if(enviarEmail(c)) {
						emailComprobanteServicio.enviarComprobanteFacade(null, null, null, c.getIdcabecera(), c, null, null, null,true);						
					}
				}else {
					// RECHAZADO
					c.setEstado(ComprobanteEstadoEnum.RECHAZADO_SRI.toString());
					c.setEstadoautorizacion("NO");
					Mensaje m = autorizacion.getMensajes().getMensaje().get(0);
					Logtransferenciasri lt = new Logtransferenciasri();
					lt.setCabecera(c);
					Estadosri estadoSri = estadosriServicio.consultarByPk("6");
					Errorsri errorsri = errorsriServicio.getErrorsriDao().getByCodErrorSri(m.getIdentificador());
					if(estadoSri==null) {
						estadoSri = new Estadosri();
						estadoSri.setIdestadosri("0");
					}
					lt.setEstadosri(estadoSri);
					lt.setEtiqueta("ERROR SRI");
					lt.setFecha(new Date());
					lt.setIdusuario(c.getIdusuario());
					lt.setMotivo((errorsri!=null? errorsri.getMotivo() + ": " :"") + m.getMensaje() + " " + m.getInformacionAdicional());
					lt.setDescripcion((errorsri!=null?errorsri.getDescripcion()+" : ":"") + m.getMensaje());
					super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
					
				}
				cabeceraServicio.actualizar(c);
				
			}else if(rs.getNumeroComprobantes()!=null && Integer.parseInt(rs.getNumeroComprobantes())==0) {
				// NO existe la clave de aceso 
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = estadosriServicio.consultarByPk("4");
				if(estadoSri==null) {
					estadoSri = new Estadosri();
					estadoSri.setIdestadosri("0");
				}
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR SRI");
				lt.setFecha(new Date());
				lt.setIdusuario(c.getIdusuario());
				lt.setMotivo("No exste el comprobante");
				lt.setDescripcion("La clave de acceso de comprobante no exste en el SRI.");
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			}else {
				
				Autorizacion autorizacion = rs.getAutorizaciones().getAutorizacion().get(0);
				Mensaje m = autorizacion.getMensajes().getMensaje().get(0);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = estadosriServicio.consultarByPk("6");
				Errorsri errorsri = errorsriServicio.getErrorsriDao().getByCodErrorSri(m.getIdentificador());
				if(estadoSri==null) {
					estadoSri = new Estadosri();
					estadoSri.setIdestadosri("0");
				}
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR SRI");
				lt.setFecha(new Date());
				lt.setIdusuario(c.getIdusuario());
				lt.setMotivo(errorsri!=null?errorsri.getMotivo() + ": ":m.getMensaje());
				lt.setDescripcion((errorsri!=null?errorsri.getDescripcion()+" : ":"") + m.getMensaje());
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
				c.setEstado(ComprobanteEstadoEnum.ERROR_SRI.toString());
				cabeceraServicio.actualizar(c);
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR COMUNICACION SRI");
				lt.setDescripcion("Error al enviar comprobante SRI");
				lt.setFecha(new Date());
				lt.setIdusuario(c.getIdusuario());
				lt.setMotivo(TextoUtil.imprimirStackTrace(e, 500));
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param c
	 * @return
	 */
	private boolean enviarEmail(Cabecera c) {
		if(c == null) return false;
		
		Integer enviar = 0;
		
		switch (GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador())) {
		case FACTURA:
			enviar = c.getEstablecimiento().getEnviaremailfactura();
			break;
			
		case RETENCION:
			enviar = c.getEstablecimiento().getEnviaremailretencion();
			break;
			
		case NOTA_CREDITO:
			enviar = c.getEstablecimiento().getEnviaremailnotacredito();
			break;
			
		case NOTA_DEBITO:
			enviar = c.getEstablecimiento().getEnviaremailnotadebito();
			break;
		case GUIA_REMISION:
			enviar = c.getEstablecimiento().getEnviaremailguiaremision();
			break;
		case LIQUIDACION_COMPRA:
			enviar = c.getEstablecimiento().getEnviaremailliqcompra();
			break;

		default:
			break;
		}
		
		return enviar==1;
		
	}
	

}
