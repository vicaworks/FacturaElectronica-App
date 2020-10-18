/**
 * 
 */
package com.vcw.sri.ws.cliente.dto;

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
@XmlRootElement(name = "RespuestaAutorizacionComprobante")
@XmlAccessorType(XmlAccessType.FIELD)
public class RespuestaComprobante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4229802938271815627L;
	
	@XmlElement(name = "claveAccesoConsultada")
	private String claveAccesoConsultada;
	
	@XmlElement(name = "numeroComprobantes")
    private String numeroComprobantes;
    
	@XmlElementRef
	@XmlElementWrapper(name = "autorizaciones")
    private List<Autorizacion> autorizacionList;

	/**
	 * 
	 */
	public RespuestaComprobante() {
	}

	/**
	 * @return the claveAccesoConsultada
	 */
	public String getClaveAccesoConsultada() {
		return claveAccesoConsultada;
	}

	/**
	 * @param claveAccesoConsultada the claveAccesoConsultada to set
	 */
	public void setClaveAccesoConsultada(String claveAccesoConsultada) {
		this.claveAccesoConsultada = claveAccesoConsultada;
	}

	/**
	 * @return the numeroComprobantes
	 */
	public String getNumeroComprobantes() {
		return numeroComprobantes;
	}

	/**
	 * @param numeroComprobantes the numeroComprobantes to set
	 */
	public void setNumeroComprobantes(String numeroComprobantes) {
		this.numeroComprobantes = numeroComprobantes;
	}

	/**
	 * @return the autorizacionList
	 */
	public List<Autorizacion> getAutorizacionList() {
		return autorizacionList;
	}

	/**
	 * @param autorizacionList the autorizacionList to set
	 */
	public void setAutorizacionList(List<Autorizacion> autorizacionList) {
		this.autorizacionList = autorizacionList;
	}

}
