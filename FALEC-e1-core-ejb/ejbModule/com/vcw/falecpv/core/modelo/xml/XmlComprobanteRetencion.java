/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
	
	
	@XmlAttribute(name = "xmlns:ns1")
	private String xmlns_ns1="http://www.w3.org/2000/09/xmldsig#";
	
	@XmlAttribute(name = "xmlns:xsi")
	private String xmlns_xsi="http://www.w3.org/2001/XMLSchema-instance";
	
	@XmlAttribute
	private String id="comprobante";
	
	@XmlAttribute
	private String version="1.0.0";
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoCompRetencion infoCompRetencion;
	
	@XmlElementRef
	@XmlElementWrapper(name = "impuestos")
	private List<XmlImpuestoRetencion> impuestoretencionList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
	// Valores transient
	@XmlTransient
	private Date fechaAutorizacion;
	
	@XmlTransient
	private String numeroAutorizacion;
	
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

	/**
	 * @return the infoCompRetencion
	 */
	public XmlInfoCompRetencion getInfoCompRetencion() {
		return infoCompRetencion;
	}

	/**
	 * @param infoCompRetencion the infoCompRetencion to set
	 */
	public void setInfoCompRetencion(XmlInfoCompRetencion infoCompRetencion) {
		this.infoCompRetencion = infoCompRetencion;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the xmlns_ns1
	 */
	public String getXmlns_ns1() {
		return xmlns_ns1;
	}

	/**
	 * @param xmlns_ns1 the xmlns_ns1 to set
	 */
	public void setXmlns_ns1(String xmlns_ns1) {
		this.xmlns_ns1 = xmlns_ns1;
	}

	/**
	 * @return the xmlns_xsi
	 */
	public String getXmlns_xsi() {
		return xmlns_xsi;
	}

	/**
	 * @param xmlns_xsi the xmlns_xsi to set
	 */
	public void setXmlns_xsi(String xmlns_xsi) {
		this.xmlns_xsi = xmlns_xsi;
	}

	/**
	 * @return the fechaAutorizacion
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

}
