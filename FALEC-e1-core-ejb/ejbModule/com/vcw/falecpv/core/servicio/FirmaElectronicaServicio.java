/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Date;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;

import com.mako.util.firma.FirmaElectronicaException;
import com.mako.util.firma.XAdESBESSignature;
import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.vcw.falecpv.core.exception.KeystorePassException;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FirmaElectronicaServicio {

	@Inject
	private EmpresaServicio empresaServicio;
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param xmlToSignIS
	 * @param penSignature
	 * @param passSignature
	 * @return
	 * @throws KeystorePassException
	 * @throws FirmaElectronicaException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public String firmarXml(String xmlToSignIS,byte[] penSignature,String passSignature) throws KeystorePassException,FirmaElectronicaException,CertificateException, IOException{
		StringWriter writer = new StringWriter();
		
		try {
			byte[] docXmlSigned = XAdESBESSignature.firmarFacade(xmlToSignIS.getBytes(),penSignature,passSignature);
			
			InputStream inputStream = new ByteArrayInputStream(docXmlSigned);
			String encoding = StandardCharsets.UTF_8.name();
			IOUtils.copy(inputStream, writer, encoding);
			
		} catch (FirmaElectronicaException e) {
			if(e.getMessage().contains("keystore password was incorrect")) {
				throw new KeystorePassException(AppConfiguracion.getString("error.clavesfirmaelectronica"));
			}
			throw new FirmaElectronicaException(e);
		}
		
		return writer.toString();
		
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param xmlToSignIS
	 * @return
	 * @throws KeystorePassException
	 * @throws FirmaElectronicaException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws DaoException
	 * @throws ParseException
	 */
	public String firmarXmlFacade(String idEmpresa,String xmlToSignIS) throws KeystorePassException,FirmaElectronicaException,CertificateException, IOException, DaoException, ParseException{
		Empresa emp = empresaServicio.consultarByPk(idEmpresa);
		
		if(emp.getArchivofirmaelectronica()==null) {
			throw new FirmaElectronicaException("NO EXISTE ARCHI p12 PARA LA FIRMA");
		}
		
		if(FechaUtil.comparaFechas(emp.getFechaexpiracion(),new Date())<0) {
			throw new FirmaElectronicaException("LA FECHA DE VIGENCIA CADUCO : " + FechaUtil.formatoFecha(emp.getFechaexpiracion()));
		}
		
		return firmarXml(xmlToSignIS, emp.getArchivofirmaelectronica(), emp.getClavefirmaelectronica());
	}
	
	

}
