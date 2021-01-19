/**
 * 
 */
package com.vcw.falecpv.web.servicio.sri;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.mako.util.firma.FirmaElectronicaException;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.TextoUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGXsdValidacionEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
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
import com.vcw.falecpv.core.servicio.FirmaElectronicaServicio;
import com.vcw.falecpv.core.servicio.LogtransferenciasriServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;
import com.vcw.falecpv.web.servicio.SriUtilServicio;
import com.vcw.sri.ws.ClienteWsSriServicio;
import com.vcw.sri.ws.recepcion.Mensaje;
import com.vcw.sri.ws.recepcion.RespuestaSolicitud;

/**
 * @author cristianvillarreal
 *
 */
public class ComprobantePendiente extends EnviarComprobanteSRIDecorador {
	
	private DocElectronicoProxy docElectronicoProxy;
	private ParametroGenericoServicio parametroGenericoServicio;
	private CabeceraServicio cabeceraServicio;
	private FirmaElectronicaServicio firmaElectronicaServicio;
	private SriAccesoHelper sriAccesoHelper;
	private EstablecimientoServicio establecimientoServicio;
	private EstadosriServicio estadosriServicio;
	@SuppressWarnings("unused")
	private SriUtilServicio sriUtilServicio;
	private ErrorsriServicio errorsriServicio;
	
	/**
	 * @param enviarComprobanteSRI
	 */
	public ComprobantePendiente(EnviarComprobanteSRI enviarComprobanteSRI) {
		super(enviarComprobanteSRI);
	}

