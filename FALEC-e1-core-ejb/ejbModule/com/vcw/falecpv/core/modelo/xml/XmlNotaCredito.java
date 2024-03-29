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
@XmlRootElement(name = "notaCredito")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNotaCredito implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4064525754159896693L;
	
	@XmlAttribute
	private String id="comprobante";
	
	@XmlAttribute
	private String version="1.0.0";
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlinfoNotaCredito infoNotaCredito;
	
	@XmlElementRef
	@XmlElementWrapper(name = "detalles")
	private List<XmlNotaCreditoDetalle> notaCreditoDetalleList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
	// Valores transient
	@XmlTransient
	private Date fechaAutorizacion;
	
	@XmlTransient
	private String numeroAutorizacion;
	
	@XmlTransient
	private List<XmlTotalComprobante> totalComprobanteList;
	
	@XmlTransient
	private String pathLogo;
	

	/**
	 * 
	 */
	public XmlNotaCredito() {
		
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
	 * @return the notaCreditoDetalleList
	 */
	public List<XmlNotaCreditoDetalle> getNotaCreditoDetalleList() {
		return notaCreditoDetalleList;
	}


	/**
	 * @param notaCreditoDetalleList the notaCreditoDetalleList to set
	 */
	public void setNotaCreditoDetalleList(List<XmlNotaCreditoDetalle> notaCreditoDetalleList) {
		this.notaCreditoDetalleList = notaCreditoDetalleList;
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

	/**
	 * @return the totalComprobanteList
	 */
	public List<XmlTotalComprobante> getTotalComprobanteList() {
		return totalComprobanteList;
	}

	/**
	 * @param totalComprobanteList the totalComprobanteList to set
	 */
	public void setTotalComprobanteList(List<XmlTotalComprobante> totalComprobanteList) {
		this.totalComprobanteList = totalComprobanteList;
	}

	/**
	 * @return the pathLogo
	 */
	public String getPathLogo() {
		return pathLogo;
	}

	/**
	 * @param pathLogo the pathLogo to set
	 */
	public void setPathLogo(String pathLogo) {
		this.pathLogo = pathLogo;
	}

}
