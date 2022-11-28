package com.vcw.falecpv.core.modelo.xml;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriDate;
import com.vcw.falecpv.core.modelo.xml.adapter.XmlAdapterSriNumero;

@XmlRootElement(name = "docSustento")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlDocSustento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4620913996963738457L;

	@XmlElement
	private String codSustento;
	
	@XmlElement
	private String codDocSustento;
	
	@XmlElement
	private String numDocSustento;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaEmisionDocSustento;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriDate.class)
	private Date fechaRegistroContable;
	
	@XmlElement
	private String numAutDocSustento;
	
	@XmlElement
	private String pagoLocExt;
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double totalSinImpuestos;
	
	
	@XmlElement
	@XmlJavaTypeAdapter(XmlAdapterSriNumero.class)
	private Double importeTotal;
	
	
	@XmlElementRef
	@XmlElementWrapper(name = "impuestosDocSustento")
	private List<XmlDocSustento> docSustentoList = null;
	
	@XmlElementRef
	@XmlElementWrapper(name = "retenciones")
	private List<XmlRetencionV2> retencionList = null; 
	
	
	
	public XmlDocSustento() {
		
	}



	/**
	 * @return the codSustento
	 */
	public String getCodSustento() {
		return codSustento;
	}



	/**
	 * @param codSustento the codSustento to set
	 */
	public void setCodSustento(String codSustento) {
		this.codSustento = codSustento;
	}



	/**
	 * @return the codDocSustento
	 */
	public String getCodDocSustento() {
		return codDocSustento;
	}



	/**
	 * @param codDocSustento the codDocSustento to set
	 */
	public void setCodDocSustento(String codDocSustento) {
		this.codDocSustento = codDocSustento;
	}



	/**
	 * @return the numDocSustento
	 */
	public String getNumDocSustento() {
		return numDocSustento;
	}



	/**
	 * @param numDocSustento the numDocSustento to set
	 */
	public void setNumDocSustento(String numDocSustento) {
		this.numDocSustento = numDocSustento;
	}



	/**
	 * @return the fechaEmisionDocSustento
	 */
	public Date getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}



	/**
	 * @param fechaEmisionDocSustento the fechaEmisionDocSustento to set
	 */
	public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}



	/**
	 * @return the fechaRegistroContable
	 */
	public Date getFechaRegistroContable() {
		return fechaRegistroContable;
	}



	/**
	 * @param fechaRegistroContable the fechaRegistroContable to set
	 */
	public void setFechaRegistroContable(Date fechaRegistroContable) {
		this.fechaRegistroContable = fechaRegistroContable;
	}



	/**
	 * @return the numAutDocSustento
	 */
	public String getNumAutDocSustento() {
		return numAutDocSustento;
	}



	/**
	 * @param numAutDocSustento the numAutDocSustento to set
	 */
	public void setNumAutDocSustento(String numAutDocSustento) {
		this.numAutDocSustento = numAutDocSustento;
	}



	/**
	 * @return the pagoLocExt
	 */
	public String getPagoLocExt() {
		return pagoLocExt;
	}



	/**
	 * @param pagoLocExt the pagoLocExt to set
	 */
	public void setPagoLocExt(String pagoLocExt) {
		this.pagoLocExt = pagoLocExt;
	}



	/**
	 * @return the totalSinImpuestos
	 */
	public Double getTotalSinImpuestos() {
		return totalSinImpuestos;
	}



	/**
	 * @param totalSinImpuestos the totalSinImpuestos to set
	 */
	public void setTotalSinImpuestos(Double totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}



	/**
	 * @return the importeTotal
	 */
	public Double getImporteTotal() {
		return importeTotal;
	}



	/**
	 * @param importeTotal the importeTotal to set
	 */
	public void setImporteTotal(Double importeTotal) {
		this.importeTotal = importeTotal;
	}



	/**
	 * @return the docSustentoList
	 */
	public List<XmlDocSustento> getDocSustentoList() {
		return docSustentoList;
	}



	/**
	 * @param docSustentoList the docSustentoList to set
	 */
	public void setDocSustentoList(List<XmlDocSustento> docSustentoList) {
		this.docSustentoList = docSustentoList;
	}



	/**
	 * @return the retencionList
	 */
	public List<XmlRetencionV2> getRetencionList() {
		return retencionList;
	}



	/**
	 * @param retencionList the retencionList to set
	 */
	public void setRetencionList(List<XmlRetencionV2> retencionList) {
		this.retencionList = retencionList;
	}
	
}
