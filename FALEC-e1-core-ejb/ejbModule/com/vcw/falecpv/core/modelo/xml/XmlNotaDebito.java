/**
 * 
 */
package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElement
	private XmlinfoNotaCredito infoNotaCredito;
	
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
	 * @return the infoNotaCredito
	 */
	public XmlinfoNotaCredito getInfoNotaCredito() {
		return infoNotaCredito;
	}



	/**
	 * @param infoNotaCredito the infoNotaCredito to set
	 */
	public void setInfoNotaCredito(XmlinfoNotaCredito infoNotaCredito) {
		this.infoNotaCredito = infoNotaCredito;
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

}
