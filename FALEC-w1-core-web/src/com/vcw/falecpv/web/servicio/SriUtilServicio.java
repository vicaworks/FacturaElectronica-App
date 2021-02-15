/**
 * 
 */
package com.vcw.falecpv.web.servicio;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.mako.util.firma.FirmaElectronicaException;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.ComprobanteEstadoEnum;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGXsdValidacionEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
import com.vcw.falecpv.core.exception.EnvioEmailException;
import com.vcw.falecpv.core.exception.FirmarXmlException;
import com.vcw.falecpv.core.exception.KeystorePassException;
import com.vcw.falecpv.core.exception.RideSriException;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.servicio.CabeceraServicio;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.FirmaElectronicaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;

/**
 * @author cristianvillarreal
 *
 */
/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SriUtilServicio {
	
	@EJB
	private DocElectronicoProxy docElectronicoProxy;
	
	@EJB
	private CabeceraServicio cabeceraServicio;
	
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;

	@EJB
	private FirmaElectronicaServicio firmaElectronicaServicio;
	
	@EJB
	private EmpresaServicio empresaServicio;
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 * @throws JAXBException 
	 */
	public String generarXml(Cabecera cabecera)throws DaoException, JAXBException{
		
		String xmlDocElectronico = docElectronicoProxy.getDocElectronicoFacade(cabecera.getIdcabecera(),
					cabecera.getEstablecimiento().getIdestablecimiento(),
					GenTipoDocumentoEnum.getEnumByIdentificador(cabecera.getTipocomprobante().getIdentificador()));
		
		return xmlDocElectronico;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param xmlNoSign
	 * @return
	 * @throws DaoException
	 * @throws NumberFormatException
	 * @throws ParametroRequeridoException
	 */
	public boolean validarXSDComprobante(String numDocumento,String identificadorComprobante,String xmlNoSign) throws DaoException, NumberFormatException, ParametroRequeridoException {
		
		try {
			
			ParametroGenericoBaseEnum pGXsdValidacionEnum = null;
			
			switch (identificadorComprobante) {
			case "01":
				pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_FACTURA;
				break;
			case "07":
				pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_RETENCION;
				break;	
			case "05":
				pGXsdValidacionEnum = PGXsdValidacionEnum.XSD_NOTA_DEBITO;
				break;
			case "06":
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
			
			String path = parametroGenericoServicio.consultarParametro(PGXsdValidacionEnum.XSD_PATH, TipoRetornoParametroGenerico.STRING) + xsdname;
			
			validarXml(path, xmlNoSign, numDocumento);
			return true;
			
		} catch (SAXException e) {
			return false;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
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
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlToSign
	 * @return
	 * @throws KeystorePassException
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws FirmarXmlException
	 */
	public String firmarXml(String xmlToSign,String idEmpresa)throws DaoException,KeystorePassException,FirmaElectronicaException, CertificateException, IOException{
		
		Empresa emp = null;
		
		try {
			
			emp = empresaServicio.consultarByPk(idEmpresa);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return firmaElectronicaServicio.firmarXml(xmlToSign, emp.getArchivofirmaelectronica(), emp.getClavefirmaelectronica());
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws RideSriException
	 */
	public byte[] generarRide(String xmlComprobante)throws RideSriException{
		return null;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlSign
	 * @param sriRide
	 */
	public void enviarEmail(String xmlSign,byte[] sriRide)throws EnvioEmailException {
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @param estado
	 * @return
	 * @throws DaoException
	 */
	public Cabecera cambiarEstado(String idCabecera,ComprobanteEstadoEnum estadoEnum,String observacion)throws DaoException{
		
		Cabecera c = cabeceraServicio.consultarByPk(idCabecera);
		c.setEstado(estadoEnum.toString());
		c.getEstadoautorizacion();
		
		return c;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idCabecera
	 * @return
	 * @throws DaoException
	 */
	public HashMap<String, Object> validacionInicial(String idCabecera)throws DaoException{
		HashMap<String, Object> resultado = new HashMap<>();
		Cabecera c = null;
		try {
			
			
			c = cabeceraServicio.consultarByPk(idCabecera);
			
			// 1. valida la estructura de xml
			String xmlToSing = generarXml(c);
			if(!validarXSDComprobante(c.getNumdocumento(),c.getTipocomprobante().getIdentificador(),xmlToSing)) {
				// cambiar el estado del comprobante
				c = cambiarEstado(c.getIdcabecera(), ComprobanteEstadoEnum.ERROR, AppConfiguracion.getString("error.validacioncomprobante"));
				resultado.put("valInicial", false);
				resultado.put("descError", AppConfiguracion.getString("error.validacioncomprobante"));
			}
			
			// 2. valida la fecha de vigencia de la firma electronica
			Empresa e = empresaServicio.consultarByPk(c.getEstablecimiento().getEmpresa().getIdempresa());
			if(FechaUtil.comparaFechas(new Date(), e.getFechaexpiracion())<0) {
				// cambiar el estado del comprobante
				c = cambiarEstado(c.getIdcabecera(), ComprobanteEstadoEnum.ERROR, AppConfiguracion.getString("error.fechafirmaelectronica"));
				resultado.put("valInicial", false);
				resultado.put("descError", AppConfiguracion.getString("error.validacioncomprobante"));
			}
			
			// 3. valida la firma electrónica
			firmarXml(xmlToSing, e.getIdempresa());
			
			resultado.put("valInicial", true);
			return resultado;
			
		} catch (KeystorePassException e) {
			cambiarEstado(c.getIdcabecera(), ComprobanteEstadoEnum.ERROR, AppConfiguracion.getString("error.clavefirma"));
			resultado.put("valInicial", false);
			resultado.put("descError", AppConfiguracion.getString("error.validacioncomprobante"));
			return resultado;
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		
	}
	
}
