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
@XmlRootElement(name = "factura")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFactura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5804615403513085034L;

	@XmlAttribute(name = "xmlns:ds")
	private String xmlns_ds="http://www.w3.org/2000/09/xmldsig#";
			
	@XmlAttribute(name = "xmlns:xsi")
	private String xmlns_xsi="http://www.w3.org/2001/XMLSchema-instance";
	
	@XmlAttribute
	private String id="comprobante";
	
	@XmlAttribute
	private String version="2.1.0";
	
	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoFactura infoFactura;
	
	@XmlElementRef
	@XmlElementWrapper(name = "detalles")
	private List<XmlDetalle> detalleList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "reembolsos")
	private List<XmlReembolsoDetalle> reembolsoDetalleList;
	
	@XmlElementRef
	@XmlElementWrapper(name = "retenciones")
	private List<XmlRetencion> retencioneList;
	
	@XmlElement
	private XmlInfoSustitutivaGuiaRemision infoSustitutivaGuiaRemision;
	
	@XmlElementRef
	@XmlElementWrapper(name = "otrosRubrosTerceros")
	private List<XmlRubro> rubroList;
	
	@XmlElementRef
	private XmlTipoNegociable tipoNegociable;
	
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
	public XmlFactura() {
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
	 * @return the infoSustitutivaGuiaRemision
	 */
	public XmlInfoSustitutivaGuiaRemision getInfoSustitutivaGuiaRemision() {
		return infoSustitutivaGuiaRemision;
	}


	/**
	 * @param infoSustitutivaGuiaRemision the infoSustitutivaGuiaRemision to set
	 */
	public void setInfoSustitutivaGuiaRemision(XmlInfoSustitutivaGuiaRemision infoSustitutivaGuiaRemision) {
		this.infoSustitutivaGuiaRemision = infoSustitutivaGuiaRemision;
	}


	/**
	 * @return the retencioneList
	 */
	public List<XmlRetencion> getRetencioneList() {
		return retencioneList;
	}


	/**
	 * @param retencioneList the retencioneList to set
	 */
	public void setRetencioneList(List<XmlRetencion> retencioneList) {
		this.retencioneList = retencioneList;
	}


	/**
	 * @return the rubroList
	 */
	public List<XmlRubro> getRubroList() {
		return rubroList;
	}


	/**
	 * @param rubroList the rubroList to set
	 */
	public void setRubroList(List<XmlRubro> rubroList) {
		this.rubroList = rubroList;
	}


	/**
	 * @return the tipoNegociable
	 */
	public XmlTipoNegociable getTipoNegociable() {
		return tipoNegociable;
	}


	/**
	 * @param tipoNegociable the tipoNegociable to set
	 */
	public void setTipoNegociable(XmlTipoNegociable tipoNegociable) {
		this.tipoNegociable = tipoNegociable;
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
	 * @return the xmlns_ds
	 */
	public String getXmlns_ds() {
		return xmlns_ds;
	}


	/**
	 * @param xmlns_ds the xmlns_ds to set
	 */
	public void setXmlns_ds(String xmlns_ds) {
		this.xmlns_ds = xmlns_ds;
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
