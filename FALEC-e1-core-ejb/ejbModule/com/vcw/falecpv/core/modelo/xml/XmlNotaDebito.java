/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "notaDebito")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNotaDebito implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1592149953320156125L;
	
	@XmlAttribute
	private String id="comprobante";
	
	@XmlAttribute
	private String version="1.0.0";
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElement
	private XmlInfoNotaDebito infoNotaDebito;
	
	@XmlElementRef
	@XmlElementWrapper(name = "motivos")
	private List<XmlMotivo> motivoList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;

	/**
	 * 
	 */
	public XmlNotaDebito() {
		
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
	 * @return the motivoList
	 */
	public List<XmlMotivo> getMotivoList() {
		return motivoList;
	}



	/**
	 * @param motivoList the motivoList to set
	 */
	public void setMotivoList(List<XmlMotivo> motivoList) {
		this.motivoList = motivoList;
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
	 * @return the infoNotaDebito
	 */
	public XmlInfoNotaDebito getInfoNotaDebito() {
		return infoNotaDebito;
	}


	/**
	 * @param infoNotaDebito the infoNotaDebito to set
	 */
	public void setInfoNotaDebito(XmlInfoNotaDebito infoNotaDebito) {
		this.infoNotaDebito = infoNotaDebito;
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

}
