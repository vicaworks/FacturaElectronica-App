/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "comprobanteRetencion")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlComprobanteRetencion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8549899996004415625L;
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoFactura infoFactura;
	
	@XmlElementRef
	@XmlElementWrapper(name = "impuestos")
	private List<XmlImpuestoRetencion> impuestoretencionList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
	/**
	 * 
	 */
	public XmlComprobanteRetencion() {
	}

	/**
	 * @return the infoTributaria
	 */
	public XmlInfoTributaria getInfoTributaria() {
		return infoTributaria;
	}

	/**
	 * @param infoTributaria the infoTributaria to set
	 */
	public void setInfoTributaria(XmlInfoTributaria infoTributaria) {
		this.infoTributaria = infoTributaria;
	}

	/**
	 * @return the infoFactura
	 */
	public XmlInfoFactura getInfoFactura() {
		return infoFactura;
	}

	/**
	 * @param infoFactura the infoFactura to set
	 */
	public void setInfoFactura(XmlInfoFactura infoFactura) {
		this.infoFactura = infoFactura;
	}

	/**
	 * @return the impuestoretencionList
	 */
	public List<XmlImpuestoRetencion> getImpuestoretencionList() {
		return impuestoretencionList;
	}

	/**
	 * @param impuestoretencionList the impuestoretencionList to set
	 */
	public void setImpuestoretencionList(List<XmlImpuestoRetencion> impuestoretencionList) {
		this.impuestoretencionList = impuestoretencionList;
	}

	/**
	 * @return the campoAdicionalList
	 */
	public List<XmlCampoAdicional> getCampoAdicionalList() {
		return campoAdicionalList;
	}

	/**
	 * @param campoAdicionalList the campoAdicionalList to set
	 */
	public void setCampoAdicionalList(List<XmlCampoAdicional> campoAdicionalList) {
		this.campoAdicionalList = campoAdicionalList;
	}

}
