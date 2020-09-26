/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "guiaRemision")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlGuiaRemision implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1363645481170869349L;
	
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
	private XmlInfoGuiaRemision infoGuiaRemision;
	
	@XmlElementRef
	@XmlElementWrapper(name = "destinatarios")
	private List<XmlDestinatario> destinatarioList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
	/**
	 * 
	 */
	public XmlGuiaRemision() {
		
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
	 * @return the infoGuiaRemision
	 */
	public XmlInfoGuiaRemision getInfoGuiaRemision() {
		return infoGuiaRemision;
	}



	/**
	 * @param infoGuiaRemision the infoGuiaRemision to set
	 */
	public void setInfoGuiaRemision(XmlInfoGuiaRemision infoGuiaRemision) {
		this.infoGuiaRemision = infoGuiaRemision;
	}



	/**
	 * @return the destinatarioList
	 */
	public List<XmlDestinatario> getDestinatarioList() {
		return destinatarioList;
	}



	/**
	 * @param destinatarioList the destinatarioList to set
	 */
	public void setDestinatarioList(List<XmlDestinatario> destinatarioList) {
		this.destinatarioList = destinatarioList;
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

}
