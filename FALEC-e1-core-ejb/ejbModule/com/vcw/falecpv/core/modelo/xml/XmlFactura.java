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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author cristianvillarreal
 *
 */
@XmlRootElement(name = "infoFactura")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFactura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5804615403513085034L;

	@XmlElementRef
	private XmlInfoTributaria infoTributaria;
	
	@XmlElementRef
	private XmlInfoFactura infoFactura;
	
	@XmlElementRef(name = "detalles")
	private List<XmlDetalle> detalleList;
	
	@XmlElementRef(name = "reembolsos")
	private List<XmlReembolsoDetalle> reembolsoDetalleList;
	
	@XmlElementRef(name = "retenciones")
	private List<XmlRetencion> retencioneList;
	
	@XmlElement
	private XmlInfoSustitutivaGuiaRemision infoSustitutivaGuiaRemision;
	
	@XmlElementRef(name = "otrosRubrosTerceros")
	private List<XmlRubro> rubroList;
	
	@XmlElementRef
	private XmlTipoNegociable tipoNegociable;
	
	@XmlElementRef(name = "infoAdicional")
	private List<XmlCampoAdicional> campoAdicionalList;
	
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

}
