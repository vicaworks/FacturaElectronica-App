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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "liquidacionCompra")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLiquidacionCompra implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3017442037988689489L;

	@XmlAttribute
	private String id="comprobante";
	
	@XmlAttribute
	private String version="1.1.0";
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoLiquidacionCompra infoLiquidacionCompra;
	
	@XmlElementRef
	@XmlElementWrapper(name = "detalles")
	private List<XmlDetalle> detalleList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "reembolsos")
	private List<XmlReembolsoDetalle> reembolsoDetalleList;
	
	@XmlElement
	private XmlMaquinaFiscal maquinaFiscal;
	
	
	@XmlElementRef
	@XmlElementWrapper(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
	// Valores transient
	@XmlTransient
	private Date fechaAutorizacion;
	
	@XmlTransient
	private String numeroAutorizacion;
	
	@XmlTransient
	private String pathLogo;
	
	@XmlTransient
	private List<XmlTotalComprobante> totalComprobanteList;
		
	
	/**
	 * 
	 */
	public XmlLiquidacionCompra() {
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
	 * @return the infoLiquidacionCompra
	 */
	public XmlInfoLiquidacionCompra getInfoLiquidacionCompra() {
		return infoLiquidacionCompra;
	}



	/**
	 * @param infoLiquidacionCompra the infoLiquidacionCompra to set
	 */
	public void setInfoLiquidacionCompra(XmlInfoLiquidacionCompra infoLiquidacionCompra) {
		this.infoLiquidacionCompra = infoLiquidacionCompra;
	}



	/**
	 * @return the detalleList
	 */
	public List<XmlDetalle> getDetalleList() {
		return detalleList;
	}



	/**
	 * @param detalleList the detalleList to set
	 */
	public void setDetalleList(List<XmlDetalle> detalleList) {
		this.detalleList = detalleList;
	}



	/**
	 * @return the reembolsoDetalleList
	 */
	public List<XmlReembolsoDetalle> getReembolsoDetalleList() {
		return reembolsoDetalleList;
	}



	/**
	 * @param reembolsoDetalleList the reembolsoDetalleList to set
	 */
	public void setReembolsoDetalleList(List<XmlReembolsoDetalle> reembolsoDetalleList) {
		this.reembolsoDetalleList = reembolsoDetalleList;
	}



	/**
	 * @return the maquinaFiscal
	 */
	public XmlMaquinaFiscal getMaquinaFiscal() {
		return maquinaFiscal;
	}



	/**
	 * @param maquinaFiscal the maquinaFiscal to set
	 */
	public void setMaquinaFiscal(XmlMaquinaFiscal maquinaFiscal) {
		this.maquinaFiscal = maquinaFiscal;
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

}
