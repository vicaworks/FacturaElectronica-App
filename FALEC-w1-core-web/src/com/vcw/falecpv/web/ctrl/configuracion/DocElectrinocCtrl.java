/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import com.servitec.common.jsf.FacesUtil;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.FechaUtil;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGXsdValidacionEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.ParametroGenericoBaseEnum;
import com.vcw.falecpv.core.exception.KeystorePassException;
import com.vcw.falecpv.core.exception.NoExisteRegistroException;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.FirmaElectronicaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoProxy;
import com.vcw.falecpv.core.servicio.sri.DocElectronicoServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@ViewScoped
public class DocElectrinocCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -686849484022120846L;
	
	@EJB
	private DocElectronicoServicio docElectronicoServicio;
	@EJB
	private DocElectronicoProxy docElectronicoProxy;
	@EJB
	private ParametroGenericoServicio parametroGenericoServicio;
	@EJB
	private FirmaElectronicaServicio firmaElectronicaServicio;
	@EJB
	private EmpresaServicio empresaServicio;
	
	private String tipoDocumentoIdentificador;
	private String numDocumento;
	private String xmlDocElectronico;
	private String xmlError;
	private boolean flagFirma = false;
	
	/**
	 * 
	 */
	public DocElectrinocCtrl() {
	}
	
	@PostConstruct
	public void init() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	@Override
	public void buscar() {
		try {
			flagFirma = false;
			xmlDocElectronico = null;
			xmlError = null;
			String idCabecera = docElectronicoServicio.getIdDocElectronico(tipoDocumentoIdentificador, AppJsfUtil.getEstablecimiento().getIdestablecimiento(), numDocumento);
			xmlDocElectronico = docElectronicoProxy.getDocElectronicoFacade(idCabecera,
					AppJsfUtil.getEstablecimiento().getIdestablecimiento(),
					GenTipoDocumentoEnum.getEnumByIdentificador(tipoDocumentoIdentificador));
		} catch (NoExisteRegistroException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void firmar() {
		try {
			
			if (xmlDocElectronico==null || xmlDocElectronico.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE COMPROBANTE " + msg.getString("label.electronico"));
				return;
			}
			
			if(flagFirma) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "COMPROBANTE " + msg.getString("label.electronico") + " YA ESTA FIRMADO.");
				return;
			}
			
			Empresa emp = empresaServicio.consultarByPk(AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa());
			
			if(emp.getArchivofirmaelectronica()==null) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "NO EXISTE ARCHI p12 PARA LA FIRMA" + msg.getString("label.electronica") + " DE ASIGNAR EL " + msg.getString("label.configuracion")  + " / EMPRESA");
				return;
			}
			
			if(FechaUtil.comparaFechas(emp.getFechaexpiracion(),new Date())<0) {
				AppJsfUtil.addErrorMessage("formMain", "ERROR", "LA FECHA DE VIGENCIA CADUCO : " + FechaUtil.formatoFecha(emp.getFechaexpiracion()));
				return;
			}
			
			
			xmlDocElectronico = firmaElectronicaServicio.firmarXml(xmlDocElectronico, emp.getArchivofirmaelectronica(), emp.getClavefirmaelectronica());
			flagFirma = true;
			AppJsfUtil.addInfoMessage("formMain", "OK", "Firmado correctamente");
		} catch (KeystorePassException e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void validar() {
		try {
			
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
			String path = FacesUtil.getServletContext().getRealPath(
					AppConfiguracion.getString("dir.base.xsd") + xsdname);
			
			// 2. inicializamos el excel
//			File xsdFile = new File(path);
//			System.out.println(xsdFile.getPath());
			validarXml(path, xmlDocElectronico, numDocumento);
			xmlError = "OK";
		} catch (SAXException e) {
			e.printStackTrace();
			xmlError = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	private void validarXml(String xsdPath,String xml,String numDoc) throws SAXException, IOException {
		SchemaFactory factory = 
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");//(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
        Schema schema = factory.newSchema(new File(xsdPath));
        
        Validator validator = schema.newValidator();
        File tempXls = File.createTempFile("xml-" + numDoc, ".xml");
        FileUtils.copyInputStreamToFile(IOUtils.toInputStream(xml,StandardCharsets.UTF_8), tempXls);
        validator.validate(new StreamSource(tempXls));
	}

	/**
	 * @return the tipoDocumentoIdentificador
	 */
	public String getTipoDocumentoIdentificador() {
		return tipoDocumentoIdentificador;
	}

	/**
	 * @param tipoDocumentoIdentificador the tipoDocumentoIdentificador to set
	 */
	public void setTipoDocumentoIdentificador(String tipoDocumentoIdentificador) {
		this.tipoDocumentoIdentificador = tipoDocumentoIdentificador;
	}

	/**
	 * @return the numDocumento
	 */
	public String getNumDocumento() {
		return numDocumento;
	}

	/**
	 * @param numDocumento the numDocumento to set
	 */
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	/**
	 * @return the xmlDocElectronico
	 */
	public String getXmlDocElectronico() {
		return xmlDocElectronico;
	}

	/**
	 * @param xmlDocElectronico the xmlDocElectronico to set
	 */
	public void setXmlDocElectronico(String xmlDocElectronico) {
		this.xmlDocElectronico = xmlDocElectronico;
	}

	/**
	 * @return the xmlError
	 */
	public String getXmlError() {
		return xmlError;
	}

	/**
	 * @param xmlError the xmlError to set
	 */
	public void setXmlError(String xmlError) {
		this.xmlError = xmlError;
	}

	/**
	 * @return the flagFirma
	 */
	public boolean isFlagFirma() {
		return flagFirma;
	}

	/**
	 * @param flagFirma the flagFirma to set
	 */
	public void setFlagFirma(boolean flagFirma) {
		this.flagFirma = flagFirma;
	}
	
}
