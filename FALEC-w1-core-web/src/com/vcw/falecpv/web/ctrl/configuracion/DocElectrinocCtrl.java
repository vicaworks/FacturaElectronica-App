/**
 * 
 */
package com.vcw.falecpv.web.ctrl.configuracion;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
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
	
	private String tipoDocumentoIdentificador;
	private String numDocumento;
	private String xmlDocElectronico;
	
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
			
			xmlDocElectronico = null;
			String idCabecera = docElectronicoServicio.getIdDocElectronico(tipoDocumentoIdentificador, AppJsfUtil.getEstablecimiento().getIdestablecimiento(), numDocumento);
			xmlDocElectronico = docElectronicoProxy.getDocElectronicoFacade(idCabecera,
					AppJsfUtil.getEstablecimiento().getIdestablecimiento(),
					GenTipoDocumentoEnum.getEnumByIdentificador(tipoDocumentoIdentificador));
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void validar() {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
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
	
}
