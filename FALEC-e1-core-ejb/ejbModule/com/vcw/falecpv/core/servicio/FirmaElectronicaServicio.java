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

import javax.ejb.Stateless;

import org.apache.commons.io.IOUtils;

import com.mako.util.firma.FirmaElectronicaException;
import com.mako.util.firma.XAdESBESSignature;
import com.servitec.common.util.AppConfiguracion;
import com.vcw.falecpv.core.exception.KeystorePassException;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class FirmaElectronicaServicio {

	
	
	
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

}
