/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
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

	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoGuiaRemision infoGuiaRemision;
	
	@XmlElementRef(name = "destinatarios")
	private List<XmlDestinatario> destinatarioList;
	
	@XmlElementRef(name = "infoAdicional")
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

}