	@Override
	public HashMap<String, Object> enviarComprobante(HashMap<String, Object> parametros)
			throws EnviarComprobanteSRIException {
		
		// parametros iniciales		
		
		docElectronicoProxy = (DocElectronicoProxy) parametros.get("docElectronicoProxy");
		parametroGenericoServicio = (ParametroGenericoServicio)parametros.get("parametroGenericoServicio");
		cabeceraServicio = (CabeceraServicio)parametros.get("cabeceraServicio");
		firmaElectronicaServicio = (FirmaElectronicaServicio) parametros.get("firmaElectronicaServicio");
		sriAccesoHelper = (SriAccesoHelper)parametros.get("sriAccesoHelper");
		establecimientoServicio = (EstablecimientoServicio)parametros.get("establecimientoServicio");
		contadorPkServicio = (ContadorPkServicio)parametros.get("contadorPkServicio");
		estadosriServicio = (EstadosriServicio)parametros.get("estadosriServicio");
		sriUtilServicio = (SriUtilServicio)parametros.get("sriUtilServicio");
		logtransferenciasriServicio = (LogtransferenciasriServicio)parametros.get("logtransferenciasriServicio");
		errorsriServicio = (ErrorsriServicio)parametros.get("errorsriServicio");
		
		Cabecera c = (Cabecera) parametros.get("cabecera");
		String idusuario = (String)parametros.get("idUsuario");
		String xmlDocElectronico = null;
		
			
		// 1. armar xml
		
		try {
			xmlDocElectronico = docElectronicoProxy.getDocElectronicoFacade(c.getIdcabecera(),
					c.getEstablecimiento().getIdestablecimiento(),
					GenTipoDocumentoEnum.getEnumByIdentificador(c.getTipocomprobante().getIdentificador()));
		} catch (Exception e) {
			c.setEstado(ComprobanteEstadoEnum.ERROR.toString());
			try {
				cabeceraServicio.actualizar(c);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR INTERNO SISTEMA");
				lt.setDescripcion("Error al generar el Xml del comprobante.");
				lt.setFecha(new Date());
				lt.setIdusuario(idusuario);
				lt.setMotivo(TextoUtil.imprimirStackTrace(e, 500));
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		} 
			
		// 2. validar 
		try {
			validar(xmlDocElectronico, c.getNumdocumento(), c.getTipocomprobante().getIdentificador());
		} catch (Exception e) {
			c.setEstado(ComprobanteEstadoEnum.ERROR.toString());
			try {
				cabeceraServicio.actualizar(c);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR INTERNO SISTEMA");
				lt.setDescripcion("Error al validar el comprobante XML.");
				lt.setFecha(new Date());
				lt.setIdusuario(idusuario);
				lt.setMotivo(TextoUtil.imprimirStackTrace(e, 500));
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		// 3. firmar
		
		try {
			xmlDocElectronico = firmaElectronicaServicio.firmarXmlFacade(c.getEstablecimiento().getEmpresa().getIdempresa(), xmlDocElectronico);
		} catch (FirmaElectronicaException e) {
			c.setEstado(ComprobanteEstadoEnum.ERROR.toString());
			try {
				cabeceraServicio.actualizar(c);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR FIRMA ELECTRONICA");
				lt.setDescripcion("Error al firmar el XML del comprobante");
				lt.setFecha(new Date());
				lt.setIdusuario(idusuario);
				lt.setMotivo(e.getMessage());
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			c.setEstado(ComprobanteEstadoEnum.ERROR.toString());
			try {
				cabeceraServicio.actualizar(c);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR FIRMA ELECTRONICA");
				lt.setDescripcion("Error al firmar el XML del comprobante");
				lt.setFecha(new Date());
				lt.setIdusuario(idusuario);
				lt.setMotivo(TextoUtil.imprimirStackTrace(e, 500));
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		// 4. enviar sri
		RespuestaSolicitud rs = null;
		try {
			
			Establecimiento e = establecimientoServicio.consultarByPk(c.getEstablecimiento().getIdestablecimiento());
			SriAccesoDto sriAccesoDto = sriAccesoHelper.consultarDatosAcceso("RECEPCION", e.getAmbiente().equals("2"));
			ClienteWsSriServicio wsAutorizacion = new ClienteWsSriServicio(sriAccesoDto.getWsdl());
			//FileUtils.writeStringToFile(new File("/app/docElectronicos/example1.xml"), xmlDocElectronico);
			//xmlDocElectronico = FileUtils.readFileToString(new File("/app/docElectronicos/example1.xml"));
			rs = wsAutorizacion.validarComprobanteFacade(xmlDocElectronico);
			
			if(rs.getEstado()!=null && rs.getEstado().equals("RECIBIDA")) {
				c.setEstado(ComprobanteEstadoEnum.RECIBIDO_SRI.toString());
				cabeceraServicio.actualizar(c);
			}else {
				if(rs.getComprobantes().getComprobante().get(0).getMensajes()!=null && !rs.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().isEmpty()) {
					
					c.setEstado(ComprobanteEstadoEnum.ERROR_SRI.toString());
					cabeceraServicio.actualizar(c);
					
					Mensaje m = rs.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0);
					Logtransferenciasri lt = new Logtransferenciasri();
					lt.setCabecera(c);
					Estadosri estadoSri = estadosriServicio.consultarByPk("6");
					Errorsri errorsri = errorsriServicio.consultarByPk(m.getIdentificador());
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
				
			}
			
		} catch (Exception e) {
			try {
				cabeceraServicio.actualizar(c);
				Logtransferenciasri lt = new Logtransferenciasri();
				lt.setCabecera(c);
				Estadosri estadoSri = new Estadosri();
				estadoSri.setIdestadosri("0");
				lt.setEstadosri(estadoSri);
				lt.setEtiqueta("ERROR COMUNICACION SRI");
				lt.setDescripcion("Error al enviar comprobante SRI");
				lt.setFecha(new Date());
				lt.setIdusuario(idusuario);
				lt.setMotivo(TextoUtil.imprimirStackTrace(e, 500));
				super.registrarlog(lt, c.getEstablecimiento().getIdestablecimiento());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
		return super.enviarComprobante(parametros);
			
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlDocElectronico
	 * @param numDocumento
	 * @param tipoDocumentoIdentificador
	 * @throws NumberFormatException
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void validar(String xmlDocElectronico,String numDocumento,String tipoDocumentoIdentificador) throws NumberFormatException, DaoException, ParametroRequeridoException, SAXException, IOException {
			
		ParametroGenericoBaseEnum pGXsdValidacionEnum = null;
		
		switch (tipoDocumentoIdentificador) {
		case "01":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_FACTURA;
			break;
		case "07":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_RETENCION;
			break;	
		case "05":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_NOTA_DEBITO;
			break;
		case "08":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_GUIA_REMISION;
			break;
		case "03":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_LIQUIDACION_COMPRA;
			break;
		case "04":
			pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_NOTA_CREDITO;
			break;
		default:
			break;
		}
		
		
		String xsdname = parametroGenericoServicio.consultarParametro(pGXsdValidacionEnum, TipoRetornoParametroGenerico.STRING);
		String path =  parametroGenericoServicio.consultarParametro(PGXsdValidacionEnum.XSD_PATH, TipoRetornoParametroGenerico.STRING) + xsdname;
		
		// 2. inicializamos el excel
		validarXml(path, xmlDocElectronico, numDocumento);
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xsdPath
	 * @param xml
	 * @param numDoc
	 * @throws SAXException
	 * @throws IOException
	 */
	private void validarXml(String xsdPath,String xml,String numDoc) throws SAXException, IOException {
		SchemaFactory factory = 
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");//(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdPath));
        Validator validator = schema.newValidator();
        File tempXls = File.createTempFile("xml-" + numDoc, ".xml");
        FileUtils.copyInputStreamToFile(IOUtils.toInputStream(xml), tempXls);
        validator.validate(new StreamSource(tempXls));
	}	

}
